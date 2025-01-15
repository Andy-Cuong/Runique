package com.andyc.core.data.run

import com.andyc.core.database.dao.RunPendingSyncDao
import com.andyc.core.database.mappers.toRun
import com.andyc.core.domain.SessionStorage
import com.andyc.core.domain.run.LocalRunDataSource
import com.andyc.core.domain.run.RemoteRunDataSource
import com.andyc.core.domain.run.Run
import com.andyc.core.domain.run.RunId
import com.andyc.core.domain.run.RunRepository
import com.andyc.core.domain.util.DataError
import com.andyc.core.domain.util.EmptyResult
import com.andyc.core.domain.util.Result
import com.andyc.core.domain.util.asEmptyResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OfflineFirstRunRepository(
    private val localRunDataSource: LocalRunDataSource,
    private val remoteRunDataSource: RemoteRunDataSource,
    private val applicationScope: CoroutineScope,
    private val runPendingSyncDao: RunPendingSyncDao,
    private val sessionStorage: SessionStorage
): RunRepository {
    override fun getRuns(): Flow<List<Run>> {
        return localRunDataSource.getRuns()
    }

    override suspend fun fetchRuns(): EmptyResult<DataError> {
        return when (val result = remoteRunDataSource.getRuns()) {
            is Result.Error -> result.asEmptyResult()
            is Result.Success -> {
                applicationScope.async {
                    localRunDataSource.upsertRuns(result.data).asEmptyResult()
                }.await()
            }
        }
    }

    override suspend fun upsertRun(
        run: Run,
        mapPicture: ByteArray
    ): EmptyResult<DataError> {
        val localResult = localRunDataSource.upsertRun(run)
        if (localResult !is Result.Success) {
            return localResult.asEmptyResult()
        }

        val runWithId = run.copy(id = localResult.data)
        val remoteResult = remoteRunDataSource.postRun(
            run = runWithId,
            mapPicture = mapPicture
        )

        return when (remoteResult) {
            is Result.Error -> {
                Result.Success(Unit) // TODO: Handle later
            }
            is Result.Success -> {
                applicationScope.async {
                    localRunDataSource.upsertRun(remoteResult.data).asEmptyResult()
                }.await()
            }
        }
    }

    override suspend fun deleteRun(id: RunId) {
        localRunDataSource.deleteRun(id)

        // Edge case where the run is created in offline-mode, and deleted in offline-mode as well
        // without being synced with the remote database. In that case, we don't need to sync anything
        val isPendingSync = runPendingSyncDao.getRunPendingSyncEntity(id) != null
        if (isPendingSync) {
            runPendingSyncDao.deleteRunPendingSyncEntity(id)
            return
        }

        val remoteResult = applicationScope.async {
            remoteRunDataSource.deleteRun(id)
        }.await()
    }

    override suspend fun syncPendingRuns() {
        withContext(Dispatchers.IO) {
            val userId = sessionStorage.get()?.userId ?: return@withContext

            val createdRuns = async {
                runPendingSyncDao.getAllRunPendingSyncEntities(userId)
            }
            val deletedRuns = async {
                runPendingSyncDao.getAllDeletedRunSyncEntities(userId)
            }

            val createJobs = createdRuns.await()
                .map {
                    launch {
                        val run = it.run.toRun()
                        when (remoteRunDataSource.postRun(run, it.mapPictureBytes)) {
                            is Result.Error -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    runPendingSyncDao.deleteRunPendingSyncEntity(it.runId)
                                }.join()
                            }
                        }
                    }
                }

            val deleteJobs = deletedRuns.await()
                .map {
                    launch {
                        when (remoteRunDataSource.deleteRun(it.runId)) {
                            is Result.Error -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    runPendingSyncDao.deleteDeletedRunSyncEntity(it.runId)
                                }.join()
                            }
                        }
                    }
                }

            createJobs.forEach { it.join() }
            deleteJobs.forEach { it.join() }
        }
    }
}
package com.andyc.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.andyc.core.database.entity.DeletedRunSyncEntity
import com.andyc.core.database.entity.RunPendingSyncEntity

@Dao
interface RunPendingSyncDao {
    // Sync locally created runs
    @Query("SELECT * FROM RunPendingSyncEntity WHERE userId=:userId")
    suspend fun getAllRunPendingSyncEntities(userId: String): List<RunPendingSyncEntity>

    @Query("SELECT * FROM RunPendingSyncEntity WHERE runId=:runId")
    suspend fun getRunPendingSyncEntity(runId: String): RunPendingSyncEntity?

    @Upsert
    suspend fun upsertRunPendingSyncEntity(entity: RunPendingSyncEntity)

    @Query("DELETE FROM RunPendingSyncEntity WHERE runId=:runId")
    suspend fun deleteRunPendingSyncEntity(runId: String)

    // Remotely delete locally deleted runs
    @Query("SELECT * FROM DeletedRunSyncEntity WHERE userId=:userId")
    suspend fun getAllDeletedRunSyncEntities(userId: String): List<DeletedRunSyncEntity>

    @Upsert
    suspend fun upsertDeletedSyncEntity(entity: DeletedRunSyncEntity)

    @Query("DELETE FROM DeletedRunSyncEntity WHERE runId=:runId")
    suspend fun deleteDeletedRunSyncEntity(runId: String)
}
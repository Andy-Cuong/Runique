package com.andyc.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.andyc.core.database.entity.RunEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDao {
    @Upsert
    suspend fun upsertRun(run: RunEntity)

    @Upsert
    suspend fun upsertRuns(runs: List<RunEntity>)

    @Query("SELECT * FROM RunEntity ORDER BY dateTimeUtc DESC")
    fun getRuns(): Flow<List<RunEntity>>

    @Query("DELETE FROM RunEntity WHERE id=:id")
    fun deleteRun(id: String)

    @Query("DELETE FROM RunEntity")
    fun deleteAllRuns()
}
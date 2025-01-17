package com.andyc.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andyc.core.database.dao.AnalyticsDao
import com.andyc.core.database.dao.RunDao
import com.andyc.core.database.dao.RunPendingSyncDao
import com.andyc.core.database.entity.DeletedRunSyncEntity
import com.andyc.core.database.entity.RunEntity
import com.andyc.core.database.entity.RunPendingSyncEntity

@Database(
    entities = [
        RunEntity::class,
        RunPendingSyncEntity::class,
        DeletedRunSyncEntity::class
               ],
    version = 2)
abstract class RunDatabase: RoomDatabase() {
    abstract val runDao: RunDao
    abstract val runPendingSyncDao: RunPendingSyncDao
    abstract val analyticsDao: AnalyticsDao
}
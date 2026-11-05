package com.blocker.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.blocker.core.data.local.dao.AppBlockDao
import com.blocker.core.data.local.dao.AppBulkActionDao
import com.blocker.core.data.local.dao.AppDetectionDao
import com.blocker.core.data.local.dao.AppMonitoringDao
import com.blocker.core.data.local.dao.AppUsageDao
import com.blocker.core.data.local.entity.AppBlockEntity
import com.blocker.core.data.local.entity.AppBulkActionEntity
import com.blocker.core.data.local.entity.AppDetectionEntity
import com.blocker.core.data.local.entity.AppMonitoringEntity
import com.blocker.core.data.local.entity.AppUsageEntity

@Database(
    entities = [
        AppMonitoringEntity::class,
        AppUsageEntity::class,
        AppBlockEntity::class,
        AppDetectionEntity::class,
        AppBulkActionEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class BlockerDatabase : RoomDatabase() {

    abstract fun appMonitoringDao(): AppMonitoringDao

    abstract fun appUsageDao(): AppUsageDao

    abstract fun appBlockDao(): AppBlockDao

    abstract fun appDetectionDao(): AppDetectionDao

    abstract fun appBulkActionDao(): AppBulkActionDao

    companion object {
        const val DATABASE_NAME = "blocker_database"
    }
}

package com.blocker.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.blocker.core.data.local.entity.AppMonitoringEntity
import kotlinx.coroutines.flow.Flow

/**
 * دسترسی به جدول تنظیمات نظارت برنامه‌ها.
 */
@Dao
interface AppMonitoringDao {

    /**
     * دریافت تمام تنظیمات نظارت.
     */
    @Query(
        """
        SELECT * FROM app_monitoring
        ORDER BY appName COLLATE NOCASE ASC
        """
    )
    fun observeAll(): Flow<List<AppMonitoringEntity>>

    /**
     * دریافت تنظیمات یک برنامه.
     */
    @Query(
        """
        SELECT * FROM app_monitoring
        WHERE packageName = :packageName
        LIMIT 1
        """
    )
    fun observeByPackage(
        packageName: String
    ): Flow<AppMonitoringEntity?>

    /**
     * دریافت مستقیم تنظیمات یک برنامه.
     */
    @Query(
        """
        SELECT * FROM app_monitoring
        WHERE packageName = :packageName
        LIMIT 1
        """
    )
    suspend fun getByPackage(
        packageName: String
    ): AppMonitoringEntity?

    /**
     * ذخیره یا جایگزینی تنظیمات یک برنامه.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(
        entity: AppMonitoringEntity
    )

    /**
     * ذخیره چند تنظیمات در یک تراکنش.
     */
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(
        entities: List<AppMonitoringEntity>
    )

    /**
     * حذف تنظیمات یک برنامه.
     */
    @Query(
        """
        DELETE FROM app_monitoring
        WHERE packageName = :packageName
        """
    )
    suspend fun deleteByPackage(
        packageName: String
    )

    /**
     * بررسی وجود تنظیمات برای یک برنامه.
     */
    @Query(
        """
        SELECT EXISTS(
            SELECT 1 FROM app_monitoring
            WHERE packageName = :packageName
        )
        """
    )
    suspend fun exists(
        packageName: String
    ): Boolean

    /**
     * دریافت تعداد برنامه‌های ثبت‌شده.
     */
    @Query("SELECT COUNT(*) FROM app_monitoring")
    suspend fun getCount(): Int

    /**
     * حذف تمام تنظیمات نظارت.
     */
    @Query("DELETE FROM app_monitoring")
    suspend fun deleteAll()
}

package com.blocker.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.blocker.core.data.local.entity.AppUsageEntity
import kotlinx.coroutines.flow.Flow

/**
 * دسترسی به جدول آمار استفاده از برنامه‌ها.
 */
@Dao
interface AppUsageDao {

    /**
     * مشاهده تمام آمار برنامه‌ها.
     */
    @Query(
        """
        SELECT * FROM app_usage
        ORDER BY appName COLLATE NOCASE ASC
        """
    )
    fun observeAll(): Flow<List<AppUsageEntity>>

    /**
     * مشاهده آمار یک برنامه.
     */
    @Query(
        """
        SELECT * FROM app_usage
        WHERE packageName = :packageName
        LIMIT 1
        """
    )
    fun observeByPackage(
        packageName: String
    ): Flow<AppUsageEntity?>

    /**
     * دریافت مستقیم آمار یک برنامه.
     */
    @Query(
        """
        SELECT * FROM app_usage
        WHERE packageName = :packageName
        LIMIT 1
        """
    )
    suspend fun getByPackage(
        packageName: String
    ): AppUsageEntity?

    /**
     * ذخیره یا جایگزینی آمار یک برنامه.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(
        entity: AppUsageEntity
    )

    /**
     * ذخیره آمار چند برنامه.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(
        entities: List<AppUsageEntity>
    )

    /**
     * دریافت برنامه‌ها بر اساس بیشترین تعداد مسدودسازی.
     */
    @Query(
        """
        SELECT * FROM app_usage
        ORDER BY blockCount DESC, appName ASC
        """
    )
    fun observeMostBlocked(): Flow<List<AppUsageEntity>>

    /**
     * دریافت برنامه‌ها بر اساس بیشترین تعداد شناسایی.
     */
    @Query(
        """
        SELECT * FROM app_usage
        ORDER BY
            (wordDetectionCount + domainDetectionCount) DESC,
            appName ASC
        """
    )
    fun observeMostDetected(): Flow<List<AppUsageEntity>>

    /**
     * افزایش تعداد شناسایی کلمات.
     */
    @Query(
        """
        UPDATE app_usage
        SET wordDetectionCount = wordDetectionCount + 1,
            updatedAt = :updatedAt
        WHERE packageName = :packageName
        """
    )
    suspend fun incrementWordDetection(
        packageName: String,
        updatedAt: Long = System.currentTimeMillis()
    )

    /**
     * افزایش تعداد شناسایی دامنه‌ها.
     */
    @Query(
        """
        UPDATE app_usage
        SET domainDetectionCount = domainDetectionCount + 1,
            updatedAt = :updatedAt
        WHERE packageName = :packageName
        """
    )
    suspend fun incrementDomainDetection(
        packageName: String,
        updatedAt: Long = System.currentTimeMillis()
    )

    /**
     * افزایش تعداد مسدودسازی‌ها.
     */
    @Query(
        """
        UPDATE app_usage
        SET blockCount = blockCount + 1,
            lastBlockedAt = :blockedAt,
            updatedAt = :blockedAt
        WHERE packageName = :packageName
        """
    )
    suspend fun incrementBlockCount(
        packageName: String,
        blockedAt: Long = System.currentTimeMillis()
    )

    /**
     * حذف آمار یک برنامه.
     */
    @Query(
        """
        DELETE FROM app_usage
        WHERE packageName = :packageName
        """
    )
    suspend fun deleteByPackage(
        packageName: String
    )

    /**
     * تعداد برنامه‌های دارای آمار.
     */
    @Query("SELECT COUNT(*) FROM app_usage")
    suspend fun getCount(): Int

    /**
     * حذف تمام آمار.
     */
    @Query("DELETE FROM app_usage")
    suspend fun deleteAll()
}

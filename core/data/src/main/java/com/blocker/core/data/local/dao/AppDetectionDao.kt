package com.blocker.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.blocker.core.data.local.entity.AppDetectionEntity

@Dao
interface AppDetectionDao {

    /**
     * مشاهده تمام رویدادهای تشخیص.
     */
    @Query(
        """
        SELECT * FROM app_detections
        ORDER BY detectedAt DESC
        """
    )
    fun observeAll(): Flow<List<AppDetectionEntity>>

    /**
     * مشاهده رویدادهای مربوط به یک برنامه.
     */
    @Query(
        """
        SELECT * FROM app_detections
        WHERE packageName = :packageName
        ORDER BY detectedAt DESC
        """
    )
    fun observeByPackage(
        packageName: String
    ): Flow<List<AppDetectionEntity>>

    /**
     * مشاهده رویدادهای مربوط به یک واژه.
     */
    @Query(
        """
        SELECT * FROM app_detections
        WHERE matchedValue = :matchedValue
        ORDER BY detectedAt DESC
        """
    )
    fun observeByMatchedValue(
        matchedValue: String
    ): Flow<List<AppDetectionEntity>>

    /**
     * دریافت آخرین رویدادهای تشخیص.
     */
    @Query(
        """
        SELECT * FROM app_detections
        ORDER BY detectedAt DESC
        LIMIT :limit
        """
    )
    suspend fun getRecent(
        limit: Int
    ): List<AppDetectionEntity>

    /**
     * ثبت یک رویداد تشخیص.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(
        entity: AppDetectionEntity
    )

    /**
     * ثبت چند رویداد تشخیص.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(
        entities: List<AppDetectionEntity>
    )

    /**
     * تعداد کل تشخیص‌ها.
     */
    @Query(
        """
        SELECT COUNT(*) FROM app_detections
        """
    )
    suspend fun getTotalCount(): Int

    /**
     * تعداد تشخیص‌های یک برنامه.
     */
    @Query(
        """
        SELECT COUNT(*) FROM app_detections
        WHERE packageName = :packageName
        """
    )
    suspend fun getCountByPackage(
        packageName: String
    ): Int

    /**
     * تعداد دفعات تشخیص یک واژه.
     */
    @Query(
        """
        SELECT COUNT(*) FROM app_detections
        WHERE matchedValue = :matchedValue
        """
    )
    suspend fun getCountByMatchedValue(
        matchedValue: String
    ): Int

    /**
     * تعداد تشخیص‌ها در یک بازه زمانی.
     */
    @Query(
        """
        SELECT COUNT(*) FROM app_detections
        WHERE detectedAt >= :startTime
          AND detectedAt <= :endTime
        """
    )
    suspend fun getCountBetween(
        startTime: Long,
        endTime: Long
    ): Int

    /**
     * دریافت آخرین زمان تشخیص یک واژه.
     */
    @Query(
        """
        SELECT MAX(detectedAt) FROM app_detections
        WHERE matchedValue = :matchedValue
        """
    )
    suspend fun getLastDetectionTime(
        matchedValue: String
    ): Long?

    /**
     * دریافت تعداد تشخیص‌ها به تفکیک واژه.
     */
    @Query(
        """
        SELECT matchedValue, COUNT(*) AS detectionCount
        FROM app_detections
        GROUP BY matchedValue
        ORDER BY detectionCount DESC
        """
    )
    suspend fun getDetectionCountsByWord(): List<WordDetectionCount>

    /**
     * حذف رویدادهای قدیمی‌تر از زمان مشخص.
     */
    @Query(
        """
        DELETE FROM app_detections
        WHERE detectedAt < :timestamp
        """
    )
    suspend fun deleteBefore(
        timestamp: Long
    )

    /**
     * حذف تاریخچه مربوط به یک برنامه.
     */
    @Query(
        """
        DELETE FROM app_detections
        WHERE packageName = :packageName
        """
    )
    suspend fun deleteByPackage(
        packageName: String
    )

    /**
     * حذف تمام تاریخچه تشخیص.
     */
    @Query(
        """
        DELETE FROM app_detections
        """
    )
    suspend fun deleteAll()
}

/**
 * نتیجه شمارش تشخیص هر واژه.
 */
data class WordDetectionCount(
    val matchedValue: String,
    val detectionCount: Int
)

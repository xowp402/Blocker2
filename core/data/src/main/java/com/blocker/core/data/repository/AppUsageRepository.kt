package com.blocker.core.data.repository

import com.blocker.core.common.AppUsageInfo
import kotlinx.coroutines.flow.Flow

/**
 * قرارداد مدیریت آمار استفاده از برنامه‌ها.
 */
interface AppUsageRepository {

    /**
     * مشاهده آمار تمام برنامه‌ها.
     */
    fun observeAll(): Flow<List<AppUsageInfo>>

    /**
     * مشاهده آمار یک برنامه.
     */
    fun observeByPackage(
        packageName: String
    ): Flow<AppUsageInfo?>

    /**
     * دریافت اطلاعات یک برنامه.
     */
    suspend fun getByPackage(
        packageName: String
    ): AppUsageInfo?

    /**
     * ذخیره یا به‌روزرسانی آمار یک برنامه.
     */
    suspend fun save(
        usageInfo: AppUsageInfo
    )

    /**
     * ذخیره آمار چند برنامه.
     */
    suspend fun saveAll(
        usageInfos: List<AppUsageInfo>
    )

    /**
     * افزایش زمان استفاده از برنامه.
     */
    suspend fun addUsageTime(
        packageName: String,
        durationMillis: Long
    )

    /**
     * ثبت زمان آخرین استفاده.
     */
    suspend fun updateLastUsed(
        packageName: String,
        timestamp: Long
    )

    /**
     * ثبت یک تشخیص کلمه.
     */
    suspend fun incrementWordDetection(
        packageName: String
    )

    /**
     * ثبت یک تشخیص دامنه.
     */
    suspend fun incrementDomainDetection(
        packageName: String
    )

    /**
     * ثبت یک مسدودسازی.
     */
    suspend fun incrementBlockCount(
        packageName: String,
        blockedAt: Long
    )

    /**
     * حذف آمار یک برنامه.
     */
    suspend fun delete(
        packageName: String
    )

    /**
     * حذف تمام آمار.
     */
    suspend fun deleteAll()
}

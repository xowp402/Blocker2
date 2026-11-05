package com.blocker.core.data.repository

import com.blocker.core.common.AppMonitoringConfig
import com.blocker.core.common.AppUsageInfo
import kotlinx.coroutines.flow.Flow

/**
 * رابط مدیریت اطلاعات نظارت روی برنامه‌ها.
 */
interface AppMonitoringRepository {

    /**
     * دریافت تمام تنظیمات نظارت.
     */
    fun observeAllConfigs(): Flow<List<AppMonitoringConfig>>

    /**
     * دریافت تنظیمات یک برنامه.
     */
    fun observeConfig(
        packageName: String
    ): Flow<AppMonitoringConfig?>

    /**
     * دریافت مستقیم تنظیمات یک برنامه.
     */
    suspend fun getConfig(
        packageName: String
    ): AppMonitoringConfig?

    /**
     * ذخیره یا به‌روزرسانی تنظیمات برنامه.
     */
    suspend fun saveConfig(
        config: AppMonitoringConfig
    )

    /**
     * ذخیره تنظیمات چند برنامه.
     */
    suspend fun saveConfigs(
        configs: List<AppMonitoringConfig>
    )

    /**
     * حذف تنظیمات اختصاصی یک برنامه.
     */
    suspend fun deleteConfig(
        packageName: String
    )

    /**
     * دریافت تمام آمار استفاده از برنامه‌ها.
     */
    fun observeAllUsage(): Flow<List<AppUsageInfo>>

    /**
     * دریافت آمار یک برنامه.
     */
    fun observeUsage(
        packageName: String
    ): Flow<AppUsageInfo?>

    /**
     * دریافت مستقیم آمار یک برنامه.
     */
    suspend fun getUsage(
        packageName: String
    ): AppUsageInfo?

    /**
     * ذخیره یا به‌روزرسانی آمار استفاده.
     */
    suspend fun saveUsage(
        usageInfo: AppUsageInfo
    )

    /**
     * حذف آمار یک برنامه.
     */
    suspend fun deleteUsage(
        packageName: String
    )

    /**
     * حذف تمام آمار استفاده.
     */
    suspend fun clearAllUsage()
}

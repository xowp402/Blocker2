package com.blocker.core.common

import kotlinx.serialization.Serializable

/**
 * اطلاعات آماری استفاده از یک برنامه.
 */
@Serializable
data class AppUsageInfo(

    /**
     * شناسه بسته برنامه.
     */
    val packageName: String,

    /**
     * نام نمایشی برنامه.
     */
    val appName: String,

    /**
     * زمان آخرین استفاده ثبت‌شده.
     */
    val lastUsedAt: Long? = null,

    /**
     * مجموع مدت استفاده بر حسب میلی‌ثانیه.
     */
    val totalUsageMillis: Long = 0L,

    /**
     * تعداد دفعات شناسایی کلمات ممنوعه.
     */
    val wordDetectionCount: Int = 0,

    /**
     * تعداد دفعات شناسایی دامنه‌های ممنوعه.
     */
    val domainDetectionCount: Int = 0,

    /**
     * تعداد دفعات مسدودسازی برنامه.
     */
    val blockCount: Int = 0,

    /**
     * آخرین زمان مسدودسازی.
     */
    val lastBlockedAt: Long? = null,

    /**
     * زمان آخرین به‌روزرسانی اطلاعات.
     */
    val updatedAt: Long = System.currentTimeMillis()
) {

    /**
     * مجموع تمام شناسایی‌ها.
     */
    val totalDetectionCount: Int
        get() = wordDetectionCount + domainDetectionCount

    /**
     * بررسی وجود سابقه استفاده.
     */
    fun hasUsageHistory(): Boolean {
        return lastUsedAt != null || totalUsageMillis > 0L
    }

    /**
     * بررسی وجود سابقه مسدودسازی.
     */
    fun hasBlockingHistory(): Boolean {
        return blockCount > 0
    }

    /**
     * افزایش شمارنده شناسایی کلمه.
     */
    fun recordWordDetection(): AppUsageInfo {
        return copy(
            wordDetectionCount = wordDetectionCount + 1,
            updatedAt = System.currentTimeMillis()
        )
    }

    /**
     * افزایش شمارنده شناسایی دامنه.
     */
    fun recordDomainDetection(): AppUsageInfo {
        return copy(
            domainDetectionCount = domainDetectionCount + 1,
            updatedAt = System.currentTimeMillis()
        )
    }

    /**
     * ثبت مسدودسازی جدید.
     */
    fun recordBlock(): AppUsageInfo {
        val now = System.currentTimeMillis()

        return copy(
            blockCount = blockCount + 1,
            lastBlockedAt = now,
            updatedAt = now
        )
    }

    /**
     * بررسی اعتبار اطلاعات.
     */
    fun isValid(): Boolean {
        return AppPackageValidator.isValid(packageName) &&
            appName.isNotBlank() &&
            totalUsageMillis >= 0L &&
            wordDetectionCount >= 0 &&
            domainDetectionCount >= 0 &&
            blockCount >= 0 &&
            (lastUsedAt == null || lastUsedAt > 0L) &&
            (lastBlockedAt == null || lastBlockedAt > 0L) &&
            updatedAt > 0L
    }

    companion object {

        /**
         * ایجاد اطلاعات اولیه برای یک برنامه.
         */
        fun create(
            packageName: String,
            appName: String
        ): AppUsageInfo {
            return AppUsageInfo(
                packageName = packageName,
                appName = appName
            )
        }
    }
}

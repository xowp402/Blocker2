package com.blocker.core.common

import kotlinx.serialization.Serializable

/**
 * وضعیت یک رکورد مسدودسازی برنامه.
 */
@Serializable
enum class AppBlockRecordStatus {
    ACTIVE,
    EXPIRED,
    CANCELLED,
    FAILED
}

/**
 * اطلاعات یک دوره مسدودسازی برنامه.
 */
@Serializable
data class AppBlockRecord(

    /**
     * شناسه یکتای رکورد.
     */
    val id: String,

    /**
     * شناسه بسته برنامه مسدودشده.
     */
    val packageName: String,

    /**
     * نام نمایشی برنامه.
     */
    val appName: String,

    /**
     * علت مسدودسازی.
     */
    val reason: String,

    /**
     * شناسه قانون ایجادکننده مسدودسازی.
     */
    val ruleId: String? = null,

    /**
     * زمان شروع مسدودسازی.
     */
    val startedAt: Long = System.currentTimeMillis(),

    /**
     * زمان پایان مسدودسازی.
     * مقدار null یعنی مسدودسازی دائمی.
     */
    val expiresAt: Long? = null,

    /**
     * وضعیت فعلی مسدودسازی.
     */
    val status: AppBlockRecordStatus =
        AppBlockRecordStatus.ACTIVE,

    /**
     * زمان پایان واقعی یا لغو مسدودسازی.
     */
    val completedAt: Long? = null,

    /**
     * توضیح اختیاری.
     */
    val description: String = ""
) {

    /**
     * تشخیص مسدودسازی دائمی.
     */
    fun isPermanent(): Boolean {
        return expiresAt == null
    }

    /**
     * بررسی فعال بودن مسدودسازی در زمان مشخص.
     */
    fun isActiveAt(time: Long = System.currentTimeMillis()): Boolean {
        if (status != AppBlockRecordStatus.ACTIVE) {
            return false
        }

        if (time < startedAt) {
            return false
        }

        return expiresAt == null || time < expiresAt
    }

    /**
     * محاسبه زمان باقی‌مانده بر حسب میلی‌ثانیه.
     * برای مسدودسازی دائمی مقدار null برمی‌گرداند.
     */
    fun remainingTimeMillis(
        time: Long = System.currentTimeMillis()
    ): Long? {
        if (!isActiveAt(time)) {
            return 0L
        }

        val end = expiresAt ?: return null

        return (end - time).coerceAtLeast(0L)
    }

    /**
     * بررسی اعتبار رکورد.
     */
    fun isValid(): Boolean {
        return id.isNotBlank() &&
            AppPackageValidator.isValid(packageName) &&
            appName.isNotBlank() &&
            reason.isNotBlank() &&
            startedAt > 0L &&
            (expiresAt == null || expiresAt > startedAt) &&
            (completedAt == null || completedAt >= startedAt) &&
            (ruleId == null || ruleId.isNotBlank()) &&
            description.length <= 1000
    }
}

package com.blocker.core.common

import kotlinx.serialization.Serializable

/**
 * تنظیمات نظارت روی یک برنامه مشخص.
 */
@Serializable
data class AppMonitoringConfig(

    /**
     * شناسه بسته برنامه اندرویدی.
     */
    val packageName: String,

    /**
     * نام نمایشی برنامه.
     */
    val appName: String,

    /**
     * فعال بودن نظارت روی برنامه.
     */
    val isMonitoringEnabled: Boolean = true,

    /**
     * بررسی کلمات و عبارت‌های ممنوعه.
     */
    val wordFilterEnabled: Boolean = true,

    /**
     * بررسی دامنه‌ها و وب‌سایت‌ها.
     */
    val websiteFilterEnabled: Boolean = true,

    /**
     * نمایش هشدار هنگام شناسایی مورد ممنوعه.
     */
    val showWarning: Boolean = true,

    /**
     * اجازه استفاده از قوانین عمومی برای این برنامه.
     */
    val useGlobalRules: Boolean = true,

    /**
     * اولویت قوانین اختصاصی این برنامه.
     */
    val rulePriority: Int = 100,

    /**
     * توضیح اختیاری درباره تنظیمات برنامه.
     */
    val description: String = "",

    /**
     * زمان آخرین تغییر تنظیمات.
     */
    val updatedAt: Long = System.currentTimeMillis()
) {

    /**
     * بررسی معتبر بودن تنظیمات.
     */
    fun isValid(): Boolean {
        return AppPackageValidator.isValid(packageName) &&
            appName.isNotBlank() &&
            rulePriority >= 0 &&
            description.length <= 1000 &&
            updatedAt > 0
    }

    /**
     * بررسی فعال بودن حداقل یکی از فیلترها.
     */
    fun hasActiveFilter(): Boolean {
        return isMonitoringEnabled &&
            (wordFilterEnabled || websiteFilterEnabled)
    }

    /**
     * غیرفعال کردن کامل نظارت روی برنامه.
     */
    fun disableMonitoring(): AppMonitoringConfig {
        return copy(
            isMonitoringEnabled = false,
            updatedAt = System.currentTimeMillis()
        )
    }

    /**
     * فعال کردن نظارت روی برنامه.
     */
    fun enableMonitoring(): AppMonitoringConfig {
        return copy(
            isMonitoringEnabled = true,
            updatedAt = System.currentTimeMillis()
        )
    }

    /**
     * فعال یا غیرفعال کردن فیلتر کلمات.
     */
    fun setWordFilter(enabled: Boolean): AppMonitoringConfig {
        return copy(
            wordFilterEnabled = enabled,
            updatedAt = System.currentTimeMillis()
        )
    }

    /**
     * فعال یا غیرفعال کردن فیلتر وب‌سایت‌ها.
     */
    fun setWebsiteFilter(enabled: Boolean): AppMonitoringConfig {
        return copy(
            websiteFilterEnabled = enabled,
            updatedAt = System.currentTimeMillis()
        )
    }
}

package com.blocker.core.common

import kotlinx.serialization.Serializable

/**
 * گزارش سلامت سرویس‌ها و بخش‌های اصلی برنامه Blocker.
 */
@Serializable
data class ServiceHealthReport(

    /**
     * وضعیت سرویس تشخیص کلمات.
     */
    val wordFilterStatus: FilterStatus = FilterStatus.DISABLED,

    /**
     * وضعیت سرویس فیلتر وب‌سایت.
     */
    val websiteFilterStatus: FilterStatus = FilterStatus.DISABLED,

    /**
     * وضعیت سرویس Accessibility اندروید.
     */
    val accessibilityStatus: FilterStatus = FilterStatus.DISABLED,

    /**
     * وضعیت سرویس VPN محلی.
     */
    val vpnStatus: FilterStatus = FilterStatus.DISABLED,

    /**
     * آیا سرویس نظارت در حال اجرا است؟
     */
    val monitoringServiceRunning: Boolean = false,

    /**
     * آیا سرویس بازیابی خودکار در دسترس است؟
     */
    val recoveryServiceAvailable: Boolean = false,

    /**
     * زمان آخرین بررسی سلامت سرویس‌ها.
     */
    val lastHealthCheckAt: Long? = null,

    /**
     * زمان آخرین قطع شدن سرویس.
     */
    val lastFailureAt: Long? = null,

    /**
     * زمان آخرین بازیابی موفق.
     */
    val lastRecoveryAt: Long? = null,

    /**
     * توضیح آخرین خطای فنی.
     */
    val lastErrorMessage: String? = null,

    /**
     * نام سرویس دارای خطا.
     */
    val failedServiceName: String? = null,

    /**
     * تعداد تلاش‌های ناموفق برای بازیابی.
     */
    val recoveryFailureCount: Int = 0,

    /**
     * آیا نیاز به راهنمایی کاربر وجود دارد؟
     */
    val requiresUserAction: Boolean = false,

    /**
     * زمان آخرین به‌روزرسانی گزارش.
     */
    val updatedAt: Long = System.currentTimeMillis()
) {

    /**
     * آیا فیلتر کلمات آماده فعالیت است؟
     */
    val isWordFilterHealthy: Boolean
        get() = wordFilterStatus == FilterStatus.ACTIVE &&
            accessibilityStatus == FilterStatus.ACTIVE

    /**
     * آیا فیلتر وب‌سایت آماده فعالیت است؟
     */
    val isWebsiteFilterHealthy: Boolean
        get() = websiteFilterStatus == FilterStatus.ACTIVE &&
            vpnStatus == FilterStatus.ACTIVE

    /**
     * آیا حداقل یکی از سرویس‌های اصلی مشکل دارد؟
     */
    val hasServiceFailure: Boolean
        get() = wordFilterStatus == FilterStatus.ERROR ||
            websiteFilterStatus == FilterStatus.ERROR ||
            accessibilityStatus == FilterStatus.ERROR ||
            vpnStatus == FilterStatus.ERROR

    /**
     * آیا تمام سرویس‌های موردنیاز فعال هستند؟
     */
    val areAllServicesHealthy: Boolean
        get() = isWordFilterHealthy &&
            isWebsiteFilterHealthy &&
            monitoringServiceRunning

    /**
     * بررسی اعتبار گزارش.
     */
    fun isValid(): Boolean {
        return recoveryFailureCount >= 0 &&
            (lastHealthCheckAt == null || lastHealthCheckAt > 0) &&
            (lastFailureAt == null || lastFailureAt > 0) &&
            (lastRecoveryAt == null || lastRecoveryAt > 0) &&
            updatedAt > 0
    }

    companion object {

        /**
         * گزارش اولیه با سرویس‌های غیرفعال.
         */
        fun initial(): ServiceHealthReport {
            return ServiceHealthReport()
        }
    }
}

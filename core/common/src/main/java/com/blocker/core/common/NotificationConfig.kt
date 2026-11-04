package com.blocker.core.common

import kotlinx.serialization.Serializable

/**
 * تنظیمات اعلان‌های برنامه Blocker.
 */
@Serializable
data class NotificationConfig(

    /**
     * فعال بودن کلی اعلان‌ها.
     */
    val notificationsEnabled: Boolean = true,

    /**
     * اعلان هنگام تشخیص کلمه.
     */
    val wordDetectionNotifications: Boolean = true,

    /**
     * اعلان هنگام مسدود شدن یک برنامه.
     */
    val appBlockNotifications: Boolean = true,

    /**
     * اعلان هنگام مسدود شدن یک وب‌سایت.
     */
    val websiteBlockNotifications: Boolean = true,

    /**
     * اعلان هنگام پایان زمان مسدودسازی.
     */
    val blockExpirationNotifications: Boolean = true,

    /**
     * اعلان هنگام غیرفعال شدن فیلتر کلمات.
     */
    val wordFilterFailureNotifications: Boolean = true,

    /**
     * اعلان هنگام غیرفعال شدن فیلتر وب‌سایت.
     */
    val websiteFilterFailureNotifications: Boolean = true,

    /**
     * اعلان هنگام بازیابی موفق سرویس‌ها.
     */
    val serviceRecoveryNotifications: Boolean = true,

    /**
     * اعلان گزارش روزانه.
     */
    val dailySummaryNotifications: Boolean = false,

    /**
     * ساعت ارسال گزارش روزانه.
     */
    val dailySummaryHour: Int = 21,

    /**
     * دقیقه ارسال گزارش روزانه.
     */
    val dailySummaryMinute: Int = 0,

    /**
     * نمایش نام کلمه تشخیص داده‌شده در اعلان.
     */
    val showDetectedWord: Boolean = true,

    /**
     * نمایش نام برنامه در اعلان.
     */
    val showAppName: Boolean = true,

    /**
     * نمایش مدت زمان باقی‌مانده.
     */
    val showRemainingTime: Boolean = true,

    /**
     * زمان آخرین ویرایش تنظیمات.
     */
    val updatedAt: Long = System.currentTimeMillis()
) {

    /**
     * بررسی معتبر بودن تنظیمات.
     */
    fun isValid(): Boolean {
        return dailySummaryHour in 0..23 &&
            dailySummaryMinute in 0..59 &&
            updatedAt > 0
    }

    /**
     * بررسی فعال بودن اعلان‌های مربوط به کلمات.
     */
    val canNotifyWordDetection: Boolean
        get() = notificationsEnabled &&
            wordDetectionNotifications

    /**
     * بررسی فعال بودن اعلان‌های مربوط به برنامه‌ها.
     */
    val canNotifyAppBlock: Boolean
        get() = notificationsEnabled &&
            appBlockNotifications

    /**
     * بررسی فعال بودن اعلان‌های مربوط به وب‌سایت‌ها.
     */
    val canNotifyWebsiteBlock: Boolean
        get() = notificationsEnabled &&
            websiteBlockNotifications

    /**
     * بررسی فعال بودن اعلان‌های پایان مسدودسازی.
     */
    val canNotifyExpiration: Boolean
        get() = notificationsEnabled &&
            blockExpirationNotifications

    companion object {

        /**
         * تنظیمات پیش‌فرض اعلان‌ها.
         */
        fun default(): NotificationConfig {
            return NotificationConfig()
        }

        /**
         * غیرفعال کردن تمام اعلان‌ها.
         */
        fun disabled(): NotificationConfig {
            return NotificationConfig(
                notificationsEnabled = false
            )
        }
    }
}

package com.blocker.core.common

import kotlinx.serialization.Serializable

/**
 * آمار کلی قابل نمایش در داشبورد Blocker.
 */
@Serializable
data class DashboardStats(

    /**
     * تعداد کل کلمات و عبارت‌های ثبت‌شده.
     */
    val totalWords: Int = 0,

    /**
     * تعداد کلمات فعال.
     */
    val activeWords: Int = 0,

    /**
     * تعداد کل دامنه‌های ثبت‌شده.
     */
    val totalDomains: Int = 0,

    /**
     * تعداد دامنه‌های فعال.
     */
    val activeDomains: Int = 0,

    /**
     * تعداد برنامه‌های نصب‌شده تحت نظارت.
     */
    val monitoredApps: Int = 0,

    /**
     * تعداد برنامه‌هایی که در حال حاضر مسدود هستند.
     */
    val currentlyBlockedApps: Int = 0,

    /**
     * تعداد کل دفعات تشخیص کلمات.
     */
    val totalWordDetections: Long = 0L,

    /**
     * تعداد دفعات مسدودسازی وب‌سایت‌ها.
     */
    val totalWebsiteBlocks: Long = 0L,

    /**
     * تعداد مسدودسازی‌های انجام‌شده امروز.
     */
    val todayBlockCount: Long = 0L,

    /**
     * تعداد مسدودسازی‌های انجام‌شده در هفت روز اخیر.
     */
    val weeklyBlockCount: Long = 0L,

    /**
     * زمان آخرین فعالیت ثبت‌شده.
     */
    val lastActivityAt: Long? = null,

    /**
     * وضعیت کلی فیلتر کلمات.
     */
    val wordFilterStatus: FilterStatus = FilterStatus.DISABLED,

    /**
     * وضعیت کلی فیلتر وب‌سایت.
     */
    val websiteFilterStatus: FilterStatus = FilterStatus.DISABLED,

    /**
     * زمان آخرین به‌روزرسانی آمار.
     */
    val updatedAt: Long = System.currentTimeMillis()
) {

    /**
     * مجموع تعداد قوانین ثبت‌شده.
     */
    val totalRules: Int
        get() = totalWords + totalDomains

    /**
     * مجموع دفعات مسدودسازی.
     */
    val totalBlocks: Long
        get() = totalWordDetections + totalWebsiteBlocks

    /**
     * آیا حداقل یک برنامه در حال حاضر مسدود است؟
     */
    val hasBlockedApps: Boolean
        get() = currentlyBlockedApps > 0

    /**
     * آیا حداقل یکی از فیلترها فعال است؟
     */
    val hasActiveFilter: Boolean
        get() = wordFilterStatus == FilterStatus.ACTIVE ||
            websiteFilterStatus == FilterStatus.ACTIVE

    /**
     * بررسی معتبر بودن آمار.
     */
    fun isValid(): Boolean {
        return totalWords >= 0 &&
            activeWords >= 0 &&
            activeWords <= totalWords &&
            totalDomains >= 0 &&
            activeDomains >= 0 &&
            activeDomains <= totalDomains &&
            monitoredApps >= 0 &&
            currentlyBlockedApps >= 0 &&
            totalWordDetections >= 0 &&
            totalWebsiteBlocks >= 0 &&
            todayBlockCount >= 0 &&
            weeklyBlockCount >= 0 &&
            updatedAt > 0
    }

    companion object {

        /**
         * آمار اولیه و خالی داشبورد.
         */
        fun empty(): DashboardStats {
            return DashboardStats()
        }
    }
}

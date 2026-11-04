package com.blocker.core.common

import kotlinx.serialization.Serializable

/**
 * فیلترهای قابل استفاده برای جست‌وجو در تاریخچه Blocker.
 */
@Serializable
data class HistoryFilter(

    /**
     * نوع رویداد موردنظر.
     *
     * مقدار null یعنی نمایش تمام انواع رویداد.
     */
    val filterType: FilterType? = null,

    /**
     * شناسه بسته برنامه موردنظر.
     *
     * مقدار null یعنی تمام برنامه‌ها.
     */
    val packageName: String? = null,

    /**
     * جست‌وجو در نام کلمه، عبارت یا دامنه.
     */
    val searchQuery: String? = null,

    /**
     * زمان شروع بازه جست‌وجو بر حسب میلی‌ثانیه.
     */
    val startTime: Long? = null,

    /**
     * زمان پایان بازه جست‌وجو بر حسب میلی‌ثانیه.
     */
    val endTime: Long? = null,

    /**
     * وضعیت مسدودسازی موردنظر.
     */
    val blockStatus: BlockStatus? = null,

    /**
     * مرتب‌سازی جدیدترین یا قدیمی‌ترین رویدادها.
     */
    val newestFirst: Boolean = true,

    /**
     * تعداد نتایج در هر صفحه.
     */
    val pageSize: Int = DEFAULT_PAGE_SIZE,

    /**
     * شماره صفحه، با شروع از صفر.
     */
    val page: Int = 0
) {

    /**
     * بررسی معتبر بودن فیلتر.
     */
    fun isValid(): Boolean {
        return (startTime == null || startTime >= 0) &&
            (endTime == null || endTime >= 0) &&
            (startTime == null ||
                endTime == null ||
                startTime <= endTime) &&
            pageSize in 1..MAX_PAGE_SIZE &&
            page >= 0 &&
            (packageName == null || packageName.isNotBlank())
    }

    /**
     * آیا جست‌وجوی متنی فعال است؟
     */
    val hasSearchQuery: Boolean
        get() = !searchQuery.isNullOrBlank()

    /**
     * آیا بازه زمانی مشخص شده است؟
     */
    val hasTimeRange: Boolean
        get() = startTime != null || endTime != null

    /**
     * ایجاد فیلتر برای نمایش تمام تاریخچه.
     */
    companion object {

        const val DEFAULT_PAGE_SIZE = 30

        const val MAX_PAGE_SIZE = 200

        fun all(): HistoryFilter {
            return HistoryFilter()
        }

        /**
         * فیلتر تاریخچه یک برنامه مشخص.
         */
        fun forPackage(packageName: String): HistoryFilter {
            return HistoryFilter(
                packageName = packageName
            )
        }

        /**
         * فیلتر تاریخچه یک نوع مشخص.
         */
        fun forType(type: FilterType): HistoryFilter {
            return HistoryFilter(
                filterType = type
            )
        }
    }
}

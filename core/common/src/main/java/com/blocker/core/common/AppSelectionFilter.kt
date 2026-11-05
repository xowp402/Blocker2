package com.blocker.core.common

import kotlinx.serialization.Serializable

/**
 * وضعیت نظارت روی برنامه.
 */
@Serializable
enum class AppMonitoringFilter {
    ALL,
    MONITORED,
    NOT_MONITORED,
    BLOCKED
}

/**
 * روش مرتب‌سازی فهرست برنامه‌ها.
 */
@Serializable
enum class AppSortOrder {
    NAME_ASCENDING,
    NAME_DESCENDING,
    CATEGORY,
    MOST_RECENTLY_USED,
    MOST_BLOCKED,
    MONITORED_FIRST
}

/**
 * تنظیمات جست‌وجو و فیلتر برنامه‌های نصب‌شده.
 */
@Serializable
data class AppSelectionFilter(

    /**
     * عبارت جست‌وجو در نام برنامه یا شناسه بسته.
     */
    val searchQuery: String = "",

    /**
     * دسته‌بندی انتخاب‌شده.
     * مقدار null یعنی نمایش تمام دسته‌بندی‌ها.
     */
    val category: AppCategory? = null,

    /**
     * فیلتر وضعیت نظارت.
     */
    val monitoringFilter: AppMonitoringFilter =
        AppMonitoringFilter.ALL,

    /**
     * ترتیب نمایش برنامه‌ها.
     */
    val sortOrder: AppSortOrder =
        AppSortOrder.NAME_ASCENDING,

    /**
     * نمایش برنامه‌های سیستمی.
     */
    val includeSystemApps: Boolean = false,

    /**
     * نمایش برنامه‌های غیرفعال.
     */
    val includeDisabledApps: Boolean = false,

    /**
     * فقط برنامه‌هایی که قانون اختصاصی دارند.
     */
    val onlyAppsWithCustomRules: Boolean = false
) {

    /**
     * بررسی خالی بودن فیلترها.
     */
    fun isDefault(): Boolean {
        return searchQuery.isBlank() &&
            category == null &&
            monitoringFilter == AppMonitoringFilter.ALL &&
            sortOrder == AppSortOrder.NAME_ASCENDING &&
            !includeSystemApps &&
            !includeDisabledApps &&
            !onlyAppsWithCustomRules
    }

    /**
     * پاک کردن فیلترها و بازگشت به حالت پیش‌فرض.
     */
    fun reset(): AppSelectionFilter {
        return AppSelectionFilter()
    }

    /**
     * بررسی معتبر بودن تنظیمات.
     */
    fun isValid(): Boolean {
        return searchQuery.length <= 200
    }
}

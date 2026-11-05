package com.blocker.core.common

import kotlinx.serialization.Serializable

/**
 * دسته‌بندی برنامه‌های نصب‌شده در اندروید.
 */
@Serializable
enum class AppCategory {
    SOCIAL,
    GAMES,
    BROWSERS,
    VIDEO,
    MESSAGING,
    EDUCATION,
    PRODUCTIVITY,
    ENTERTAINMENT,
    SHOPPING,
    FINANCE,
    SYSTEM,
    OTHER
}

/**
 * اطلاعات دسته‌بندی یک برنامه.
 */
@Serializable
data class AppCategoryInfo(

    /**
     * شناسه دسته‌بندی.
     */
    val category: AppCategory,

    /**
     * عنوان نمایشی دسته‌بندی.
     */
    val displayName: String,

    /**
     * توضیحات دسته‌بندی.
     */
    val description: String,

    /**
     * تعداد برنامه‌های موجود در این دسته.
     */
    val appCount: Int = 0,

    /**
     * آیا دسته‌بندی برای نمایش فعال است؟
     */
    val isVisible: Boolean = true,

    /**
     * ترتیب نمایش دسته‌بندی.
     */
    val displayOrder: Int = 0
) {

    /**
     * بررسی اعتبار اطلاعات دسته‌بندی.
     */
    fun isValid(): Boolean {
        return displayName.isNotBlank() &&
            description.isNotBlank() &&
            appCount >= 0 &&
            displayOrder >= 0
    }

    companion object {

        /**
         * دسته‌بندی‌های پیش‌فرض برنامه.
         */
        fun defaultCategories(): List<AppCategoryInfo> {
            return listOf(
                AppCategoryInfo(
                    AppCategory.SOCIAL,
                    "شبکه‌های اجتماعی",
                    "برنامه‌های شبکه اجتماعی",
                    displayOrder = 0
                ),
                AppCategoryInfo(
                    AppCategory.GAMES,
                    "بازی‌ها",
                    "بازی‌های نصب‌شده",
                    displayOrder = 1
                ),
                AppCategoryInfo(
                    AppCategory.BROWSERS,
                    "مرورگرها",
                    "برنامه‌های مرور وب",
                    displayOrder = 2
                ),
                AppCategoryInfo(
                    AppCategory.VIDEO,
                    "ویدئو",
                    "برنامه‌های تماشای ویدئو",
                    displayOrder = 3
                ),
                AppCategoryInfo(
                    AppCategory.MESSAGING,
                    "پیام‌رسان‌ها",
                    "برنامه‌های ارتباطی و پیام‌رسان",
                    displayOrder = 4
                ),
                AppCategoryInfo(
                    AppCategory.EDUCATION,
                    "آموزشی",
                    "برنامه‌های آموزشی و یادگیری",
                    displayOrder = 5
                ),
                AppCategoryInfo(
                    AppCategory.PRODUCTIVITY,
                    "بهره‌وری",
                    "ابزارهای کار و مدیریت زمان",
                    displayOrder = 6
                ),
                AppCategoryInfo(
                    AppCategory.ENTERTAINMENT,
                    "سرگرمی",
                    "برنامه‌های سرگرمی",
                    displayOrder = 7
                ),
                AppCategoryInfo(
                    AppCategory.SHOPPING,
                    "خرید",
                    "فروشگاه‌ها و برنامه‌های خرید",
                    displayOrder = 8
                ),
                AppCategoryInfo(
                    AppCategory.FINANCE,
                    "مالی",
                    "برنامه‌های مالی و بانکی",
                    displayOrder = 9
                ),
                AppCategoryInfo(
                    AppCategory.SYSTEM,
                    "سیستمی",
                    "برنامه‌های سیستمی اندروید",
                    displayOrder = 10
                ),
                AppCategoryInfo(
                    AppCategory.OTHER,
                    "سایر",
                    "برنامه‌های دسته‌بندی‌نشده",
                    displayOrder = 11
                )
            )
        }
    }
}

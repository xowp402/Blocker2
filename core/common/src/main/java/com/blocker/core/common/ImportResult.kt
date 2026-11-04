package com.blocker.core.common

import kotlinx.serialization.Serializable

/**
 * نتیجه عملیات ورود اطلاعات به برنامه.
 *
 * برای گزارش موفقیت، موارد تکراری و خطاهای هنگام واردسازی
 * کلمات، دامنه‌ها و اطلاعات پشتیبان استفاده می‌شود.
 */
@Serializable
data class ImportResult(

    /**
     * تعداد کل موارد بررسی‌شده.
     */
    val totalItems: Int,

    /**
     * تعداد مواردی که با موفقیت وارد شدند.
     */
    val importedItems: Int,

    /**
     * تعداد موارد تکراری که وارد نشدند.
     */
    val duplicateItems: Int,

    /**
     * تعداد موارد نامعتبر.
     */
    val invalidItems: Int,

    /**
     * تعداد مواردی که به دلیل خطا وارد نشدند.
     */
    val failedItems: Int,

    /**
     * پیام‌های خطا یا توضیحات مربوط به واردسازی.
     */
    val errors: List<String> = emptyList(),

    /**
     * زمان انجام عملیات بر اساس Unix timestamp میلی‌ثانیه.
     */
    val completedAt: Long = System.currentTimeMillis()
) {

    /**
     * آیا تمام موارد با موفقیت وارد شده‌اند؟
     */
    val isFullySuccessful: Boolean
        get() = totalItems > 0 &&
            importedItems == totalItems &&
            duplicateItems == 0 &&
            invalidItems == 0 &&
            failedItems == 0

    /**
     * آیا حداقل یک مورد با موفقیت وارد شده است؟
     */
    val hasSuccessfulImports: Boolean
        get() = importedItems > 0

    /**
     * تعداد مواردی که وارد نشده‌اند.
     */
    val skippedItems: Int
        get() = duplicateItems + invalidItems + failedItems

    /**
     * درصد موفقیت عملیات واردسازی.
     */
    val successPercentage: Int
        get() {
            if (totalItems <= 0) return 0

            return ((importedItems.toDouble() / totalItems) * 100)
                .toInt()
                .coerceIn(0, 100)
        }

    companion object {

        /**
         * ایجاد نتیجه خالی برای زمانی که هیچ موردی بررسی نشده است.
         */
        fun empty(): ImportResult {
            return ImportResult(
                totalItems = 0,
                importedItems = 0,
                duplicateItems = 0,
                invalidItems = 0,
                failedItems = 0
            )
        }
    }
}

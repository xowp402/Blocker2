package com.blocker.core.common

import kotlinx.serialization.Serializable

/**
 * نوع عملیات گروهی روی برنامه‌ها.
 */
@Serializable
enum class AppBulkActionType {
    ENABLE_MONITORING,
    DISABLE_MONITORING,
    ENABLE_WORD_FILTER,
    DISABLE_WORD_FILTER,
    ENABLE_WEBSITE_FILTER,
    DISABLE_WEBSITE_FILTER,
    ENABLE_ALL_FILTERS,
    DISABLE_ALL_FILTERS,
    REMOVE_FROM_MONITORING,
    CLEAR_CUSTOM_RULES
}

/**
 * وضعیت اجرای عملیات گروهی.
 */
@Serializable
enum class AppBulkActionStatus {
    PENDING,
    RUNNING,
    COMPLETED,
    PARTIALLY_COMPLETED,
    FAILED,
    CANCELLED
}

/**
 * درخواست اجرای یک عملیات گروهی.
 */
@Serializable
data class AppBulkAction(

    /**
     * شناسه یکتای عملیات.
     */
    val id: String,

    /**
     * نوع عملیات موردنظر.
     */
    val actionType: AppBulkActionType,

    /**
     * شناسه بسته برنامه‌های هدف.
     */
    val targetPackages: Set<String>,

    /**
     * وضعیت اجرای عملیات.
     */
    val status: AppBulkActionStatus =
        AppBulkActionStatus.PENDING,

    /**
     * زمان ایجاد درخواست.
     */
    val createdAt: Long = System.currentTimeMillis(),

    /**
     * زمان شروع اجرا.
     */
    val startedAt: Long? = null,

    /**
     * زمان پایان اجرا.
     */
    val completedAt: Long? = null,

    /**
     * تعداد برنامه‌های پردازش‌شده.
     */
    val processedCount: Int = 0,

    /**
     * تعداد برنامه‌هایی که با موفقیت پردازش شده‌اند.
     */
    val successCount: Int = 0,

    /**
     * تعداد برنامه‌هایی که پردازش آن‌ها ناموفق بوده است.
     */
    val failedCount: Int = 0,

    /**
     * توضیح خطا یا نتیجه عملیات.
     */
    val message: String? = null
) {

    /**
     * تعداد کل برنامه‌های هدف.
     */
    val totalCount: Int
        get() = targetPackages.size

    /**
     * درصد پیشرفت عملیات.
     */
    val progressPercent: Int
        get() {
            if (totalCount == 0) return 0

            return (
                processedCount.toDouble() /
                    totalCount.toDouble() * 100.0
                ).toInt().coerceIn(0, 100)
        }

    /**
     * بررسی امکان شروع عملیات.
     */
    fun canStart(): Boolean {
        return status == AppBulkActionStatus.PENDING &&
            targetPackages.isNotEmpty() &&
            isValid()
    }

    /**
     * بررسی اعتبار درخواست.
     */
    fun isValid(): Boolean {
        return id.isNotBlank() &&
            targetPackages.isNotEmpty() &&
            targetPackages.all {
                AppPackageValidator.isValid(it)
            } &&
            processedCount >= 0 &&
            successCount >= 0 &&
            failedCount >= 0 &&
            processedCount <= totalCount &&
            successCount + failedCount <= processedCount &&
            createdAt > 0L &&
            (startedAt == null || startedAt >= createdAt) &&
            (completedAt == null ||
                (startedAt != null && completedAt >= startedAt)) &&
            (message == null || message.length <= 1000)
    }

    companion object {

        /**
         * ساخت درخواست عملیات گروهی جدید.
         */
        fun create(
            actionType: AppBulkActionType,
            packages: Collection<String>
        ): AppBulkAction {
            return AppBulkAction(
                id = java.util.UUID.randomUUID().toString(),
                actionType = actionType,
                targetPackages = packages
                    .map { it.trim() }
                    .filter {
                        AppPackageValidator.isValid(it)
                    }
                    .toSet()
            )
        }
    }
}

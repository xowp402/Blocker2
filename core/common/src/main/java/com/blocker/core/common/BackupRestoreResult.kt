package com.blocker.core.common

import kotlinx.serialization.Serializable

/**
 * نوع عملیات انجام‌شده روی نسخه پشتیبان.
 */
@Serializable
enum class BackupOperationType {
    CREATE_BACKUP,
    RESTORE_BACKUP,
    VALIDATE_BACKUP
}

/**
 * وضعیت نهایی عملیات پشتیبان‌گیری یا بازیابی.
 */
@Serializable
enum class BackupOperationStatus {
    SUCCESS,
    PARTIAL_SUCCESS,
    FAILED,
    CANCELLED
}

/**
 * نتیجه عملیات پشتیبان‌گیری یا بازیابی اطلاعات Blocker.
 */
@Serializable
data class BackupRestoreResult(

    /**
     * شناسه یکتای عملیات.
     */
    val id: String,

    /**
     * نوع عملیات.
     */
    val operationType: BackupOperationType,

    /**
     * وضعیت نهایی عملیات.
     */
    val status: BackupOperationStatus,

    /**
     * مسیر فایل پشتیبان.
     */
    val backupFilePath: String? = null,

    /**
     * تعداد کلمات پردازش‌شده.
     */
    val processedWords: Int = 0,

    /**
     * تعداد دامنه‌های پردازش‌شده.
     */
    val processedDomains: Int = 0,

    /**
     * تعداد قوانین برنامه‌های پردازش‌شده.
     */
    val processedAppRules: Int = 0,

    /**
     * تعداد تنظیمات پردازش‌شده.
     */
    val processedSettings: Int = 0,

    /**
     * تعداد رویدادهای تاریخچه پردازش‌شده.
     */
    val processedHistoryEntries: Int = 0,

    /**
     * اندازه فایل بر حسب بایت.
     */
    val fileSizeBytes: Long = 0L,

    /**
     * پیام خطا در صورت شکست عملیات.
     */
    val errorMessage: String? = null,

    /**
     * توضیحات تکمیلی.
     */
    val details: List<String> = emptyList(),

    /**
     * زمان شروع عملیات.
     */
    val startedAt: Long = System.currentTimeMillis(),

    /**
     * زمان پایان عملیات.
     */
    val completedAt: Long = System.currentTimeMillis()
) {

    /**
     * آیا عملیات با موفقیت کامل انجام شده است؟
     */
    val isSuccessful: Boolean
        get() = status == BackupOperationStatus.SUCCESS

    /**
     * آیا عملیات به‌صورت جزئی موفق بوده است؟
     */
    val isPartiallySuccessful: Boolean
        get() = status == BackupOperationStatus.PARTIAL_SUCCESS

    /**
     * آیا عملیات شکست خورده است؟
     */
    val hasFailed: Boolean
        get() = status == BackupOperationStatus.FAILED

    /**
     * مجموع موارد پردازش‌شده.
     */
    val totalProcessedItems: Int
        get() = processedWords +
            processedDomains +
            processedAppRules +
            processedSettings +
            processedHistoryEntries

    /**
     * مدت زمان اجرای عملیات بر حسب میلی‌ثانیه.
     */
    val durationMillis: Long
        get() = (completedAt - startedAt).coerceAtLeast(0L)

    /**
     * بررسی معتبر بودن نتیجه.
     */
    fun isValid(): Boolean {
        return id.isNotBlank() &&
            processedWords >= 0 &&
            processedDomains >= 0 &&
            processedAppRules >= 0 &&
            processedSettings >= 0 &&
            processedHistoryEntries >= 0 &&
            fileSizeBytes >= 0 &&
            startedAt > 0 &&
            completedAt >= startedAt
    }

    companion object {

        /**
         * ایجاد نتیجه عملیات ناموفق.
         */
        fun failure(
            id: String,
            operationType: BackupOperationType,
            errorMessage: String
        ): BackupRestoreResult {
            return BackupRestoreResult(
                id = id,
                operationType = operationType,
                status = BackupOperationStatus.FAILED,
                errorMessage = errorMessage
            )
        }

        /**
         * ایجاد نتیجه عملیات لغوشده.
         */
        fun cancelled(
            id: String,
            operationType: BackupOperationType
        ): BackupRestoreResult {
            return BackupRestoreResult(
                id = id,
                operationType = operationType,
                status = BackupOperationStatus.CANCELLED
            )
        }
    }
}

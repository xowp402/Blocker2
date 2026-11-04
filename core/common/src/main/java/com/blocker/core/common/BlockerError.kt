package com.blocker.core.common

import kotlinx.serialization.Serializable

/**
 * دسته‌بندی خطاهای داخلی برنامه.
 */
@Serializable
enum class BlockerErrorType {
    DATABASE,
    WORD_FILTER,
    WEBSITE_FILTER,
    ACCESSIBILITY,
    VPN,
    BACKUP,
    RESTORE,
    NOTIFICATION,
    PERMISSION,
    SERVICE_RECOVERY,
    VALIDATION,
    UNKNOWN
}

/**
 * شدت خطا.
 */
@Serializable
enum class ErrorSeverity {
    INFO,
    WARNING,
    ERROR,
    CRITICAL
}

/**
 * اطلاعات یک خطای ثبت‌شده در برنامه Blocker.
 */
@Serializable
data class BlockerError(

    /**
     * شناسه یکتای خطا.
     */
    val id: String,

    /**
     * نوع خطا.
     */
    val type: BlockerErrorType,

    /**
     * شدت خطا.
     */
    val severity: ErrorSeverity,

    /**
     * عنوان کوتاه خطا.
     */
    val title: String,

    /**
     * توضیحات خطا.
     */
    val message: String,

    /**
     * نام بخشی که خطا در آن رخ داده است.
     */
    val component: String? = null,

    /**
     * کد اختصاصی خطا.
     */
    val errorCode: String? = null,

    /**
     * آیا خطا قابل بازیابی است؟
     */
    val isRecoverable: Boolean = true,

    /**
     * آیا نیاز به دخالت کاربر وجود دارد؟
     */
    val requiresUserAction: Boolean = false,

    /**
     * زمان وقوع خطا.
     */
    val occurredAt: Long = System.currentTimeMillis(),

    /**
     * زمان برطرف شدن خطا.
     */
    val resolvedAt: Long? = null,

    /**
     * تعداد دفعات تکرار خطا.
     */
    val occurrenceCount: Int = 1
) {

    /**
     * آیا خطا هنوز برطرف نشده است؟
     */
    val isUnresolved: Boolean
        get() = resolvedAt == null

    /**
     * آیا خطا بحرانی است؟
     */
    val isCritical: Boolean
        get() = severity == ErrorSeverity.CRITICAL

    /**
     * بررسی اعتبار اطلاعات خطا.
     */
    fun isValid(): Boolean {
        return id.isNotBlank() &&
            title.isNotBlank() &&
            message.isNotBlank() &&
            occurredAt > 0 &&
            (resolvedAt == null || resolvedAt >= occurredAt) &&
            occurrenceCount > 0
    }

    /**
     * علامت‌گذاری خطا به‌عنوان برطرف‌شده.
     */
    fun markResolved(
        currentTimeMillis: Long = System.currentTimeMillis()
    ): BlockerError {
        return copy(
            resolvedAt = currentTimeMillis
        )
    }
}

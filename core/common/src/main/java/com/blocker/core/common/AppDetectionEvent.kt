package com.blocker.core.common

import kotlinx.serialization.Serializable

/**
 * نوع مورد شناسایی‌شده.
 */
@Serializable
enum class DetectionTargetType {
    WORD,
    PHRASE,
    DOMAIN,
    URL,
    IP_ADDRESS
}

/**
 * نتیجه پردازش رویداد شناسایی.
 */
@Serializable
enum class DetectionAction {
    BLOCKED,
    WARNING_ONLY,
    IGNORED,
    ALREADY_BLOCKED,
    FAILED
}

/**
 * رویداد شناسایی یک مورد در برنامه اندرویدی.
 */
@Serializable
data class AppDetectionEvent(

    /**
     * شناسه یکتای رویداد.
     */
    val id: String,

    /**
     * شناسه بسته برنامه‌ای که مورد در آن شناسایی شده است.
     */
    val packageName: String,

    /**
     * نام نمایشی برنامه.
     */
    val appName: String,

    /**
     * نوع مورد شناسایی‌شده.
     */
    val targetType: DetectionTargetType,

    /**
     * کلمه، عبارت یا دامنه شناسایی‌شده.
     */
    val matchedValue: String,

    /**
     * شناسه قانون مرتبط، در صورت وجود.
     */
    val ruleId: String? = null,

    /**
     * نتیجه پردازش رویداد.
     */
    val action: DetectionAction,

    /**
     * مدت مسدودسازی بر حسب ثانیه.
     */
    val blockDurationSeconds: Long = 0L,

    /**
     * زمان ثبت رویداد بر حسب میلی‌ثانیه.
     */
    val detectedAt: Long = System.currentTimeMillis(),

    /**
     * توضیح فنی اختیاری برای خطاها.
     */
    val errorMessage: String? = null
) {

    /**
     * بررسی اعتبار رویداد.
     */
    fun isValid(): Boolean {
        return id.isNotBlank() &&
            AppPackageValidator.isValid(packageName) &&
            appName.isNotBlank() &&
            matchedValue.isNotBlank() &&
            blockDurationSeconds >= 0L &&
            detectedAt > 0L &&
            (ruleId == null || ruleId.isNotBlank()) &&
            (errorMessage == null || errorMessage.length <= 1000)
    }

    /**
     * آیا این رویداد منجر به مسدودسازی شده است؟
     */
    fun causedBlocking(): Boolean {
        return action == DetectionAction.BLOCKED
    }

    /**
     * آیا فقط هشدار نمایش داده شده است؟
     */
    fun isWarningOnly(): Boolean {
        return action == DetectionAction.WARNING_ONLY
    }

    /**
     * آیا پردازش رویداد با خطا مواجه شده است؟
     */
    fun hasFailed(): Boolean {
        return action == DetectionAction.FAILED
    }
}

package com.blocker.core.common

import kotlinx.serialization.Serializable

/**
 * یک شکل نوشتاری جایگزین برای یک کلمه اصلی.
 */
@Serializable
data class WordVariant(

    /**
     * شناسه یکتای شکل جایگزین.
     */
    val id: String,

    /**
     * شناسه کلمه اصلی.
     */
    val wordId: String,

    /**
     * متن شکل جایگزین.
     */
    val value: String,

    /**
     * نسخه نرمال‌شده برای مقایسه.
     */
    val normalizedValue: String,

    /**
     * آیا این شکل جایگزین فعال است؟
     */
    val isEnabled: Boolean = true,

    /**
     * آیا این شکل توسط کاربر ساخته شده است؟
     */
    val isUserDefined: Boolean = true,

    /**
     * تعداد دفعات تشخیص این شکل.
     */
    val detectionCount: Long = 0L,

    /**
     * آخرین زمان تشخیص.
     */
    val lastDetectedAt: Long? = null,

    /**
     * زمان ایجاد.
     */
    val createdAt: Long = System.currentTimeMillis(),

    /**
     * زمان آخرین ویرایش.
     */
    val updatedAt: Long = System.currentTimeMillis()
) {

    /**
     * بررسی معتبر بودن شکل جایگزین.
     */
    fun isValid(): Boolean {
        return id.isNotBlank() &&
            wordId.isNotBlank() &&
            value.isNotBlank() &&
            normalizedValue.isNotBlank() &&
            detectionCount >= 0 &&
            createdAt > 0 &&
            updatedAt > 0
    }

    /**
     * افزایش تعداد دفعات تشخیص.
     */
    fun recordDetection(
        currentTimeMillis: Long = System.currentTimeMillis()
    ): WordVariant {
        return copy(
            detectionCount = detectionCount + 1,
            lastDetectedAt = currentTimeMillis,
            updatedAt = currentTimeMillis
        )
    }
}

package com.blocker.core.common

import kotlinx.serialization.Serializable

/**
 * اطلاعات یک رویداد مسدودسازی در برنامه Blocker.
 *
 * این مدل فقط اطلاعات ضروری رویداد را نگه می‌دارد
 * و متن کامل صفحه یا محتوای خصوصی کاربر را ذخیره نمی‌کند.
 */
@Serializable
data class BlockHistoryEntry(

    /**
     * شناسه یکتای رویداد.
     */
    val id: String,

    /**
     * نوع فیلتر:
     * WORD یا WEBSITE.
     */
    val filterType: FilterType,

    /**
     * نام برنامه‌ای که رویداد در آن اتفاق افتاده است.
     */
    val appName: String,

    /**
     * شناسه بسته برنامه.
     */
    val packageName: String,

    /**
     * کلمه یا دامنه‌ای که باعث مسدودسازی شده است.
     */
    val detectedValue: String,

    /**
     * نام قانونی که باعث مسدودسازی شده است.
     */
    val ruleName: String,

    /**
     * زمان وقوع رویداد بر اساس Unix timestamp بر حسب میلی‌ثانیه.
     */
    val detectedAt: Long,

    /**
     * مدت مسدودسازی بر حسب ثانیه.
     *
     * مقدار صفر می‌تواند نشان‌دهنده مسدودسازی دائمی باشد.
     */
    val blockedDurationSeconds: Long,

    /**
     * زمان پایان مسدودسازی بر حسب Unix timestamp.
     *
     * برای مسدودسازی دائمی مقدار null است.
     */
    val blockedUntil: Long?,

    /**
     * وضعیت نهایی رویداد.
     */
    val status: BlockStatus,

    /**
     * تعداد دفعات تکرار این رویداد.
     */
    val occurrenceCount: Int = 1,

    /**
     * توضیح فنی اختیاری درباره رویداد.
     */
    val technicalMessage: String? = null
) {

    /**
     * مشخص می‌کند آیا مسدودسازی دائمی بوده است.
     */
    val isPermanent: Boolean
        get() = blockedDurationSeconds == 0L && blockedUntil == null

    /**
     * مشخص می‌کند آیا زمان مسدودسازی تمام شده است.
     */
    fun isExpired(currentTimeMillis: Long = System.currentTimeMillis()): Boolean {
        if (isPermanent) return false

        val endTime = blockedUntil ?: return true

        return currentTimeMillis >= endTime
    }

    /**
     * مدت زمان باقی‌مانده تا پایان مسدودسازی.
     */
    fun remainingSeconds(
        currentTimeMillis: Long = System.currentTimeMillis()
    ): Long {
        if (isPermanent) return Long.MAX_VALUE

        val endTime = blockedUntil ?: return 0L

        return ((endTime - currentTimeMillis).coerceAtLeast(0L)) / 1000L
    }
}

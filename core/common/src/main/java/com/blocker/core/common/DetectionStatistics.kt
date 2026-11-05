package com.blocker.core.common

import kotlinx.serialization.Serializable

/**
 * آمار تشخیص و مسدودسازی یک مورد مشخص.
 */
@Serializable
data class DetectionStatistics(

    /**
     * شناسه یکتای آمار.
     */
    val id: String,

    /**
     * شناسه قانون مرتبط با این آمار.
     */
    val ruleId: String,

    /**
     * نوع فیلتر.
     */
    val filterType: FilterType,

    /**
     * مقدار تشخیص داده‌شده؛ کلمه یا دامنه.
     */
    val detectedValue: String,

    /**
     * تعداد کل دفعات تشخیص.
     */
    val totalDetections: Long = 0L,

    /**
     * تعداد دفعات تشخیص امروز.
     */
    val todayDetections: Long = 0L,

    /**
     * تعداد دفعات تشخیص در هفت روز اخیر.
     */
    val weeklyDetections: Long = 0L,

    /**
     * تعداد دفعات تشخیص در ماه جاری.
     */
    val monthlyDetections: Long = 0L,

    /**
     * تعداد دفعاتی که واقعاً مسدودسازی انجام شده است.
     */
    val successfulBlocks: Long = 0L,

    /**
     * تعداد دفعاتی که تشخیص فقط هشدار ایجاد کرده است.
     */
    val warningOnlyCount: Long = 0L,

    /**
     * زمان اولین تشخیص.
     */
    val firstDetectedAt: Long? = null,

    /**
     * زمان آخرین تشخیص.
     */
    val lastDetectedAt: Long? = null,

    /**
     * زمان آخرین به‌روزرسانی آمار.
     */
    val updatedAt: Long = System.currentTimeMillis()
) {

    /**
     * آیا تاکنون این مورد شناسایی شده است؟
     */
    val hasBeenDetected: Boolean
        get() = totalDetections > 0

    /**
     * آیا تاکنون برای این مورد مسدودسازی موفق انجام شده است؟
     */
    val hasSuccessfulBlock: Boolean
        get() = successfulBlocks > 0

    /**
     * بررسی اعتبار اطلاعات آماری.
     */
    fun isValid(): Boolean {
        return id.isNotBlank() &&
            ruleId.isNotBlank() &&
            detectedValue.isNotBlank() &&
            totalDetections >= 0 &&
            todayDetections >= 0 &&
            weeklyDetections >= 0 &&
            monthlyDetections >= 0 &&
            successfulBlocks >= 0 &&
            warningOnlyCount >= 0 &&
            successfulBlocks <= totalDetections &&
            warningOnlyCount <= totalDetections &&
            (firstDetectedAt == null || firstDetectedAt > 0) &&
            (lastDetectedAt == null || lastDetectedAt > 0) &&
            updatedAt > 0
    }

    companion object {

        /**
         * ساخت آمار اولیه برای یک قانون.
         */
        fun initial(
            ruleId: String,
            filterType: FilterType,
            detectedValue: String
        ): DetectionStatistics {
            return DetectionStatistics(
                id = "${filterType.name}_$ruleId",
                ruleId = ruleId,
                filterType = filterType,
                detectedValue = detectedValue
            )
        }
    }
}

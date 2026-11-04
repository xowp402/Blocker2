package com.blocker.core.common

import kotlinx.serialization.Serializable

/**
 * اطلاعات یک کلمه یا عبارت قابل تشخیص و مسدودسازی.
 */
@Serializable
data class WordEntry(

    /**
     * شناسه یکتای کلمه.
     */
    val id: String,

    /**
     * کلمه یا عبارت اصلی.
     */
    val value: String,

    /**
     * نسخه نرمال‌شده برای مقایسه.
     */
    val normalizedValue: String,

    /**
     * نام دسته‌بندی کلمه.
     */
    val categoryId: String? = null,

    /**
     * برنامه‌های هدف.
     *
     * مجموعه خالی یعنی تمام برنامه‌های تحت نظارت.
     */
    val targetPackages: Set<String> = emptySet(),

    /**
     * روش تشخیص کلمه.
     */
    val detectionMethod: DetectionMethod = DetectionMethod.ACCESSIBILITY,

    /**
     * مدت مسدودسازی بر حسب ثانیه.
     *
     * مقدار صفر یعنی مسدودسازی دائمی.
     */
    val blockDurationSeconds: Long = 120L,

    /**
     * متن هشدار اختصاصی این کلمه.
     */
    val warningMessage: String? = null,

    /**
     * آیا این قانون فعال است؟
     */
    val isEnabled: Boolean = true,

    /**
     * آیا تطبیق باید دقیق باشد؟
     */
    val exactMatch: Boolean = true,

    /**
     * آیا گونه‌های نوشتاری جایگزین نیز بررسی شوند؟
     */
    val includeVariants: Boolean = true,

    /**
     * گونه‌های نوشتاری جایگزین.
     */
    val variants: Set<String> = emptySet(),

    /**
     * تعداد دفعات تشخیص.
     */
    val detectionCount: Long = 0L,

    /**
     * آخرین زمان تشخیص بر حسب میلی‌ثانیه.
     */
    val lastDetectedAt: Long? = null,

    /**
     * زمان ایجاد قانون.
     */
    val createdAt: Long = System.currentTimeMillis(),

    /**
     * زمان آخرین ویرایش.
     */
    val updatedAt: Long = System.currentTimeMillis(),

    /**
     * اولویت قانون.
     *
     * عدد بزرگ‌تر یعنی اولویت بالاتر.
     */
    val priority: Int = 0
) {

    /**
     * آیا این قانون برای برنامه مشخص‌شده کاربرد دارد؟
     */
    fun appliesToPackage(packageName: String): Boolean {
        if (!isEnabled) return false

        if (packageName.isBlank()) return false

        if (AppPackageValidator.isBlockerPackage(packageName)) {
            return false
        }

        return targetPackages.isEmpty() ||
            packageName in targetPackages
    }

    /**
     * بررسی اعتبار اطلاعات کلمه.
     */
    fun isValid(): Boolean {
        return id.isNotBlank() &&
            value.isNotBlank() &&
            normalizedValue.isNotBlank() &&
            blockDurationSeconds >= 0 &&
            detectionCount >= 0 &&
            createdAt > 0 &&
            updatedAt > 0 &&
            targetPackages.none { it.isBlank() } &&
            variants.none { it.isBlank() }
    }

    /**
     * بررسی دائمی بودن مسدودسازی.
     */
    val isPermanentBlock: Boolean
        get() = blockDurationSeconds == 0L
}

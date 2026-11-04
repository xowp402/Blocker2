package com.blocker.core.common

import kotlinx.serialization.Serializable

/**
 * اطلاعات یک دامنه یا وب‌سایت قابل مسدودسازی.
 */
@Serializable
data class DomainEntry(

    /**
     * شناسه یکتای قانون دامنه.
     */
    val id: String,

    /**
     * دامنه اصلی یا آدرس واردشده توسط کاربر.
     */
    val domain: String,

    /**
     * نسخه نرمال‌شده دامنه برای مقایسه.
     */
    val normalizedDomain: String,

    /**
     * آیا زیر‌دامنه‌های این دامنه نیز مسدود شوند؟
     *
     * مثال:
     * با فعال بودن این گزینه، example.com و
     * sub.example.com هر دو مشمول قانون می‌شوند.
     */
    val includeSubdomains: Boolean = true,

    /**
     * برنامه‌های هدف.
     *
     * مجموعه خالی یعنی تمام برنامه‌های تحت پوشش فیلتر وب.
     */
    val targetPackages: Set<String> = emptySet(),

    /**
     * مدت مسدودسازی بر حسب ثانیه.
     *
     * صفر به معنی مسدودسازی دائمی است.
     */
    val blockDurationSeconds: Long = 120L,

    /**
     * پیام اختصاصی هنگام مسدود شدن دامنه.
     */
    val warningMessage: String? = null,

    /**
     * آیا قانون فعال است؟
     */
    val isEnabled: Boolean = true,

    /**
     * آیا این قانون شامل آدرس‌های دارای مسیر و پارامتر نیز می‌شود؟
     */
    val matchFullUrl: Boolean = false,

    /**
     * تعداد دفعات مسدودسازی دامنه.
     */
    val blockCount: Long = 0L,

    /**
     * آخرین زمان مسدودسازی.
     */
    val lastBlockedAt: Long? = null,

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
     * عدد بزرگ‌تر نشان‌دهنده اولویت بالاتر است.
     */
    val priority: Int = 0,

    /**
     * توضیحات اختیاری درباره دامنه.
     */
    val description: String? = null
) {

    /**
     * آیا این قانون برای برنامه مشخص‌شده فعال است؟
     */
    fun appliesToPackage(packageName: String): Boolean {
        if (!isEnabled || packageName.isBlank()) {
            return false
        }

        return targetPackages.isEmpty() ||
            packageName in targetPackages
    }

    /**
     * بررسی اعتبار اطلاعات دامنه.
     */
    fun isValid(): Boolean {
        return id.isNotBlank() &&
            domain.isNotBlank() &&
            normalizedDomain.isNotBlank() &&
            blockDurationSeconds >= 0 &&
            blockCount >= 0 &&
            createdAt > 0 &&
            updatedAt > 0 &&
            targetPackages.none { it.isBlank() }
    }

    /**
     * بررسی دائمی بودن مسدودسازی.
     */
    val isPermanentBlock: Boolean
        get() = blockDurationSeconds == 0L
}

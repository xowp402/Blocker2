package com.blocker.core.common

import kotlinx.serialization.Serializable

/**
 * قانون اختصاصی نظارت و مسدودسازی برای یک برنامه اندرویدی.
 */
@Serializable
data class AppRule(

    /**
     * شناسه یکتای قانون.
     */
    val id: String,

    /**
     * شناسه بسته برنامه اندرویدی.
     */
    val packageName: String,

    /**
     * نام نمایشی برنامه.
     */
    val appName: String,

    /**
     * آیا نظارت روی این برنامه فعال است؟
     */
    val isMonitoringEnabled: Boolean = true,

    /**
     * آیا تشخیص کلمات در این برنامه فعال است؟
     */
    val wordFilterEnabled: Boolean = true,

    /**
     * آیا فیلتر وب‌سایت برای این برنامه فعال است؟
     */
    val websiteFilterEnabled: Boolean = true,

    /**
     * آیا این قانون اجازه مسدودسازی برنامه را می‌دهد؟
     */
    val blockingEnabled: Boolean = true,

    /**
     * مدت مسدودسازی اختصاصی برنامه بر حسب ثانیه.
     *
     * مقدار null یعنی استفاده از مدت مسدودسازی قانون تشخیص‌شده.
     */
    val blockDurationOverrideSeconds: Long? = null,

    /**
     * کلمات اختصاصی این برنامه.
     *
     * مجموعه خالی یعنی استفاده از قوانین عمومی.
     */
    val includedWordIds: Set<String> = emptySet(),

    /**
     * کلماتی که نباید در این برنامه باعث مسدودسازی شوند.
     */
    val excludedWordIds: Set<String> = emptySet(),

    /**
     * دامنه‌های اختصاصی این برنامه.
     */
    val includedDomainIds: Set<String> = emptySet(),

    /**
     * دامنه‌هایی که برای این برنامه مستثنا هستند.
     */
    val excludedDomainIds: Set<String> = emptySet(),

    /**
     * آیا این قانون بر قوانین عمومی اولویت دارد؟
     */
    val overrideGeneralRules: Boolean = false,

    /**
     * اولویت اجرای قانون.
     */
    val priority: Int = 0,

    /**
     * زمان ایجاد قانون.
     */
    val createdAt: Long = System.currentTimeMillis(),

    /**
     * زمان آخرین ویرایش.
     */
    val updatedAt: Long = System.currentTimeMillis(),

    /**
     * توضیحات اختیاری.
     */
    val description: String? = null
) {

    /**
     * بررسی اعتبار قانون.
     */
    fun isValid(): Boolean {
        return id.isNotBlank() &&
            packageName.isNotBlank() &&
            appName.isNotBlank() &&
            (blockDurationOverrideSeconds == null ||
                blockDurationOverrideSeconds >= 0) &&
            createdAt > 0 &&
            updatedAt > 0 &&
            includedWordIds.none { it.isBlank() } &&
            excludedWordIds.none { it.isBlank() } &&
            includedDomainIds.none { it.isBlank() } &&
            excludedDomainIds.none { it.isBlank() }
    }

    /**
     * آیا نظارت روی این برنامه واقعاً فعال است؟
     */
    val isActive: Boolean
        get() = isMonitoringEnabled &&
            !AppPackageValidator.isBlockerPackage(packageName)

    /**
     * آیا یک کلمه مشخص برای این برنامه مستثنا شده است؟
     */
    fun isWordExcluded(wordId: String): Boolean {
        return wordId in excludedWordIds
    }

    /**
     * آیا یک دامنه مشخص برای این برنامه مستثنا شده است؟
     */
    fun isDomainExcluded(domainId: String): Boolean {
        return domainId in excludedDomainIds
    }

    /**
     * تعیین مدت مسدودسازی نهایی.
     */
    fun resolveBlockDuration(
        defaultDurationSeconds: Long
    ): Long {
        return blockDurationOverrideSeconds
            ?: defaultDurationSeconds
    }
}

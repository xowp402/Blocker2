package com.blocker.core.common

import kotlinx.serialization.Serializable

/**
 * اطلاعات شناسایی و مشخصات فایل پشتیبان Blocker.
 */
@Serializable
data class BackupMetadata(

    /**
     * شناسه قالب پشتیبان.
     */
    val formatId: String = FORMAT_ID,

    /**
     * نسخه قالب فایل پشتیبان.
     */
    val formatVersion: Int = CURRENT_FORMAT_VERSION,

    /**
     * نسخه برنامه‌ای که پشتیبان را ایجاد کرده است.
     */
    val appVersion: String,

    /**
     * شماره نسخه پایگاه داده هنگام تهیه پشتیبان.
     */
    val databaseVersion: Int,

    /**
     * زمان ایجاد پشتیبان بر اساس Unix timestamp میلی‌ثانیه.
     */
    val createdAt: Long = System.currentTimeMillis(),

    /**
     * تعداد کلمات ذخیره‌شده در پشتیبان.
     */
    val wordCount: Int = 0,

    /**
     * تعداد قوانین وب‌سایت و دامنه در پشتیبان.
     */
    val websiteRuleCount: Int = 0,

    /**
     * تعداد قوانین برنامه‌ها در پشتیبان.
     */
    val appRuleCount: Int = 0,

    /**
     * تعداد تنظیمات ذخیره‌شده.
     */
    val settingsCount: Int = 0,

    /**
     * آیا تاریخچه مسدودسازی نیز در پشتیبان قرار دارد؟
     */
    val includesHistory: Boolean = true,

    /**
     * توضیح اختیاری درباره نسخه پشتیبان.
     */
    val description: String? = null
) {

    /**
     * بررسی معتبر بودن اطلاعات اصلی پشتیبان.
     */
    fun isValid(): Boolean {
        return formatId == FORMAT_ID &&
            formatVersion > 0 &&
            appVersion.isNotBlank() &&
            databaseVersion > 0 &&
            createdAt > 0 &&
            wordCount >= 0 &&
            websiteRuleCount >= 0 &&
            appRuleCount >= 0 &&
            settingsCount >= 0
    }

    /**
     * بررسی سازگاری نسخه قالب پشتیبان با نسخه فعلی.
     */
    fun isSupportedFormat(): Boolean {
        return formatId == FORMAT_ID &&
            formatVersion <= CURRENT_FORMAT_VERSION
    }

    companion object {

        const val FORMAT_ID = "BLOCKER_BACKUP"

        const val CURRENT_FORMAT_VERSION = 1
    }
}

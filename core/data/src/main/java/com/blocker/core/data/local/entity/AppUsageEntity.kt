package com.blocker.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * موجودیت دیتابیس برای ذخیره آمار استفاده از برنامه‌ها.
 */
@Entity(
    tableName = "app_usage"
)
data class AppUsageEntity(

    /**
     * شناسه بسته برنامه؛ کلید اصلی جدول.
     */
    @PrimaryKey
    val packageName: String,

    /**
     * نام نمایشی برنامه.
     */
    val appName: String,

    /**
     * زمان آخرین استفاده.
     */
    val lastUsedAt: Long?,

    /**
     * مجموع مدت استفاده بر حسب میلی‌ثانیه.
     */
    val totalUsageMillis: Long,

    /**
     * تعداد شناسایی کلمات ممنوعه.
     */
    val wordDetectionCount: Int,

    /**
     * تعداد شناسایی دامنه‌های ممنوعه.
     */
    val domainDetectionCount: Int,

    /**
     * تعداد دفعات مسدودسازی برنامه.
     */
    val blockCount: Int,

    /**
     * آخرین زمان مسدودسازی.
     */
    val lastBlockedAt: Long?,

    /**
     * زمان آخرین به‌روزرسانی اطلاعات.
     */
    val updatedAt: Long
)

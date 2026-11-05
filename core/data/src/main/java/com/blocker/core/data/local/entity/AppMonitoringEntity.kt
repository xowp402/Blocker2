package com.blocker.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * موجودیت دیتابیس برای ذخیره تنظیمات نظارت برنامه‌ها.
 */
@Entity(
    tableName = "app_monitoring"
)
data class AppMonitoringEntity(

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
     * فعال بودن نظارت.
     */
    val isMonitoringEnabled: Boolean,

    /**
     * فعال بودن فیلتر کلمات.
     */
    val wordFilterEnabled: Boolean,

    /**
     * فعال بودن فیلتر وب‌سایت‌ها.
     */
    val websiteFilterEnabled: Boolean,

    /**
     * نمایش هشدار هنگام شناسایی.
     */
    val showWarning: Boolean,

    /**
     * استفاده از قوانین عمومی.
     */
    val useGlobalRules: Boolean,

    /**
     * اولویت قوانین اختصاصی.
     */
    val rulePriority: Int,

    /**
     * توضیحات تنظیمات.
     */
    val description: String,

    /**
     * زمان آخرین به‌روزرسانی.
     */
    val updatedAt: Long
)

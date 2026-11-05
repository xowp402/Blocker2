package com.blocker.core.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * موجودیت دیتابیس برای ذخیره تاریخچه شناسایی‌ها.
 */
@Entity(
    tableName = "app_detection_events",
    indices = [
        Index(value = ["packageName"]),
        Index(value = ["matchedValue"]),
        Index(value = ["targetType"]),
        Index(value = ["action"]),
        Index(value = ["detectedAt"]),
        Index(value = ["packageName", "detectedAt"])
    ]
)
data class AppDetectionEntity(

    /**
     * شناسه یکتای رویداد.
     */
    @PrimaryKey
    val id: String,

    /**
     * شناسه بسته برنامه.
     */
    val packageName: String,

    /**
     * نام نمایشی برنامه.
     */
    val appName: String,

    /**
     * نوع مورد شناسایی‌شده:
     * WORD, PHRASE, DOMAIN, URL یا IP_ADDRESS
     */
    val targetType: String,

    /**
     * کلمه، عبارت یا دامنه شناسایی‌شده.
     */
    val matchedValue: String,

    /**
     * شناسه قانون مرتبط.
     */
    val ruleId: String?,

    /**
     * نتیجه پردازش:
     * BLOCKED, WARNING_ONLY, IGNORED,
     * ALREADY_BLOCKED یا FAILED
     */
    val action: String,

    /**
     * مدت مسدودسازی بر حسب ثانیه.
     */
    val blockDurationSeconds: Long,

    /**
     * زمان شناسایی بر حسب میلی‌ثانیه.
     */
    val detectedAt: Long,

    /**
     * توضیح فنی خطا، در صورت وجود.
     */
    val errorMessage: String?
)

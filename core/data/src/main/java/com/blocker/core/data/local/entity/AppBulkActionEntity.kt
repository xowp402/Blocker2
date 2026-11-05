package com.blocker.core.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * موجودیت دیتابیس برای ذخیره عملیات گروهی.
 */
@Entity(
    tableName = "app_bulk_actions",
    indices = [
        Index(value = ["status"]),
        Index(value = ["createdAt"])
    ]
)
data class AppBulkActionEntity(

    /**
     * شناسه یکتای عملیات.
     */
    @PrimaryKey
    val id: String,

    /**
     * نوع عملیات گروهی.
     */
    val actionType: String,

    /**
     * شناسه بسته برنامه‌های هدف.
     * به صورت JSON ذخیره می‌شود.
     */
    val targetPackagesJson: String,

    /**
     * وضعیت اجرای عملیات.
     */
    val status: String,

    /**
     * زمان ایجاد درخواست.
     */
    val createdAt: Long,

    /**
     * زمان شروع اجرا.
     */
    val startedAt: Long?,

    /**
     * زمان پایان اجرا.
     */
    val completedAt: Long?,

    /**
     * تعداد برنامه‌های پردازش‌شده.
     */
    val processedCount: Int,

    /**
     * تعداد عملیات موفق.
     */
    val successCount: Int,

    /**
     * تعداد عملیات ناموفق.
     */
    val failedCount: Int,

    /**
     * پیام یا توضیح نتیجه.
     */
    val message: String?
)

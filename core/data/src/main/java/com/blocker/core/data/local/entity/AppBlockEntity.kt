package com.blocker.core.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * موجودیت دیتابیس برای ذخیره سوابق مسدودسازی برنامه‌ها.
 */
@Entity(
    tableName = "app_blocks",
    indices = [
        Index(value = ["packageName"]),
        Index(value = ["status"]),
        Index(value = ["expiresAt"]),
        Index(value = ["startedAt"])
    ]
)
data class AppBlockEntity(

    /**
     * شناسه یکتای رکورد.
     */
    @PrimaryKey
    val id: String,

    /**
     * شناسه بسته برنامه مسدودشده.
     */
    val packageName: String,

    /**
     * نام نمایشی برنامه.
     */
    val appName: String,

    /**
     * علت مسدودسازی.
     */
    val reason: String,

    /**
     * شناسه قانون ایجادکننده مسدودسازی.
     */
    val ruleId: String?,

    /**
     * زمان شروع مسدودسازی.
     */
    val startedAt: Long,

    /**
     * زمان پایان مسدودسازی.
     * مقدار null به معنی مسدودسازی دائمی است.
     */
    val expiresAt: Long?,

    /**
     * وضعیت فعلی مسدودسازی.
     */
    val status: String,

    /**
     * زمان پایان یا لغو مسدودسازی.
     */
    val completedAt: Long?,

    /**
     * توضیحات اختیاری.
     */
    val description: String
)

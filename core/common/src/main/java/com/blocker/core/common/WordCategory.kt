package com.blocker.core.common

import kotlinx.serialization.Serializable

/**
 * دسته‌بندی کلمات در برنامه Blocker.
 */
@Serializable
data class WordCategory(

    /**
     * شناسه یکتای دسته‌بندی.
     */
    val id: String,

    /**
     * نام دسته‌بندی.
     */
    val name: String,

    /**
     * توضیحات اختیاری دسته‌بندی.
     */
    val description: String? = null,

    /**
     * رنگ دسته‌بندی به صورت ARGB.
     */
    val colorArgb: Long = DEFAULT_COLOR,

    /**
     * آیا این دسته‌بندی توسط خود برنامه ایجاد شده است؟
     */
    val isSystemCategory: Boolean = false,

    /**
     * آیا دسته‌بندی فعال است؟
     */
    val isEnabled: Boolean = true,

    /**
     * تعداد کلمات مرتبط با این دسته‌بندی.
     */
    val wordCount: Int = 0,

    /**
     * ترتیب نمایش دسته‌بندی در رابط کاربری.
     */
    val displayOrder: Int = 0,

    /**
     * زمان ایجاد دسته‌بندی.
     */
    val createdAt: Long = System.currentTimeMillis(),

    /**
     * زمان آخرین ویرایش.
     */
    val updatedAt: Long = System.currentTimeMillis()
) {

    /**
     * بررسی معتبر بودن اطلاعات دسته‌بندی.
     */
    fun isValid(): Boolean {
        return id.isNotBlank() &&
            name.isNotBlank() &&
            wordCount >= 0 &&
            displayOrder >= 0 &&
            createdAt > 0 &&
            updatedAt > 0
    }

    companion object {

        const val DEFAULT_COLOR: Long = 0xFF607D8B

        /**
         * دسته‌بندی‌های پیش‌فرض برنامه.
         */
        fun defaultCategories(): List<WordCategory> {
            val now = System.currentTimeMillis()

            return listOf(
                WordCategory(
                    id = "social",
                    name = "شبکه‌های اجتماعی",
                    description = "کلمات و عبارت‌های مرتبط با شبکه‌های اجتماعی",
                    colorArgb = 0xFF2196F3,
                    isSystemCategory = true,
                    displayOrder = 0,
                    createdAt = now,
                    updatedAt = now
                ),
                WordCategory(
                    id = "games",
                    name = "بازی‌ها",
                    description = "کلمات و عبارت‌های مرتبط با بازی",
                    colorArgb = 0xFF9C27B0,
                    isSystemCategory = true,
                    displayOrder = 1,
                    createdAt = now,
                    updatedAt = now
                ),
                WordCategory(
                    id = "entertainment",
                    name = "سرگرمی",
                    description = "محتوای سرگرمی و تفریح",
                    colorArgb = 0xFFFF9800,
                    isSystemCategory = true,
                    displayOrder = 2,
                    createdAt = now,
                    updatedAt = now
                ),
                WordCategory(
                    id = "custom",
                    name = "دسته‌بندی شخصی",
                    description = "کلمات انتخابی کاربر",
                    colorArgb = 0xFF009688,
                    isSystemCategory = true,
                    displayOrder = 3,
                    createdAt = now,
                    updatedAt = now
                )
            )
        }
    }
}

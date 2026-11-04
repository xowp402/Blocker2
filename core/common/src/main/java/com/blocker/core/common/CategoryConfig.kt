package com.blocker.core.common

/**
 * Represents a user-defined or built-in category
 * for organizing word-filter rules.
 */
data class CategoryConfig(

    val id: String,

    val name: String,

    val description: String? = null,

    val colorHex: String = "#607D8B",

    val isBuiltIn: Boolean = false,

    val isEnabled: Boolean = true,

    val sortOrder: Int = 0,

    val createdAt: Long = System.currentTimeMillis(),

    val updatedAt: Long = System.currentTimeMillis()
) {

    init {
        require(id.isNotBlank()) {
            "Category ID cannot be empty."
        }

        require(name.isNotBlank()) {
            "Category name cannot be empty."
        }

        require(
            Regex("^#[0-9A-Fa-f]{6}$").matches(colorHex)
        ) {
            "Category color must be a valid six-digit HEX color."
        }
    }
}

/**
 * Default categories provided by Blocker.
 */
object DefaultCategories {

    val all: List<CategoryConfig> = listOf(

        CategoryConfig(
            id = "social",
            name = "شبکه‌های اجتماعی",
            description = "کلمات مربوط به شبکه‌های اجتماعی",
            colorHex = "#2196F3",
            isBuiltIn = true,
            sortOrder = 1
        ),

        CategoryConfig(
            id = "entertainment",
            name = "سرگرمی",
            description = "محتوای سرگرمی و تفریح",
            colorHex = "#9C27B0",
            isBuiltIn = true,
            sortOrder = 2
        ),

        CategoryConfig(
            id = "games",
            name = "بازی‌ها",
            description = "کلمات مربوط به بازی‌ها",
            colorHex = "#4CAF50",
            isBuiltIn = true,
            sortOrder = 3
        ),

        CategoryConfig(
            id = "custom",
            name = "شخصی",
            description = "قوانین و کلمات شخصی کاربر",
            colorHex = "#607D8B",
            isBuiltIn = true,
            sortOrder = 4
        )
    )
}

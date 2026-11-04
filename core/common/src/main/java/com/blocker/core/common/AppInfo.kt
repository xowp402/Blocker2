package com.blocker.core.common

/**
 * Information about an installed Android application.
 *
 * This model does not launch, modify, or uninstall apps.
 * It is used by Blocker's app-management interface.
 */
data class AppInfo(

    val packageName: String,

    val appName: String,

    val versionName: String? = null,

    val versionCode: Long = 0L,

    val category: AppCategory = AppCategory.OTHER,

    val isSystemApp: Boolean = false,

    val isMonitored: Boolean = false,

    val isBlockerApp: Boolean = false,

    val lastDetectedAt: Long? = null,

    val detectionCount: Long = 0L,

    val installedAt: Long? = null,

    val updatedAt: Long? = null
) {

    init {
        require(packageName.isNotBlank()) {
            "Package name cannot be empty."
        }

        require(appName.isNotBlank()) {
            "Application name cannot be empty."
        }

        require(detectionCount >= 0L) {
            "Detection count cannot be negative."
        }
    }

    /**
     * Returns whether this app can be monitored.
     */
    fun canBeMonitored(): Boolean {
        return !isBlockerApp
    }
}

/**
 * Categories used to organize installed applications.
 */
enum class AppCategory {

    SOCIAL,

    GAMES,

    BROWSERS,

    VIDEO,

    MESSAGING,

    EDUCATION,

    PRODUCTIVITY,

    SHOPPING,

    FINANCE,

    SYSTEM,

    OTHER
}

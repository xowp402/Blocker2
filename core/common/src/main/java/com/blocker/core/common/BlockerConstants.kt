package com.blocker.core.common

object BlockerConstants {

    const val APP_NAME = "Blocker"

    const val APPLICATION_ID = "com.blocker.app"

    const val DEFAULT_BLOCK_DURATION_SECONDS = 120L

    const val MIN_BLOCK_DURATION_SECONDS = 5L

    const val MAX_BLOCK_DURATION_SECONDS = 86400L

    const val ACCESSIBILITY_SERVICE_ID =
        "com.blocker.app/.service.accessibility.BlockerAccessibilityService"

    const val WEBSITE_VPN_CHANNEL_ID = "blocker_website_filter"

    const val GENERAL_NOTIFICATION_CHANNEL_ID = "blocker_general"

    const val BLOCK_NOTIFICATION_ID = 1001

    const val SERVICE_NOTIFICATION_ID = 1002

    const val HEALTH_NOTIFICATION_ID = 1003

    const val DATABASE_NAME = "blocker_database"

    const val DATABASE_VERSION = 1

    const val BACKUP_DIRECTORY = "BlockerBackups"

    const val DEFAULT_LANGUAGE = "system"

    const val LANGUAGE_PERSIAN = "fa"

    const val LANGUAGE_ENGLISH = "en"
}

enum class FilterType {
    WORD,
    WEBSITE
}

enum class FilterStatus {
    ENABLED,
    DISABLED,
    WARNING,
    ERROR
}

enum class BlockStatus {
    ACTIVE,
    EXPIRED,
    CANCELLED
}

enum class DetectionMethod {
    ACCESSIBILITY,
    NOTIFICATION,
    MANUAL
}

enum class RuleScope {
    ALL_APPS,
    SELECTED_APPS,
    SELECTED_BROWSERS,
    ALL_BROWSERS
}

enum class ConflictResolution {
    MOST_RESTRICTIVE,
    MANUAL_PRIORITY,
    WARN_ONLY
}

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

enum class AppLanguage {
    SYSTEM,
    PERSIAN,
    ENGLISH
}

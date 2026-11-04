package com.blocker.core.common

/**
 * Global application settings.
 *
 * These settings describe user preferences and service behavior.
 */
data class BlockerSettings(

    val language: AppLanguage = AppLanguage.SYSTEM,

    val themeMode: ThemeMode = ThemeMode.SYSTEM,

    val usePersianDigits: Boolean = true,

    val wordFilterEnabled: Boolean = false,

    val websiteFilterEnabled: Boolean = false,

    val showWordDetectionNotifications: Boolean = true,

    val showWebsiteBlockNotifications: Boolean = true,

    val showServiceErrorNotifications: Boolean = true,

    val showDailySummaryNotifications: Boolean = false,

    val autoRestoreServicesAfterReboot: Boolean = true,

    val attemptServiceRecovery: Boolean = true,

    val defaultBlockDurationSeconds: Long =
        BlockerConstants.DEFAULT_BLOCK_DURATION_SECONDS,

    val conflictResolution: ConflictResolution =
        ConflictResolution.MOST_RESTRICTIVE,

    val uncertainDetectionBehavior: UncertainDetectionBehavior =
        UncertainDetectionBehavior.IGNORE,

    val saveDetectionHistory: Boolean = true,

    val saveTechnicalLogs: Boolean = true,

    val excludeBlockerFromMonitoring: Boolean = true,

    val requireConfirmationBeforeDeletingRules: Boolean = true,

    val updatedAt: Long = System.currentTimeMillis()
) {

    init {
        require(
            defaultBlockDurationSeconds in
                BlockerConstants.MIN_BLOCK_DURATION_SECONDS..
                BlockerConstants.MAX_BLOCK_DURATION_SECONDS
        ) {
            "Default block duration is invalid."
        }
    }
}

/**
 * Behavior when text detection is uncertain.
 */
enum class UncertainDetectionBehavior {

    IGNORE,

    SHOW_WARNING
}

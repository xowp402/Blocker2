package com.blocker.core.common

/**
 * Represents a voluntary commitment period configured by the user.
 *
 * A commitment protects selected settings from accidental changes,
 * but it must never prevent emergency access or permanently lock
 * the user out of their own device.
 */
data class CommitmentConfig(

    val id: String,

    val enabled: Boolean = false,

    val title: String = "تعهد شخصی",

    val startedAt: Long = System.currentTimeMillis(),

    val durationSeconds: Long = 0L,

    val protectRules: Boolean = true,

    val protectWordDeletion: Boolean = true,

    val protectWebsiteDeletion: Boolean = true,

    val protectFilterSettings: Boolean = true,

    val allowEmergencyExit: Boolean = true,

    val emergencyExitRequiresConfirmation: Boolean = true,

    val emergencyExitReasonRequired: Boolean = true,

    val emergencyExitCount: Int = 0,

    val lastEmergencyExitAt: Long? = null,

    val endedAt: Long? = null,

    val createdAt: Long = System.currentTimeMillis(),

    val updatedAt: Long = System.currentTimeMillis()
) {

    init {
        require(id.isNotBlank()) {
            "Commitment ID cannot be empty."
        }

        require(durationSeconds >= 0L) {
            "Commitment duration cannot be negative."
        }

        require(emergencyExitCount >= 0) {
            "Emergency exit count cannot be negative."
        }
    }

    /**
     * Returns the commitment expiration timestamp.
     */
    fun expiresAt(): Long? {

        if (!enabled || durationSeconds == 0L) {
            return null
        }

        return startedAt + durationSeconds * 1000L
    }

    /**
     * Checks whether the commitment is currently active.
     */
    fun isActive(
        currentTimeMillis: Long = System.currentTimeMillis()
    ): Boolean {

        if (!enabled || endedAt != null) {
            return false
        }

        val expiration = expiresAt() ?: return true

        return currentTimeMillis < expiration
    }

    /**
     * Returns the remaining commitment duration in seconds.
     */
    fun remainingSeconds(
        currentTimeMillis: Long = System.currentTimeMillis()
    ): Long {

        if (!isActive(currentTimeMillis)) {
            return 0L
        }

        val expiration = expiresAt() ?: return Long.MAX_VALUE

        val remainingMillis =
            expiration - currentTimeMillis

        return if (remainingMillis <= 0L) {
            0L
        } else {
            (remainingMillis + 999L) / 1000L
        }
    }

    /**
     * Records an emergency exit without deleting its history.
     */
    fun recordEmergencyExit(
        currentTimeMillis: Long = System.currentTimeMillis()
    ): CommitmentConfig {

        require(allowEmergencyExit) {
            "Emergency exit is not enabled."
        }

        return copy(
            enabled = false,
            emergencyExitCount = emergencyExitCount + 1,
            lastEmergencyExitAt = currentTimeMillis,
            endedAt = currentTimeMillis,
            updatedAt = currentTimeMillis
        )
    }

    /**
     * Ends the commitment normally.
     */
    fun end(
        currentTimeMillis: Long = System.currentTimeMillis()
    ): CommitmentConfig {

        return copy(
            enabled = false,
            endedAt = currentTimeMillis,
            updatedAt = currentTimeMillis
        )
    }
}

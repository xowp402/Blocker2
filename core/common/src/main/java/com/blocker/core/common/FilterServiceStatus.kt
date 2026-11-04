package com.blocker.core.common

/**
 * Represents the runtime health of a Blocker service.
 */
data class FilterServiceStatus(

    val serviceId: String,

    val serviceName: String,

    val filterType: FilterType? = null,

    val status: FilterStatus = FilterStatus.DISABLED,

    val isRunning: Boolean = false,

    val permissionGranted: Boolean = false,

    val lastStartedAt: Long? = null,

    val lastStoppedAt: Long? = null,

    val lastHealthCheckAt: Long? = null,

    val lastError: String? = null,

    val restartAttempts: Int = 0,

    val updatedAt: Long = System.currentTimeMillis()
) {

    init {
        require(serviceId.isNotBlank()) {
            "Service ID cannot be empty."
        }

        require(serviceName.isNotBlank()) {
            "Service name cannot be empty."
        }

        require(restartAttempts >= 0) {
            "Restart attempts cannot be negative."
        }
    }

    /**
     * Whether the service is healthy enough to operate.
     */
    fun isHealthy(): Boolean {
        return isRunning &&
            permissionGranted &&
            status == FilterStatus.ENABLED &&
            lastError == null
    }

    /**
     * Creates a new status after a successful service start.
     */
    fun markStarted(
        currentTimeMillis: Long = System.currentTimeMillis()
    ): FilterServiceStatus {

        return copy(
            status = FilterStatus.ENABLED,
            isRunning = true,
            lastStartedAt = currentTimeMillis,
            lastHealthCheckAt = currentTimeMillis,
            lastError = null,
            restartAttempts = 0,
            updatedAt = currentTimeMillis
        )
    }

    /**
     * Creates a new status after a service failure.
     */
    fun markFailed(
        error: String,
        currentTimeMillis: Long = System.currentTimeMillis()
    ): FilterServiceStatus {

        return copy(
            status = FilterStatus.ERROR,
            isRunning = false,
            lastStoppedAt = currentTimeMillis,
            lastHealthCheckAt = currentTimeMillis,
            lastError = error,
            restartAttempts = restartAttempts + 1,
            updatedAt = currentTimeMillis
        )
    }

    /**
     * Creates a new status after a normal service stop.
     */
    fun markStopped(
        currentTimeMillis: Long = System.currentTimeMillis()
    ): FilterServiceStatus {

        return copy(
            status = FilterStatus.DISABLED,
            isRunning = false,
            lastStoppedAt = currentTimeMillis,
            lastHealthCheckAt = currentTimeMillis,
            lastError = null,
            updatedAt = currentTimeMillis
        )
    }
}

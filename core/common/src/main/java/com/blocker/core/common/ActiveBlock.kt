package com.blocker.core.common

/**
 * Represents an active temporary or permanent block.
 */
data class ActiveBlock(

    val id: String,

    val targetId: String,

    val targetName: String,

    val targetPackage: String? = null,

    val filterType: FilterType,

    val reason: String,

    val warningMessage: String,

    val startedAt: Long = System.currentTimeMillis(),

    val durationSeconds: Long? =
        BlockerConstants.DEFAULT_BLOCK_DURATION_SECONDS,

    val permanent: Boolean = false,

    val status: BlockStatus = BlockStatus.ACTIVE,

    val sourceRuleId: String? = null,

    val restartCount: Int = 0,

    val updatedAt: Long = System.currentTimeMillis()
) {

    init {
        require(id.isNotBlank()) {
            "Block ID cannot be empty."
        }

        require(targetId.isNotBlank()) {
            "Target ID cannot be empty."
        }

        require(targetName.isNotBlank()) {
            "Target name cannot be empty."
        }

        require(permanent || durationSeconds != null) {
            "A temporary block must have a duration."
        }

        require(
            permanent ||
                (durationSeconds != null &&
                    durationSeconds >=
                    BlockerConstants.MIN_BLOCK_DURATION_SECONDS)
        ) {
            "Block duration is invalid."
        }

        require(restartCount >= 0) {
            "Restart count cannot be negative."
        }
    }

    /**
     * Calculates the expiration time.
     * A permanent block has no expiration.
     */
    fun expiresAt(): Long? {

        if (permanent) return null

        val duration = durationSeconds ?: return null

        return startedAt + duration * 1000L
    }

    /**
     * Returns the remaining blocking time in seconds.
     */
    fun remainingSeconds(
        currentTimeMillis: Long = System.currentTimeMillis()
    ): Long {

        if (status != BlockStatus.ACTIVE) return 0L

        if (permanent) return Long.MAX_VALUE

        val expiration = expiresAt() ?: return 0L

        val remainingMillis =
            expiration - currentTimeMillis

        if (remainingMillis <= 0L) return 0L

        return (remainingMillis + 999L) / 1000L
    }

    /**
     * Checks whether the block is still active.
     */
    fun isActive(
        currentTimeMillis: Long = System.currentTimeMillis()
    ): Boolean {

        if (status != BlockStatus.ACTIVE) return false

        return permanent ||
            remainingSeconds(currentTimeMillis) > 0L
    }

    /**
     * Creates an updated block after repeated detection.
     */
    fun restart(
        newDurationSeconds: Long = durationSeconds
            ?: BlockerConstants.DEFAULT_BLOCK_DURATION_SECONDS,
        currentTimeMillis: Long = System.currentTimeMillis()
    ): ActiveBlock {

        require(
            newDurationSeconds in
                BlockerConstants.MIN_BLOCK_DURATION_SECONDS..
                BlockerConstants.MAX_BLOCK_DURATION_SECONDS
        ) {
            "New block duration is invalid."
        }

        return copy(
            startedAt = currentTimeMillis,
            durationSeconds = newDurationSeconds,
            status = BlockStatus.ACTIVE,
            restartCount = restartCount + 1,
            updatedAt = currentTimeMillis
        )
    }
}

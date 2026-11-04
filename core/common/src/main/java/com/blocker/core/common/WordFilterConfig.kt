package com.blocker.core.common

/**
 * Configuration for an individual word-filter rule.
 */
data class WordFilterConfig(

    val id: String,

    val word: String,

    val enabled: Boolean = true,

    val categoryId: String? = null,

    val targetPackages: Set<String> = emptySet(),

    val detectionMethod: DetectionMethod =
        DetectionMethod.ACCESSIBILITY,

    val blockDurationSeconds: Long =
        BlockerConstants.DEFAULT_BLOCK_DURATION_SECONDS,

    val warningMessage: String =
        "این برنامه به دلیل تشخیص کلمه تعیین‌شده موقتاً مسدود شده است.",

    val matchCaseSensitive: Boolean = false,

    val matchWholeWord: Boolean = true,

    val ignoreZwnj: Boolean = true,

    val restartDurationOnRepeatedDetection: Boolean = true,

    val detectionCount: Long = 0L,

    val lastDetectedAt: Long? = null,

    val priority: Int = 0,

    val createdAt: Long = System.currentTimeMillis(),

    val updatedAt: Long = System.currentTimeMillis()
) {

    init {
        require(id.isNotBlank()) {
            "Rule ID cannot be empty."
        }

        require(word.isNotBlank()) {
            "Word cannot be empty."
        }

        require(blockDurationSeconds in
            BlockerConstants.MIN_BLOCK_DURATION_SECONDS..
            BlockerConstants.MAX_BLOCK_DURATION_SECONDS
        ) {
            "Block duration is outside the allowed range."
        }

        require(detectionCount >= 0) {
            "Detection count cannot be negative."
        }
    }

    /**
     * Checks whether this rule applies to the given app.
     */
    fun appliesTo(packageName: String): Boolean {
        if (!enabled) return false

        // Empty package list means all monitored apps.
        return targetPackages.isEmpty() ||
            packageName in targetPackages
    }
}

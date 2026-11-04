package com.blocker.core.common

/**
 * Represents a detection event without storing
 * the full private screen content.
 */
data class DetectionEvent(

    val id: String,

    val filterType: FilterType,

    val detectedValue: String,

    val normalizedValue: String,

    val sourcePackage: String,

    val sourceAppName: String? = null,

    val ruleId: String? = null,

    val detectedAt: Long = System.currentTimeMillis(),

    val actionTaken: DetectionAction,

    val confidence: Float = 1f,

    val technicalDetails: String? = null
) {

    init {
        require(id.isNotBlank()) {
            "Event ID cannot be empty."
        }

        require(detectedValue.isNotBlank()) {
            "Detected value cannot be empty."
        }

        require(sourcePackage.isNotBlank()) {
            "Source package cannot be empty."
        }

        require(confidence in 0f..1f) {
            "Confidence must be between 0 and 1."
        }
    }
}

/**
 * Action taken after a detection.
 */
enum class DetectionAction {

    BLOCKED,

    WARNING_SHOWN,

    IGNORED,

    BLOCK_RESTARTED,

    BLOCK_EXTENDED,

    FILTER_DISABLED,

    DETECTION_ERROR
}

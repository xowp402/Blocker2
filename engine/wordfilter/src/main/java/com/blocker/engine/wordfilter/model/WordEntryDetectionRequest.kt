package com.blocker.engine.wordfilter.model

import com.blocker.core.common.model.WordEntry

data class WordEntryDetectionRequest(
    val packageName: String,
    val visibleText: String,
    val entries: List<WordEntry>,
    val timestamp: Long = System.currentTimeMillis(),
    val monitoringEnabled: Boolean = true
) {
    val isValid: Boolean
        get() = packageName.isNotBlank() &&
                visibleText.isNotBlank() &&
                entries.isNotEmpty() &&
                timestamp > 0L

    val enabledEntries: List<WordEntry>
        get() = entries.filter { it.isEnabled }
}

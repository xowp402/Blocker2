package com.blocker.core.common.model

data class WordEntryImportResult(
    val totalLines: Int,
    val importedCount: Int,
    val duplicateCount: Int,
    val invalidCount: Int,
    val importedWords: List<String> = emptyList(),
    val duplicateWords: List<String> = emptyList(),
    val invalidWords: List<String> = emptyList()
) {
    val hasErrors: Boolean
        get() = duplicateCount > 0 || invalidCount > 0

    val isSuccessful: Boolean
        get() = importedCount > 0 && invalidCount == 0

    val skippedCount: Int
        get() = duplicateCount + invalidCount
}

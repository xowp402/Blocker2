package com.blocker.core.common.validation

import com.blocker.core.common.model.ValidationResult
import com.blocker.core.common.model.WordEntry

object WordEntryValidator {

    fun validate(entry: WordEntry): ValidationResult {

        val word = entry.word.trim()

        if (word.isBlank()) {
            return ValidationResult.Invalid(
                reason = "Word cannot be empty."
            )
        }

        if (word.length > 200) {
            return ValidationResult.Invalid(
                reason = "Word cannot exceed 200 characters."
            )
        }

        if (entry.normalizedWord.isBlank()) {
            return ValidationResult.Invalid(
                reason = "Normalized word cannot be empty."
            )
        }

        if (entry.detectionCount < 0) {
            return ValidationResult.Invalid(
                reason = "Detection count cannot be negative."
            )
        }

        if (entry.createdAt < 0 || entry.updatedAt < 0) {
            return ValidationResult.Invalid(
                reason = "Invalid timestamp."
            )
        }

        return ValidationResult.Valid
    }
}

package com.blocker.core.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "word_entries",
    indices = [
        Index(value = ["normalizedWord"], unique = true),
        Index(value = ["isEnabled"]),
        Index(value = ["categoryId"])
    ]
)
data class WordEntryEntity(

    @PrimaryKey
    val id: String,

    val word: String,

    val normalizedWord: String,

    val description: String = "",

    val categoryId: String? = null,

    val isEnabled: Boolean = true,

    val blockDurationMillis: Long = 120_000L,

    val warningMessage: String = "",

    val detectionCount: Int = 0,

    val lastDetectedAt: Long? = null,

    val createdAt: Long = System.currentTimeMillis(),

    val updatedAt: Long = System.currentTimeMillis()
)

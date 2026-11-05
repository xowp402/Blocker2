package com.blocker.core.data.mapper

import com.blocker.core.common.model.WordEntry
import com.blocker.core.data.local.entity.WordEntryEntity

object WordEntryMapper {

    fun toDomain(entity: WordEntryEntity): WordEntry {
        return WordEntry(
            id = entity.id,
            word = entity.word,
            normalizedWord = entity.normalizedWord,
            categoryId = entity.categoryId,
            isEnabled = entity.isEnabled,
            detectionCount = entity.detectionCount,
            lastDetectedAt = entity.lastDetectedAt,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    fun toEntity(model: WordEntry): WordEntryEntity {
        return WordEntryEntity(
            id = model.id,
            word = model.word,
            normalizedWord = model.normalizedWord,
            categoryId = model.categoryId,
            isEnabled = model.isEnabled,
            detectionCount = model.detectionCount,
            lastDetectedAt = model.lastDetectedAt,
            createdAt = model.createdAt,
            updatedAt = model.updatedAt
        )
    }

    fun toDomainList(
        entities: List<WordEntryEntity>
    ): List<WordEntry> {
        return entities.map(::toDomain)
    }

    fun toEntityList(
        models: List<WordEntry>
    ): List<WordEntryEntity> {
        return models.map(::toEntity)
    }
}

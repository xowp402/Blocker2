package com.blocker.core.data.service

import com.blocker.core.common.model.WordEntry

enum class WordEntrySortType {
    NAME_ASCENDING,
    NAME_DESCENDING,
    MOST_DETECTED,
    LEAST_DETECTED,
    NEWEST,
    OLDEST,
    LAST_DETECTED
}

object WordEntrySortService {

    fun sort(
        entries: List<WordEntry>,
        sortType: WordEntrySortType
    ): List<WordEntry> {

        return when (sortType) {

            WordEntrySortType.NAME_ASCENDING ->
                entries.sortedBy { it.word.lowercase() }

            WordEntrySortType.NAME_DESCENDING ->
                entries.sortedByDescending { it.word.lowercase() }

            WordEntrySortType.MOST_DETECTED ->
                entries.sortedByDescending { it.detectionCount }

            WordEntrySortType.LEAST_DETECTED ->
                entries.sortedBy { it.detectionCount }

            WordEntrySortType.NEWEST ->
                entries.sortedByDescending { it.createdAt }

            WordEntrySortType.OLDEST ->
                entries.sortedBy { it.createdAt }

            WordEntrySortType.LAST_DETECTED ->
                entries.sortedByDescending {
                    it.lastDetectedAt ?: 0L
                }
        }
    }
}

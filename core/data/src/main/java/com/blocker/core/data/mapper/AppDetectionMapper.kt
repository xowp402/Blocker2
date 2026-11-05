package com.blocker.core.data.mapper

import com.blocker.core.common.AppDetectionEvent
import com.blocker.core.data.local.entity.AppDetectionEntity

/**
 * تبدیل رویدادهای تشخیص بین دیتابیس و مدل اصلی.
 */
object AppDetectionMapper {

    /**
     * تبدیل Entity به مدل دامنه.
     */
    fun AppDetectionEntity.toDomain(): AppDetectionEvent {
        return AppDetectionEvent(
            id = id,
            packageName = packageName,
            appName = appName,
            matchedValue = matchedValue,
            detectedAt = detectedAt,
            detectionMethod = detectionMethod,
            wasBlocked = wasBlocked
        )
    }

    /**
     * تبدیل مدل دامنه به Entity.
     */
    fun AppDetectionEvent.toEntity(): AppDetectionEntity {
        return AppDetectionEntity(
            id = id,
            packageName = packageName,
            appName = appName,
            matchedValue = matchedValue,
            detectedAt = detectedAt,
            detectionMethod = detectionMethod,
            wasBlocked = wasBlocked
        )
    }

    /**
     * تبدیل فهرست Entityها به مدل دامنه.
     */
    fun List<AppDetectionEntity>.toDomainList(): List<AppDetectionEvent> {
        return map { it.toDomain() }
    }

    /**
     * تبدیل فهرست مدل دامنه به Entity.
     */
    fun List<AppDetectionEvent>.toEntityList(): List<AppDetectionEntity> {
        return map { it.toEntity() }
    }
}

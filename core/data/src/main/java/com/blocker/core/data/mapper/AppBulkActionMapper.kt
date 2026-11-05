package com.blocker.core.data.mapper

import com.blocker.core.common.AppBulkAction
import com.blocker.core.data.local.entity.AppBulkActionEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * تبدیل عملیات گروهی بین مدل دامنه و دیتابیس.
 */
object AppBulkActionMapper {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    /**
     * تبدیل Entity دیتابیس به مدل اصلی.
     */
    fun AppBulkActionEntity.toDomain(): AppBulkAction {
        val packages = runCatching {
            json.decodeFromString<List<String>>(
                targetPackagesJson
            )
        }.getOrDefault(emptyList())

        return AppBulkAction(
            id = id,
            actionType = actionType,
            targetPackages = packages,
            createdAt = createdAt
        )
    }

    /**
     * تبدیل مدل اصلی به Entity دیتابیس.
     */
    fun AppBulkAction.toEntity(): AppBulkActionEntity {
        return AppBulkActionEntity(
            id = id,
            actionType = actionType,
            targetPackagesJson = json.encodeToString(
                targetPackages
            ),
            createdAt = createdAt
        )
    }

    /**
     * تبدیل فهرست Entityها به مدل اصلی.
     */
    fun List<AppBulkActionEntity>.toDomainList(): List<AppBulkAction> {
        return map { it.toDomain() }
    }

    /**
     * تبدیل فهرست مدل‌های اصلی به Entity.
     */
    fun List<AppBulkAction>.toEntityList(): List<AppBulkActionEntity> {
        return map { it.toEntity() }
    }
}

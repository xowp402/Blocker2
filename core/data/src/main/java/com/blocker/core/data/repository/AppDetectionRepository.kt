package com.blocker.core.data.repository

import com.blocker.core.common.AppDetectionEvent
import com.blocker.core.common.DetectionAction
import com.blocker.core.common.DetectionTargetType
import kotlinx.coroutines.flow.Flow

/**
 * رابط مدیریت تاریخچه شناسایی موارد ممنوعه.
 */
interface AppDetectionRepository {

    /**
     * مشاهده تمام رویدادهای شناسایی.
     */
    fun observeAllEvents(): Flow<List<AppDetectionEvent>>

    /**
     * مشاهده رویدادهای مربوط به یک برنامه.
     */
    fun observeEventsByPackage(
        packageName: String
    ): Flow<List<AppDetectionEvent>>

    /**
     * مشاهده رویدادهای مربوط به یک کلمه یا دامنه.
     */
    fun observeEventsByValue(
        matchedValue: String
    ): Flow<List<AppDetectionEvent>>

    /**
     * مشاهده رویدادها بر اساس نوع مورد شناسایی‌شده.
     */
    fun observeEventsByType(
        targetType: DetectionTargetType
    ): Flow<List<AppDetectionEvent>>

    /**
     * مشاهده رویدادها بر اساس نتیجه پردازش.
     */
    fun observeEventsByAction(
        action: DetectionAction
    ): Flow<List<AppDetectionEvent>>

    /**
     * دریافت یک رویداد با شناسه مشخص.
     */
    suspend fun getEvent(
        id: String
    ): AppDetectionEvent?

    /**
     * ثبت یک رویداد شناسایی.
     */
    suspend fun saveEvent(
        event: AppDetectionEvent
    )

    /**
     * ثبت چند رویداد شناسایی.
     */
    suspend fun saveEvents(
        events: List<AppDetectionEvent>
    )

    /**
     * دریافت تعداد کل رویدادها.
     */
    suspend fun getEventCount(): Long

    /**
     * دریافت تعداد شناسایی‌های یک برنامه.
     */
    suspend fun getEventCountByPackage(
        packageName: String
    ): Long

    /**
     * دریافت تعداد شناسایی‌های یک کلمه یا دامنه.
     */
    suspend fun getEventCountByValue(
        matchedValue: String
    ): Long

    /**
     * دریافت تعداد رویدادها در یک بازه زمانی.
     */
    suspend fun getEventCountBetween(
        startTime: Long,
        endTime: Long
    ): Long

    /**
     * حذف یک رویداد مشخص.
     */
    suspend fun deleteEvent(
        id: String
    )

    /**
     * حذف رویدادهای قدیمی‌تر از زمان مشخص.
     */
    suspend fun deleteEventsBefore(
        timestamp: Long
    )

    /**
     * حذف تمام تاریخچه شناسایی‌ها.
     */
    suspend fun clearAllEvents()
}

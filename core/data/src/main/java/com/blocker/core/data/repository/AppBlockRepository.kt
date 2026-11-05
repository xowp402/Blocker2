package com.blocker.core.data.repository

import com.blocker.core.common.AppBlockRecord
import com.blocker.core.common.AppBlockRecordStatus
import kotlinx.coroutines.flow.Flow

/**
 * رابط مدیریت اطلاعات مسدودسازی برنامه‌ها.
 */
interface AppBlockRepository {

    /**
     * مشاهده تمام رکوردهای مسدودسازی.
     */
    fun observeAllBlocks(): Flow<List<AppBlockRecord>>

    /**
     * مشاهده مسدودسازی‌های فعال.
     */
    fun observeActiveBlocks(): Flow<List<AppBlockRecord>>

    /**
     * مشاهده مسدودسازی یک برنامه.
     */
    fun observeBlock(
        packageName: String
    ): Flow<AppBlockRecord?>

    /**
     * دریافت مستقیم یک رکورد بر اساس شناسه.
     */
    suspend fun getBlock(
        id: String
    ): AppBlockRecord?

    /**
     * دریافت مسدودسازی فعال یک برنامه.
     */
    suspend fun getActiveBlock(
        packageName: String
    ): AppBlockRecord?

    /**
     * ثبت مسدودسازی جدید.
     */
    suspend fun saveBlock(
        record: AppBlockRecord
    )

    /**
     * ثبت چند مسدودسازی.
     */
    suspend fun saveBlocks(
        records: List<AppBlockRecord>
    )

    /**
     * تغییر وضعیت مسدودسازی.
     */
    suspend fun updateStatus(
        id: String,
        status: AppBlockRecordStatus,
        completedAt: Long = System.currentTimeMillis()
    )

    /**
     * لغو مسدودسازی یک برنامه.
     */
    suspend fun cancelBlock(
        id: String,
        cancelledAt: Long = System.currentTimeMillis()
    )

    /**
     * پایان دادن به مسدودسازی‌های منقضی‌شده.
     */
    suspend fun expireBlocks(
        currentTime: Long = System.currentTimeMillis()
    )

    /**
     * دریافت مسدودسازی‌های فعال برای بازیابی پس از راه‌اندازی گوشی.
     */
    suspend fun getBlocksForRecovery(): List<AppBlockRecord>

    /**
     * حذف رکوردهای قدیمی‌تر از زمان مشخص.
     */
    suspend fun deleteBlocksBefore(
        timestamp: Long
    )

    /**
     * حذف تمام سوابق مسدودسازی.
     */
    suspend fun clearAllBlocks()
}

package com.blocker.core.data.repository

import com.blocker.core.common.AppBulkAction
import com.blocker.core.common.AppBulkActionStatus
import kotlinx.coroutines.flow.Flow

/**
 * رابط مدیریت عملیات گروهی برنامه‌ها.
 */
interface AppBulkActionRepository {

    /**
     * مشاهده تمام عملیات گروهی.
     */
    fun observeAllActions(): Flow<List<AppBulkAction>>

    /**
     * مشاهده عملیات در حال اجرا.
     */
    fun observeRunningActions(): Flow<List<AppBulkAction>>

    /**
     * مشاهده عملیات بر اساس شناسه.
     */
    fun observeAction(
        id: String
    ): Flow<AppBulkAction?>

    /**
     * دریافت مستقیم یک عملیات.
     */
    suspend fun getAction(
        id: String
    ): AppBulkAction?

    /**
     * ثبت درخواست عملیات گروهی جدید.
     */
    suspend fun saveAction(
        action: AppBulkAction
    )

    /**
     * به‌روزرسانی اطلاعات عملیات.
     */
    suspend fun updateAction(
        action: AppBulkAction
    )

    /**
     * تغییر وضعیت عملیات.
     */
    suspend fun updateStatus(
        id: String,
        status: AppBulkActionStatus
    )

    /**
     * دریافت عملیات‌های نیمه‌تمام برای بازیابی.
     */
    suspend fun getPendingActions(): List<AppBulkAction>

    /**
     * حذف یک عملیات.
     */
    suspend fun deleteAction(
        id: String
    )

    /**
     * حذف عملیات‌های قدیمی‌تر از زمان مشخص.
     */
    suspend fun deleteActionsBefore(
        timestamp: Long
    )

    /**
     * حذف تمام سوابق عملیات گروهی.
     */
    suspend fun clearAllActions()
}

package com.blocker.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.blocker.core.data.local.entity.AppBulkActionEntity

@Dao
interface AppBulkActionDao {

    /**
     * مشاهده تمام عملیات گروهی.
     */
    @Query(
        """
        SELECT * FROM app_bulk_actions
        ORDER BY createdAt DESC
        """
    )
    fun observeAll(): Flow<List<AppBulkActionEntity>>

    /**
     * دریافت عملیات بر اساس شناسه.
     */
    @Query(
        """
        SELECT * FROM app_bulk_actions
        WHERE id = :id
        LIMIT 1
        """
    )
    suspend fun getById(
        id: String
    ): AppBulkActionEntity?

    /**
     * ثبت یک عملیات گروهی.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(
        entity: AppBulkActionEntity
    )

    /**
     * ثبت چند عملیات گروهی.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(
        entities: List<AppBulkActionEntity>
    )

    /**
     * دریافت آخرین عملیات‌ها.
     */
    @Query(
        """
        SELECT * FROM app_bulk_actions
        ORDER BY createdAt DESC
        LIMIT :limit
        """
    )
    suspend fun getRecent(
        limit: Int
    ): List<AppBulkActionEntity>

    /**
     * شمارش کل عملیات‌های ثبت‌شده.
     */
    @Query(
        """
        SELECT COUNT(*) FROM app_bulk_actions
        """
    )
    suspend fun getTotalCount(): Int

    /**
     * حذف عملیات‌های قدیمی.
     */
    @Query(
        """
        DELETE FROM app_bulk_actions
        WHERE createdAt < :timestamp
        """
    )
    suspend fun deleteBefore(
        timestamp: Long
    )

    /**
     * حذف یک عملیات مشخص.
     */
    @Query(
        """
        DELETE FROM app_bulk_actions
        WHERE id = :id
        """
    )
    suspend fun deleteById(
        id: String
    )

    /**
     * حذف تمام سوابق عملیات گروهی.
     */
    @Query(
        """
        DELETE FROM app_bulk_actions
        """
    )
    suspend fun deleteAll()
}

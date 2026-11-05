package com.blocker.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.blocker.core.data.local.entity.AppBlockEntity
import kotlinx.coroutines.flow.Flow

/**
 * دسترسی به جدول مسدودسازی برنامه‌ها.
 */
@Dao
interface AppBlockDao {

    /**
     * مشاهده تمام سوابق مسدودسازی.
     */
    @Query(
        """
        SELECT * FROM app_blocks
        ORDER BY startedAt DESC
        """
    )
    fun observeAll(): Flow<List<AppBlockEntity>>

    /**
     * مشاهده مسدودسازی‌های فعال.
     */
    @Query(
        """
        SELECT * FROM app_blocks
        WHERE status = 'ACTIVE'
        ORDER BY startedAt DESC
        """
    )
    fun observeActive(): Flow<List<AppBlockEntity>>

    /**
     * مشاهده مسدودسازی‌های یک برنامه.
     */
    @Query(
        """
        SELECT * FROM app_blocks
        WHERE packageName = :packageName
        ORDER BY startedAt DESC
        """
    )
    fun observeByPackage(
        packageName: String
    ): Flow<List<AppBlockEntity>>

    /**
     * دریافت یک رکورد بر اساس شناسه.
     */
    @Query(
        """
        SELECT * FROM app_blocks
        WHERE id = :id
        LIMIT 1
        """
    )
    suspend fun getById(
        id: String
    ): AppBlockEntity?

    /**
     * دریافت مسدودسازی فعال یک برنامه.
     *
     * مسدودسازی دائمی نیز فعال محسوب می‌شود.
     */
    @Query(
        """
        SELECT * FROM app_blocks
        WHERE packageName = :packageName
          AND status = 'ACTIVE'
          AND (expiresAt IS NULL OR expiresAt > :currentTime)
        ORDER BY startedAt DESC
        LIMIT 1
        """
    )
    suspend fun getActiveByPackage(
        packageName: String,
        currentTime: Long = System.currentTimeMillis()
    ): AppBlockEntity?

    /**
     * ثبت یا جایگزینی رکورد مسدودسازی.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(
        entity: AppBlockEntity
    )

    /**
     * ثبت چند رکورد مسدودسازی.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(
        entities: List<AppBlockEntity>
    )

    /**
     * تغییر وضعیت یک مسدودسازی.
     */
    @Query(
        """
        UPDATE app_blocks
        SET status = :status,
            completedAt = :completedAt
        WHERE id = :id
        """
    )
    suspend fun updateStatus(
        id: String,
        status: String,
        completedAt: Long
    )

    /**
     * تمدید زمان پایان مسدودسازی.
     */
    @Query(
        """
        UPDATE app_blocks
        SET expiresAt = :newExpiresAt
        WHERE id = :id
          AND status = 'ACTIVE'
        """
    )
    suspend fun updateExpiration(
        id: String,
        newExpiresAt: Long?
    )

    /**
     * دریافت مسدودسازی‌های منقضی‌شده.
     */
    @Query(
        """
        SELECT * FROM app_blocks
        WHERE status = 'ACTIVE'
          AND expiresAt IS NOT NULL
          AND expiresAt <= :currentTime
        """
    )
    suspend fun getExpired(
        currentTime: Long = System.currentTimeMillis()
    ): List<AppBlockEntity>

    /**
     * پایان دادن به تمام مسدودسازی‌های منقضی‌شده.
     */
    @Query(
        """
        UPDATE app_blocks
        SET status = 'EXPIRED',
            completedAt = :currentTime
        WHERE status = 'ACTIVE'
          AND expiresAt IS NOT NULL
          AND expiresAt <= :currentTime
        """
    )
    suspend fun expireAll(
        currentTime: Long = System.currentTimeMillis()
    )

    /**
     * دریافت مسدودسازی‌های فعال برای بازیابی پس از راه‌اندازی گوشی.
     */
    @Query(
        """
        SELECT * FROM app_blocks
        WHERE status = 'ACTIVE'
          AND (expiresAt IS NULL OR expiresAt > :currentTime)
        ORDER BY startedAt ASC
        """
    )
    suspend fun getActiveForRecovery(
        currentTime: Long = System.currentTimeMillis()
    ): List<AppBlockEntity>

    /**
     * حذف سوابق قدیمی.
     */
    @Query(
        """
        DELETE FROM app_blocks
        WHERE startedAt < :timestamp
        """
    )
    suspend fun deleteBefore(
        timestamp: Long
    )

    /**
     * تعداد مسدودسازی‌های فعال.
     */
    @Query(
        """
        SELECT COUNT(*) FROM app_blocks
        WHERE status = 'ACTIVE'
          AND (expiresAt IS NULL OR expiresAt > :currentTime)
        """
    )
    suspend fun getActiveCount(
        currentTime: Long = System.currentTimeMillis()
    ): Int

    /**
     * حذف تمام سوابق مسدودسازی.
     */
    @Query("DELETE FROM app_blocks")
    suspend fun deleteAll()
}

package com.blocker.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.blocker.core.data.local.entity.WordEntryEntity

@Dao
interface WordEntryDao {

    /**
     * مشاهده تمام واژه‌ها.
     */
    @Query(
        """
        SELECT * FROM word_entries
        ORDER BY createdAt DESC
        """
    )
    fun observeAll(): Flow<List<WordEntryEntity>>

    /**
     * مشاهده واژه‌های فعال.
     */
    @Query(
        """
        SELECT * FROM word_entries
        WHERE isEnabled = 1
        ORDER BY createdAt DESC
        """
    )
    fun observeEnabled(): Flow<List<WordEntryEntity>>

    /**
     * مشاهده واژه‌های یک دسته‌بندی.
     */
    @Query(
        """
        SELECT * FROM word_entries
        WHERE categoryId = :categoryId
        ORDER BY word COLLATE NOCASE ASC
        """
    )
    fun observeByCategory(
        categoryId: String
    ): Flow<List<WordEntryEntity>>

    /**
     * دریافت واژه بر اساس شناسه.
     */
    @Query(
        """
        SELECT * FROM word_entries
        WHERE id = :id
        LIMIT 1
        """
    )
    suspend fun getById(
        id: String
    ): WordEntryEntity?

    /**
     * دریافت واژه بر اساس متن نرمال‌شده.
     */
    @Query(
        """
        SELECT * FROM word_entries
        WHERE normalizedWord = :normalizedWord
        LIMIT 1
        """
    )
    suspend fun getByNormalizedWord(
        normalizedWord: String
    ): WordEntryEntity?

    /**
     * جست‌وجو در نام واژه‌ها و توضیحات.
     */
    @Query(
        """
        SELECT * FROM word_entries
        WHERE word LIKE '%' || :query || '%'
           OR description LIKE '%' || :query || '%'
        ORDER BY word COLLATE NOCASE ASC
        """
    )
    fun search(
        query: String
    ): Flow<List<WordEntryEntity>>

    /**
     * مرتب‌سازی بر اساس نام.
     */
    @Query(
        """
        SELECT * FROM word_entries
        ORDER BY word COLLATE NOCASE ASC
        """
    )
    fun observeSortedByName(): Flow<List<WordEntryEntity>>

    /**
     * مرتب‌سازی بر اساس تعداد تشخیص.
     */
    @Query(
        """
        SELECT * FROM word_entries
        ORDER BY detectionCount DESC, word COLLATE NOCASE ASC
        """
    )
    fun observeSortedByDetectionCount(): Flow<List<WordEntryEntity>>

    /**
     * دریافت آخرین واژه‌های تشخیص‌داده‌شده.
     */
    @Query(
        """
        SELECT * FROM word_entries
        WHERE lastDetectedAt IS NOT NULL
        ORDER BY lastDetectedAt DESC
        LIMIT :limit
        """
    )
    suspend fun getRecentlyDetected(
        limit: Int
    ): List<WordEntryEntity>

    /**
     * ثبت واژه جدید.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(
        entity: WordEntryEntity
    )

    /**
     * ثبت چند واژه جدید.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(
        entities: List<WordEntryEntity>
    )

    /**
     * به‌روزرسانی واژه.
     */
    @Update
    suspend fun update(
        entity: WordEntryEntity
    )

    /**
     * فعال یا غیرفعال کردن واژه.
     */
    @Query(
        """
        UPDATE word_entries
        SET isEnabled = :enabled,
            updatedAt = :updatedAt
        WHERE id = :id
        """
    )
    suspend fun setEnabled(
        id: String,
        enabled: Boolean,
        updatedAt: Long = System.currentTimeMillis()
    )

    /**
     * افزایش تعداد تشخیص واژه.
     */
    @Query(
        """
        UPDATE word_entries
        SET detectionCount = detectionCount + 1,
            lastDetectedAt = :detectedAt,
            updatedAt = :detectedAt
        WHERE id = :id
        """
    )
    suspend fun incrementDetectionCount(
        id: String,
        detectedAt: Long = System.currentTimeMillis()
    )

    /**
     * حذف واژه بر اساس شناسه.
     */
    @Query(
        """
        DELETE FROM word_entries
        WHERE id = :id
        """
    )
    suspend fun deleteById(
        id: String
    )

    /**
     * حذف چند واژه.
     */
    @Query(
        """
        DELETE FROM word_entries
        WHERE id IN (:ids)
        """
    )
    suspend fun deleteByIds(
        ids: List<String>
    )

    /**
     * تعداد کل واژه‌ها.
     */
    @Query(
        """
        SELECT COUNT(*) FROM word_entries
        """
    )
    suspend fun getTotalCount(): Int

    /**
     * تعداد واژه‌های فعال.
     */
    @Query(
        """
        SELECT COUNT(*) FROM word_entries
        WHERE isEnabled = 1
        """
    )
    suspend fun getEnabledCount(): Int

    /**
     * بررسی وجود واژه تکراری.
     */
    @Query(
        """
        SELECT EXISTS(
            SELECT 1 FROM word_entries
            WHERE normalizedWord = :normalizedWord
        )
        """
    )
    suspend fun exists(
        normalizedWord: String
    ): Boolean

    /**
     * حذف تمام واژه‌ها.
     */
    @Query("DELETE FROM word_entries")
    suspend fun deleteAll()
}

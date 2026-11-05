package com.blocker.core.data.local

import android.content.Context
import androidx.room.Room

/**
 * سازنده و ارائه‌دهنده پایگاه داده Blocker.
 *
 * از ایجاد چند نمونه مستقل دیتابیس جلوگیری می‌کند.
 */
object BlockerDatabaseProvider {

    @Volatile
    private var instance: BlockerDatabase? = null

    /**
     * دریافت نمونه مشترک دیتابیس.
     */
    fun getDatabase(context: Context): BlockerDatabase {
        return instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }
    }

    /**
     * ساخت دیتابیس محلی.
     */
    private fun buildDatabase(context: Context): BlockerDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            BlockerDatabase::class.java,
            BlockerDatabase.DATABASE_NAME
        )
            .build()
    }
}

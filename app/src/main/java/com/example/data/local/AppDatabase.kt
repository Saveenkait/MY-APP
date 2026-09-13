package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.MixingPack
import com.example.data.model.RemixOrder
import com.example.data.model.Song

@Database(
    entities = [Song::class, MixingPack::class, RemixOrder::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun mixingPackDao(): MixingPackDao
    abstract fun remixOrderDao(): RemixOrderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dj_remix_hub.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

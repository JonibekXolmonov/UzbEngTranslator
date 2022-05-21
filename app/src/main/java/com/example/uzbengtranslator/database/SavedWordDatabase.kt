package com.example.uzbengtranslator.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.uzbengtranslator.database.dao.SavedWordDao
import com.example.uzbengtranslator.model.SavedWord

@Database(entities = [SavedWord::class], version = 1)
abstract class SavedWordDatabase : RoomDatabase() {

    abstract fun savedWordDao(): SavedWordDao

    companion object {
        private var instance: SavedWordDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SavedWordDatabase {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(context, SavedWordDatabase::class.java, "savedWord.db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
            }
            return instance!!
        }
    }
}
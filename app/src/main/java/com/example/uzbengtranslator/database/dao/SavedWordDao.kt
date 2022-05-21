package com.example.uzbengtranslator.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.uzbengtranslator.model.SavedWord

@Dao
interface SavedWordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveWord(savedWord: SavedWord)

    @Query("SELECT * FROM SavedWords")
    fun getSavedWords(): List<SavedWord>

    @Query("DELETE FROM SavedWords WHERE id=:id")
    fun removeFromDatabase(id: Int)

}
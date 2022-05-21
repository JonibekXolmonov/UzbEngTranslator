package com.example.uzbengtranslator.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SavedWords")
data class SavedWord(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var uzWord: String,
    var enWord: String
)
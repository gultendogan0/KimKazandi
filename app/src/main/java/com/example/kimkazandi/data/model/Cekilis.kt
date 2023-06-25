package com.example.kimkazandi.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cekilisler")
data class Cekilis (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title:String,
    val date:String,
    val gift:String,
    val price:String,
    val imageUrl: String
)
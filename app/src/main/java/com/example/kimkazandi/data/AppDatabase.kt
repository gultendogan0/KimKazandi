package com.example.kimkazandi.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.kimkazandi.data.model.Cekilis

@TypeConverters(Converters::class)
@Database(entities = [Cekilis::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cekilisDao(): CekilisDao
}
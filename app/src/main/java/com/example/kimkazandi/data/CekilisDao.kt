package com.example.kimkazandi.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.kimkazandi.data.model.Cekilis

@Dao
interface CekilisDao {
    @Insert
    suspend fun insert(cekilis: Cekilis)

    @Query("SELECT * FROM cekilisler")
    fun getAllData(): LiveData<List<Cekilis>>

    @Query("SELECT * FROM cekilisler WHERE title = :title")
    fun getCekilisByTitle(title: String): Cekilis?

    @Delete
    suspend fun deleteItem(cekilis: Cekilis)
}
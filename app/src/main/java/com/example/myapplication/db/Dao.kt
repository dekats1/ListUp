package com.example.myapplication.db

import androidx.room.*
import com.example.myapplication.utils.ListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MainDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ListItem)

    @Delete
    suspend fun deleteItem(item: ListItem)

    @Query("SELECT * FROM main WHERE category LIKE :cat")
    fun getAllItemsByCategory(cat: String): Flow<List<ListItem>>

    @Query("SELECT * FROM main WHERE isfav = 1")
    fun getFavorites(): Flow<List<ListItem>>
}
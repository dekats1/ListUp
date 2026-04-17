

package com.example.myapplication.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.utils.ListItem

@Database(entities = [ListItem::class], version = 1)
abstract class MainDb : RoomDatabase() {
    abstract val dao: MainDao
}
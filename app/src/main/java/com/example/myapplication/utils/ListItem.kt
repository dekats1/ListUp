package com.example.myapplication.utils

import androidx.compose.runtime.saveable.listSaver
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "main")
data class ListItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val imageName: String,
    val htmlName: String,
    val isfav: Boolean = false,
    val category: String = ""
)

val ItemSaver = listSaver<ListItem, Any>(
    save = { listOf(it.id ?: 0, it.title, it.imageName, it.htmlName, it.isfav, it.category) },
    restore = {
        ListItem(
            id = it[0] as Int,
            title = it[1] as String,
            imageName = it[2] as String,
            htmlName = it[3] as String,
            isfav = it[4] as Boolean,
            category = it[5] as String
        )
    }
)
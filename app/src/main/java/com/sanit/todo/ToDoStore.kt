package com.sanit.todo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Todotable1")
data class ToDoStore(

    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    @ColumnInfo(name = "title")
    var todotitle: String,
    @ColumnInfo(name = "desc")
    var tododesc: String,
    @ColumnInfo(name = "date")
    var tododate: String

)
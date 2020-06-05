package com.sanit.todo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(ToDoStore::class), version = 1)
abstract class ToDoDB : RoomDatabase() {
    abstract fun ToDoDAO(): ToDoDAO

    companion object {
        var INSTANCE: ToDoDB? = null
        fun getAPPDataBase(context: Context): ToDoDB? {
            if (INSTANCE == null) {
                synchronized(ToDoDB::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ToDoDB::class.java,
                        "Todotable1"
                    ).build()
                }

            }

            return INSTANCE
        }
    }

}
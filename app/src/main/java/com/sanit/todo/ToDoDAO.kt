package com.sanit.todo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ToDoDAO {
    @Insert()
    fun insert(todoDAO: ToDoStore)

    @Query("select * from Todotable1")
    fun getToDoList(): List<ToDoStore>

    @Query("select id from Todotable1")
    fun getToDoId(): Long?

    @Query("DELETE from Todotable1")
    fun clear()

    @Query("DELETE FROM todotable1 WHERE id = :todoId")
    fun deleteByUserId(todoId: Long?)

    @Delete
    fun deleteList(toDoStore: ToDoStore)


}
package com.example.to_dolist.ui.theme

import android.R.attr.id
import com.example.to_dolist.todo
import java.time.Instant
import java.util.Date

object todoManager {
    private val todoList = mutableListOf<todo>()

    fun getallTodo():List<todo>{
        return todoList
    }

    fun addTodo(title:String){
        todoList.add(todo(System.currentTimeMillis().toInt(),title,Date.from(Instant.now())))
    }

    fun deleteTodo(id:Int){
        todoList.removeIf{
            it.id==id}
    }

}
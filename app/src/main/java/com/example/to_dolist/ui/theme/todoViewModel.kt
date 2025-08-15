package com.example.to_dolist.ui.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.to_dolist.todo


class todoViewModel : ViewModel() {
    private var _todoList= MutableLiveData<List<todo>>()
    val todoList: LiveData<List<todo>> = _todoList


    fun getallTodo(){
        _todoList.value=todoManager.getallTodo().reversed()
    }

    fun addTodo(title:String){
        todoManager.addTodo(title)
        getallTodo()

    }

    fun deleteTodo(id: Int){
        todoManager.deleteTodo(id)
        getallTodo()

    }
}
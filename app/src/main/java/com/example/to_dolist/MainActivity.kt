package com.example.to_dolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.to_dolist.ui.theme.TodoListTheme
import com.example.to_dolist.ui.theme.todoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val todoViewModel = ViewModelProvider(this)[todoViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            TodoListTheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color= MaterialTheme.colorScheme.background
                ){
                    Todopage(todoViewModel)
                }
            }
        }
    }
}

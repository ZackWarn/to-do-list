package com.example.to_dolist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.to_dolist.ui.theme.todoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

@Composable
fun Todopage(viewModel: todoViewModel) {

    val todolist by viewModel.todoList.observeAsState()
    var inputText by remember { mutableStateOf("") }

    val buttonScale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(inputText) {
        if (inputText.isNotEmpty() && inputText.length == 1) {
            buttonScale.animateTo(1.2f, animationSpec = tween(150))
            buttonScale.animateTo(1f, animationSpec = tween(150))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .padding(top = 32.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text(text = "Add a new task") },
                singleLine = true,
            )
            Button(
                onClick = {
                    viewModel.addTodo(inputText)
                    inputText = ""
                },
                modifier = Modifier.scale(buttonScale.value),
                enabled = inputText.isNotBlank()
            ) {
                Text("Add")
            }
        }

        todolist?.let { todos ->
            if (todos.isEmpty()) {
                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    text = "No items yet 📝",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(todos, key = { _, item -> item.id }) { _, item ->

                        key(item.id) {
                            var visible by remember { mutableStateOf(true) }
                            AnimatedVisibility(
                                visible = visible,
                                enter = fadeIn(tween(300)) + expandVertically(
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                ),
                                exit = fadeOut(tween(300)) + shrinkVertically(tween(300))
                            ) {
                                Todoitem(
                                    item = item,
                                    onDelete = {
                                        visible = false
                                        scope.launch {
                                            delay(300)
                                            viewModel.deleteTodo(item.id)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Todoitem(item: todo, onDelete: () -> Unit) {
    val cardElevation = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(cardElevation.value),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = SimpleDateFormat("HH:mm aa, dd/MM", Locale.ENGLISH).format(item.createdAt),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = item.title,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 3,
                    softWrap = true
                )
            }

            IconButton(
                onClick = {
                    scope.launch {
                        cardElevation.animateTo(0.95f, tween(100))
                        cardElevation.animateTo(1f, tween(100))
                        onDelete()
                    }
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.icons8_delete),
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodopagePreview() {
    val mockViewModel = object : todoViewModel() {
        private val sampleTodos = MutableLiveData(
            listOf(
                todo(id = 1, title = "Buy groceries", createdAt = System.currentTimeMillis()),
                todo(id = 2, title = "Read Jetpack Compose docs", createdAt = System.currentTimeMillis())
            )
        )
        override val todoList: LiveData<List<todo>> = sampleTodos
    }

    Todopage(viewModel = mockViewModel)
}

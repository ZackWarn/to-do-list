package com.example.to_dolist


import com.example.to_dolist.R
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
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


@Composable
fun Todopage(viewModel: todoViewModel) {

    val todolist by viewModel.todoList.observeAsState()
    var inputText by remember { mutableStateOf("") }

    // Animation for the add button
    val buttonScale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(inputText) {
        if (inputText.isNotEmpty() && inputText.length == 1) {
            // Subtle pulse animation when starting to type
            buttonScale.animateTo(1.2f, animationSpec = tween(150))
            buttonScale.animateTo(1f, animationSpec = tween(150))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .padding(top = 24.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .animateContentSize(animationSpec = tween(200))
            )
            Button(
                onClick = {
                    viewModel.addTodo(inputText)
                    inputText = ""
                },
                modifier = Modifier.scale(buttonScale.value)
            ) {
                Text(text = "Add")
            }
        }

        todolist?.let { todos ->
            LazyColumn {
                itemsIndexed(
                    items = todos,
                    key = { _, item -> item.id }
                ) { index, item ->
                    // Use key to track item state for animation
                    key(item.id) {
                        var visible by remember { mutableStateOf(true) }

                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(animationSpec = tween(300)) +
                                    expandVertically(animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )),
                            exit = fadeOut(animationSpec = tween(300)) +
                                    shrinkVertically(animationSpec = tween(300))
                        ) {
                            Todoitem(
                                item = item,
                                onDelete = {
                                    visible = false
                                    // Delay deletion to allow exit animation to play
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
        } ?: Text(
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center,
            text = "No items yet 📝",
            fontSize = 16.sp
        )
    }
}

@Composable
fun Todoitem(item: todo, onDelete: () -> Unit) {
    // Card animation
    val cardElevation = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .scale(cardElevation.value)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.DarkGray)
            .padding(10.dp)
            .animateContentSize(animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )),
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = SimpleDateFormat("HH:mm aa, dd/MM", Locale.ENGLISH).format(item.createdAt),
                fontSize = 12.sp,
                color = Color.LightGray
            )
            Text(
                text = item.title,
                fontSize = 20.sp,
                color = Color.White,
                maxLines = 10,
                softWrap = true
            )
        }

        // Delete button with animation
        IconButton(
            onClick = {
                // Button press animation before deletion
                scope.launch {
                    cardElevation.animateTo(0.95f, animationSpec = tween(100))
                    cardElevation.animateTo(1f, animationSpec = tween(100))
                    onDelete()
                }
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.icons8_delete),
                contentDescription = "Delete",
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )

        }
    }
}




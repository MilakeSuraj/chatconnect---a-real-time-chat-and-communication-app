package com.example.myapplication.view.chatroom

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.border

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomListView(
    onChatRoomSelected: (String) -> Unit,
    chatRoomListViewModel: ChatRoomListViewModel = viewModel()
) {
    val chatRooms by chatRoomListViewModel.chatRooms.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var newRoomName by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }

    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(Color(0xFF6A11CB), Color(0xFF2575FC))
    )

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .drawWithCache {
                        onDrawBehind {
                            drawRect(gradientBrush)
                        }
                    }
            ) {
                TopAppBar(
                    title = { Box(Modifier.fillMaxWidth()) {} }, // Empty to avoid default title placement
                    actions = {
                        IconButton(onClick = { showDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = "Create Chat Room", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Chat Rooms", color = Color.White, style = MaterialTheme.typography.titleLarge)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(chatRooms) { room ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable { onChatRoomSelected(room) }
                            .border(
                                width = 1.dp,
                                color = Color(0xFF2575FC),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Text(room, style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false; newRoomName = ""; errorText = "" },
                title = { Text("Create Chat Room") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = newRoomName,
                            onValueChange = { newRoomName = it },
                            label = { Text("Room Name") },
                            singleLine = true
                        )
                        if (errorText.isNotEmpty()) {
                            Text(errorText, color = MaterialTheme.colorScheme.error)
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        chatRoomListViewModel.createChatRoom(newRoomName,
                            onSuccess = {
                                showDialog = false; newRoomName = ""; errorText = ""
                            },
                            onError = { error -> errorText = error }
                        )
                    }) {
                        Text("Create")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showDialog = false; newRoomName = ""; errorText = "" }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

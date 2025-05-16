package com.example.myapplication.view.chatroom

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(chatRooms) { room ->
                    Card(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clickable { onChatRoomSelected(room.name) }
                            .border(
                                width = 2.dp,
                                color = Color(0xFF2575FC),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .drawWithCache {
                                    val gradientBrush = Brush.horizontalGradient(
                                        colors = listOf(Color(0xFF6A11CB), Color(0xFF2575FC))
                                    )
                                    onDrawBehind { drawRect(gradientBrush) }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        room.name,
                                        style = MaterialTheme.typography.titleLarge,
                                        color = Color.White,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.BottomEnd
                                ) {
                                    Text(
                                        "Created by ${room.creator}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White.copy(alpha = 0.8f)
                                    )
                                }
                            }
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

package com.example.myapplication.view.chatroom

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.border
import com.google.firebase.messaging.FirebaseMessaging
import com.example.myapplication.R
import androidx.compose.foundation.background
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.Fill

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

    // Simple, light color palette
    val topBarColor = Color(0xFFEDF4FF)
    val cardBgColor = Color(0xFFFFFFFF)
    val cardBorderColor = Color(0xFFBFD7ED)
    val iconBgColor = Color(0xFFE3ECF7)
    val iconTint = Color(0xFF4F8CFF)
    val creatorTextColor = Color(0xFF7B8FA1)
    val backgroundColor = Color(0xFFF7FAFC)

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(topBarColor)
                    .shadow(2.dp)
            ) {
                TopAppBar(
                    title = { Box(Modifier.fillMaxWidth()) {} },
                    actions = {
                        IconButton(onClick = { showDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = "Create Chat Room", tint = iconTint)
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Chat Rooms",
                        color = Color(0xFF1A2B49),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(padding)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(18.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                items(chatRooms) { room ->
                    Card(
                        modifier = Modifier
                            .aspectRatio(0.95f)
                            .clickable {
                                val safeRoomName = room.name.replace(Regex("[^A-Za-z0-9_\\-]"), "_")
                                FirebaseMessaging.getInstance().subscribeToTopic("room_${safeRoomName}")
                                onChatRoomSelected(room.name)
                            }
                            .shadow(4.dp, RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = cardBgColor
                        ),
                        border = BorderStroke(1.dp, cardBorderColor),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(iconBgColor, shape = RoundedCornerShape(14.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Home",
                                    tint = iconTint,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                room.name,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color(0xFF1A2B49),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                ),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            // Show creator name directly below the room name
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Created by ${room.creator}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = creatorTextColor,
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            }
            if (showDialog) {
                // Custom home-shaped popup
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x66000000))
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .graphicsLayer {
                                shadowElevation = 16f
                                shape = RoundedCornerShape(bottomEnd = 24.dp, bottomStart = 24.dp, topEnd = 12.dp, topStart = 12.dp)
                                clip = true
                            }
                    ) {
                        // Draw the "roof" triangle
                        Canvas(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .size(220.dp, 60.dp)
                        ) {
                            val path = Path().apply {
                                moveTo(size.width / 2f, 0f)
                                lineTo(0f, size.height)
                                lineTo(size.width, size.height)
                                close()
                            }
                            drawPath(
                                path = path,
                                color = Color(0xFF4F8CFF)
                            )
                        }
                        // Draw the "body" of the house
                        Card(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .width(260.dp)
                                .height(280.dp) // Increased height from 220.dp to 280.dp
                                .offset(y = 30.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = null,
                                    tint = Color(0xFF4F8CFF),
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Create Chat Room",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1A2B49),
                                    fontSize = 20.sp
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                OutlinedTextField(
                                    value = newRoomName,
                                    onValueChange = { newRoomName = it },
                                    label = { Text("Room Name") },
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp)
                                )
                                if (errorText.isNotEmpty()) {
                                    Text(
                                        errorText,
                                        color = MaterialTheme.colorScheme.error,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { showDialog = false; newRoomName = ""; errorText = "" },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Cancel")
                                    }
                                    Button(
                                        onClick = {
                                            chatRoomListViewModel.createChatRoom(newRoomName,
                                                onSuccess = {
                                                    showDialog = false; newRoomName = ""; errorText = ""
                                                },
                                                onError = { error -> errorText = error }
                                            )
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Create")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }}
    }


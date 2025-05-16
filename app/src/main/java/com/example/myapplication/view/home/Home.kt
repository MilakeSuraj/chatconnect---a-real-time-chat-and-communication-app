package com.example.myapplication.view.home

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.Constants
import com.example.myapplication.view.SingleMessage
import com.example.myapplication.view.register.ui.theme.Pink40
import com.example.myapplication.view.register.ui.theme.Purple40
import kotlin.math.abs


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    roomId: String,
    homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.provideFactory(roomId)),
    onBackClick: () -> Unit = {} // Add this parameter for back navigation
) {
    val message: String by homeViewModel.message.observeAsState(initial = "")
    val messages: List<Map<String, Any>> by homeViewModel.messages.observeAsState(
        initial = emptyList<Map<String, Any>>().toMutableList()
    )

    // Define a list of modern, vibrant colors for user messages
    val userColors = listOf(
        Color(0xFF1ABC9C), // Turquoise
        Color(0xFF2ECC71), // Emerald
        Color(0xFF3498DB), // Peter River
        Color(0xFF9B59B6), // Amethyst
        Color(0xFF34495E), // Wet Asphalt
        Color(0xFFF1C40F), // Sun Flower
        Color(0xFFE67E22), // Carrot
        Color(0xFFE74C3C), // Alizarin
        Color(0xFF16A085), // Green Sea
        Color(0xFF2980B9), // Belize Hole
        Color(0xFF8E44AD), // Wisteria
        Color(0xFF2C3E50), // Midnight Blue
        Color(0xFFF39C12), // Orange
        Color(0xFFD35400), // Pumpkin
        Color(0xFFC0392B), // Pomegranate
        Color(0xFF27AE60), // Nephritis
        Color(0xFF2980B9), // Belize Hole (repeat for more users)
        Color(0xFF6C5CE7), // Bright Purple
        Color(0xFF00B894), // Greenish
        Color(0xFF00CEC9)  // Cyan
    )
    // Map user to color
    fun getUserColor(user: String): Color {
        val index = abs(user.hashCode()) % userColors.size
        return userColors[index]
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        // App Bar at the top with centered text and gradient background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                        colors = listOf(
                            Purple40,
                            Pink40
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onBackClick() },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = roomId,
                        color = Color.White,
                        style = androidx.compose.material3.MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 0.85f, fill = true),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            reverseLayout = true
        ) {
            items(messages) { message ->
                val isCurrentUser = message[Constants.IS_CURRENT_USER] as Boolean
                val sentBy = message["sent_by_name"]?.toString() ?: message[Constants.SENT_BY]?.toString() ?: "Unknown"
                val sentOn = (message[Constants.SENT_ON] as? Number)?.toLong() ?: 0L
                val userColor = getUserColor(sentBy)

                SingleMessage(
                    message = message[Constants.MESSAGE].toString(),
                    isCurrentUser = isCurrentUser,
                    sentBy = sentBy,
                    sentOn = sentOn,
                    userColor = userColor
                )
            }
        }
        OutlinedTextField(
            value = message,
            onValueChange = {
                homeViewModel.updateMessage(it)
            },
            label = {
                Text(
                    "Type Your Message"
                )
            },
            maxLines = 1,
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 1.dp)
                .fillMaxWidth()
                .weight(weight = 0.09f, fill = true),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            singleLine = true,
            trailingIcon = {
                val interactionSource = remember { MutableInteractionSource() }
                IconButton(
                    onClick = {
                        homeViewModel.addMessage()
                    },
                    interactionSource = interactionSource
                ) {
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = "Send Button"
                    )
                }
            }
        )
    }
}
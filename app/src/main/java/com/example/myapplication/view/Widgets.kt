package com.example.myapplication.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import com.example.myapplication.view.register.ui.theme.Pink40
import com.example.myapplication.view.register.ui.theme.Purple40

@Composable
fun Title(title: String) {
    Text(
        text = title,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun Buttons(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(50)
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(
            Purple40,
            Pink40
        )
    )

    Box(
        modifier = modifier
            .clip(shape)
            .background(brush = gradientBrush)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Appbar(title: String, action: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(
            Purple40,
            Pink40
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(brush = gradientBrush, shape = shape)
            .padding(horizontal = 12.dp)
            .clip(shape),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back button",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { action() }
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                color = Color.White,
                fontSize = 20.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType,
    visualTransformation: VisualTransformation
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                label
            )
        },
        placeholder = { Text(text = label) },
        maxLines = 1,
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        singleLine = true,
        visualTransformation = visualTransformation,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            placeholderColor = Color.Transparent,
        ),
    )
}

fun getMessageTextColor(backgroundColor: Color): Color {
    // Use black for light backgrounds, white for dark backgrounds
    val luminance = (0.299 * backgroundColor.red + 0.587 * backgroundColor.green + 0.114 * backgroundColor.blue)
    return if (luminance > 0.6) Color.Black else Color.White
}

@Composable
fun SingleMessage(message: String, isCurrentUser: Boolean, sentBy: String, sentOn: Long, userColor: Color) {
    val messageTextColor = getMessageTextColor(userColor)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = userColor
            ),
            modifier = Modifier
                .padding(vertical = 2.dp, horizontal = 4.dp)
                .widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = message,
                    textAlign = if (isCurrentUser) TextAlign.End else TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                    color = messageTextColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
                ) {
                    Text(
                        text = "Sent by: $sentBy",
                        style = MaterialTheme.typography.bodySmall,
                        color = messageTextColor.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = java.text.SimpleDateFormat("hh:mm a, dd MMM", java.util.Locale.getDefault()).format(java.util.Date(sentOn)),
                        style = MaterialTheme.typography.bodySmall,
                        color = messageTextColor.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}
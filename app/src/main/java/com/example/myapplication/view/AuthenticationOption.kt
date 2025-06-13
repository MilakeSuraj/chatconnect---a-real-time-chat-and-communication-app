package com.example.myapplication.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.background
import androidx.compose.foundation.border

@Composable
fun AuthenticationView(register: () -> Unit, login: () -> Unit) {
    var selected by remember { mutableStateOf("Login") }
    MyApplicationTheme {
        Surface(color = Color.White) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    painterResource(id = R.drawable.chatno),
                    contentDescription = "",
                    modifier = Modifier.size(420.dp)
                )
                // Toggle Button Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .height(54.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Register Toggle
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(end = 4.dp)
                            .background(
                                if (selected == "Register") MaterialTheme.colorScheme.primary else Color.White,
                                shape = RoundedCornerShape(topStart = 50.dp, bottomStart = 50.dp)
                            )
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(topStart = 50.dp, bottomStart = 50.dp)
                            )
                            .clickable {
                                selected = "Register"
                                register()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Register",
                            color = if (selected == "Register") Color.White else Color.Black,
                            modifier = Modifier.padding(vertical = 10.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    // Login Toggle
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(start = 4.dp)
                            .background(
                                if (selected == "Login") MaterialTheme.colorScheme.primary else Color.White,
                                shape = RoundedCornerShape(topEnd = 50.dp, bottomEnd = 50.dp)
                            )
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(topEnd = 50.dp, bottomEnd = 50.dp)
                            )
                            .clickable {
                                selected = "Login"
                                login()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Login",
                            color = if (selected == "Login") Color.White else Color.Black,
                            modifier = Modifier.padding(vertical = 10.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
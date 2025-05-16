package com.example.myapplication.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
                Image(painterResource(id = R.drawable.chatno), contentDescription = "",
                    modifier = Modifier.size(250.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            selected = "Register"
                            register()
                        },
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                            containerColor = if (selected == "Register") MaterialTheme.colorScheme.primary else Color.Transparent,
                            contentColor = if (selected == "Register") Color.White else Color.Black
                        )
                    ) {
                        Text("Register")
                    }
                    OutlinedButton(
                        onClick = {
                            selected = "Login"
                            login()
                        },
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                            containerColor = if (selected == "Login") MaterialTheme.colorScheme.primary else Color.Transparent,
                            contentColor = if (selected == "Login") Color.White else Color.Black
                        )
                    ) {
                        Text("Login")
                    }
                }
            }
        }
    }
}
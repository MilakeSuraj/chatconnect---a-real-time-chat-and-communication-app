package com.example.myapplication.view.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.view.Appbar
import com.example.myapplication.view.Buttons
import com.example.myapplication.view.TextFormField
import androidx.compose.runtime.livedata.observeAsState
import kotlinx.coroutines.launch

@Composable
fun RegisterView(
    home: () -> Unit,
    back: () -> Unit,
    registerViewModel: RegisterViewModel = viewModel()
) {
    val email: String by registerViewModel.email.observeAsState("")
    val password: String by registerViewModel.password.observeAsState("")
    val loading: Boolean by registerViewModel.loading.observeAsState(initial = false)
    val errorMessage: String? by registerViewModel.errorMessage.observeAsState(null)

    val username = remember { mutableStateOf("") }
    val confirm = remember { mutableStateOf(TextFieldValue()) }
    val showPass = remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Show snackbar when errorMessage changes
    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrEmpty()) {
            snackbarHostState.showSnackbar(errorMessage!!)
            registerViewModel.clearError()
        }
    }

    fun validateTextFields(): Boolean {
        if (email.isBlank()) {
            registerViewModel.clearError()
            registerViewModel.setError("Please enter your email.")
            return false
        }

        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex())) {
            registerViewModel.clearError()
            registerViewModel.setError("Email is invalid.")
            return false
        }

        if (username.value.isBlank()) {
            registerViewModel.clearError()
            registerViewModel.setError("Please enter your username.")
            return false
        }

        if (password.isBlank()) {
            registerViewModel.clearError()
            registerViewModel.setError("Please enter a password.")
            return false
        }

        if (password.length < 8 || !password.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).+\$".toRegex())) {
            registerViewModel.clearError()
            registerViewModel.setError("Password should be at least 8 characters long and include letters, numbers, and symbols.")
            return false
        }

        if (confirm.value.text != password) {
            registerViewModel.clearError()
            registerViewModel.setError("Password and confirm password do not match.")
            return false
        }

        return true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .verticalScroll(scrollState)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Appbar(title = "Register", action = back)

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.steps),
                        contentDescription = null,
                        modifier = Modifier.size(200.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextFormField(
                        value = email,
                        onValueChange = { registerViewModel.updateEmail(it) },
                        label = "Email",
                        keyboardType = KeyboardType.Email,
                        visualTransformation = VisualTransformation.None
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    TextFormField(
                        value = username.value,
                        onValueChange = { username.value = it },
                        label = "Username",
                        keyboardType = KeyboardType.Text,
                        visualTransformation = VisualTransformation.None
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    TextFormField(
                        value = password,
                        onValueChange = { registerViewModel.updatePassword(it) },
                        label = "Password",
                        keyboardType = KeyboardType.Password,
                        visualTransformation = if (!showPass.value) PasswordVisualTransformation() else VisualTransformation.None
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    TextFormField(
                        value = confirm.value.text,
                        onValueChange = { confirm.value = TextFieldValue(it) },
                        label = "Confirm Password",
                        keyboardType = KeyboardType.Password,
                        visualTransformation = if (!showPass.value) PasswordVisualTransformation() else VisualTransformation.None
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = showPass.value,
                            onCheckedChange = { showPass.value = !showPass.value },
                            colors = CheckboxDefaults.colors(checkedColor = Color(26, 115, 232))
                        )
                        Text(
                            text = "Show password",
                            color = Color.Black,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Buttons(
                        title = "Register",
                        onClick = {
                            if (validateTextFields()) {
                                registerViewModel.registerUser(
                                    home = home,
                                    username = username.value
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        // Snackbar Host
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

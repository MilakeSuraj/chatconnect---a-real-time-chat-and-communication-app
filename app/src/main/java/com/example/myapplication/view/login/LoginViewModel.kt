package com.example.myapplication.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.IllegalArgumentException

class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _email = MutableLiveData("")
    val email: LiveData<String> = _email

    private val _password = MutableLiveData("")
    val password: LiveData<String> = _password

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    fun loginUser(home: () -> Unit) {
        if (_loading.value == false) {
            val email: String = _email.value ?: ""
            val password: String = _password.value ?: ""
            if (email.isBlank() || password.isBlank()) {
                _errorMessage.value = "All fields are mandatory"
                return
            }
            _loading.value = true

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && auth.currentUser != null) {
                        _errorMessage.value = null
                        home()
                    } else {
                        val exception = task.exception
                        _errorMessage.value = when {
                            exception?.message?.contains("no user record") == true -> "No account found with this email."
                            exception?.message?.contains("password is invalid") == true -> "Wrong password."
                            exception?.message?.contains("badly formatted") == true -> "Invalid email format."
                            else -> exception?.localizedMessage ?: "Login failed."
                        }
                    }
                    _loading.value = false
                }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
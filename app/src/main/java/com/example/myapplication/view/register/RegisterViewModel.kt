package com.example.myapplication.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import java.lang.IllegalArgumentException

class RegisterViewModel : ViewModel() {
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

    fun registerUser(home: () -> Unit, username: String) {
        if (_loading.value == false) {
            val email: String = _email.value ?: ""
            val password: String = _password.value ?: ""
            if (email.isBlank() || password.isBlank() || username.isBlank()) {
                _errorMessage.value = "All fields are mandatory"
                return
            }
            _loading.value = true

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid
                        if (uid != null) {
                            val user = hashMapOf(
                                "uid" to uid,
                                "email" to email,
                                "username" to username
                            )
                            com.google.firebase.ktx.Firebase.firestore.collection("users").document(uid).set(user)
                        }
                        _errorMessage.value = null
                        home()
                    } else {
                        val exception = task.exception
                        _errorMessage.value = when {
                            exception?.message?.contains("already in use") == true -> "This email is already registered."
                            exception?.message?.contains("badly formatted") == true -> "Invalid email format."
                            exception?.message?.contains("Password should be at least") == true -> "Password should be at least 6 characters."
                            else -> exception?.localizedMessage ?: "Registration failed."
                        }
                    }
                    _loading.value = false
                }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun setError(message: String) {
        _errorMessage.value = message
    }
}
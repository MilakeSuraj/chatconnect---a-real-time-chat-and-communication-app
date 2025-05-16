package com.example.myapplication.view.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.Constants
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeViewModel(private val roomId: String) : ViewModel() {
    init {
        getMessages()
    }

    private val _message = MutableLiveData("")
    val message: LiveData<String> = _message

    private var _messages = MutableLiveData(emptyList<Map<String, Any>>().toMutableList())
    val messages: LiveData<MutableList<Map<String, Any>>> = _messages


    fun updateMessage(message: String) {
        _message.value = message
    }

    fun addMessage() {
        val message: String =
            _message.value ?: throw IllegalArgumentException("message empty")
        val uid = Firebase.auth.currentUser?.uid
        if (message.isNotEmpty() && uid != null) {
            val db = Firebase.firestore
            db.collection("users").document(uid).get().addOnSuccessListener { document ->
                val username = document.getString("username") ?: "Unknown"
                db.collection("chatRooms").document(roomId).collection("messages").document().set(
                    hashMapOf(
                        Constants.MESSAGE to message,
                        Constants.SENT_BY to uid,
                        "sent_by_name" to username,
                        Constants.SENT_ON to System.currentTimeMillis()
                    )
                ).addOnSuccessListener {
                    _message.value = ""
                }
            }
        }
    }


    private fun getMessages() {
        Firebase.firestore.collection("chatRooms").document(roomId).collection("messages")
            .orderBy(Constants.SENT_ON)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w(Constants.TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                val list = emptyList<Map<String, Any>>().toMutableList()

                if (value != null) {
                    for (doc in value) {
                        val data = doc.data
                        data[Constants.IS_CURRENT_USER] =
                            Firebase.auth.currentUser?.uid.toString() == data[Constants.SENT_BY].toString()

                        list.add(data)
                    }
                }

                updateMessages(list)
            }
    }

    private fun updateMessages(list: MutableList<Map<String, Any>>) {
        _messages.value = list.asReversed()
    }

    fun clearCacheAndLogout() {
        // Sign out from Firebase
        Firebase.auth.signOut()
        // TODO: Add cache clearing logic if you use any local storage (e.g., SharedPreferences, Room, etc.)
        // Navigate to authentication page
        // This should be handled in the Composable via a callback or navigation event
    }

    companion object {
        fun provideFactory(roomId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return HomeViewModel(roomId) as T
                }
            }
    }
}
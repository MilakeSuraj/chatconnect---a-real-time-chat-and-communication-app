package com.example.myapplication.view.chatroom

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ChatRoomListViewModel : ViewModel() {
    private val _chatRooms = MutableStateFlow<List<String>>(emptyList())
    val chatRooms: StateFlow<List<String>> = _chatRooms

    private val db = Firebase.firestore

    init {
        fetchChatRooms()
    }

    fun fetchChatRooms() {
        db.collection("chatRooms").addSnapshotListener { value, _ ->
            val rooms = value?.documents?.mapNotNull { it.id } ?: emptyList()
            _chatRooms.value = rooms
        }
    }

    fun createChatRoom(name: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (name.isBlank()) {
            onError("Room name cannot be empty")
            return
        }
        val trimmed = name.trim()
        db.collection("chatRooms").document(trimmed).get().addOnSuccessListener { doc ->
            if (doc.exists()) {
                onError("Room name already exists")
            } else {
                db.collection("chatRooms").document(trimmed).set(hashMapOf("createdAt" to System.currentTimeMillis()))
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { onError("Failed to create room") }
            }
        }.addOnFailureListener { onError("Failed to check room name") }
    }
}

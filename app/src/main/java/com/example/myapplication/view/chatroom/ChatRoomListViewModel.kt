package com.example.myapplication.view.chatroom

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.google.firebase.auth.ktx.auth

class ChatRoomListViewModel : ViewModel() {
    private val _chatRooms = MutableStateFlow<List<ChatRoom>>(emptyList())
    val chatRooms: StateFlow<List<ChatRoom>> = _chatRooms

    private val db = Firebase.firestore

    init {
        fetchChatRooms()
    }

    fun fetchChatRooms() {
        db.collection("chatRooms").addSnapshotListener { value, _ ->
            val rooms = value?.documents?.mapNotNull { doc ->
                val creatorId = doc.getString("creator") ?: "Unknown"
                var creatorDisplay = creatorId
                if (creatorId != "Unknown") {
                    // Try to get username from users collection
                    // This is a blocking call, but for demo purposes, we use the id as fallback
                    // In production, you should cache usernames or denormalize
                    // Here, we just display the id or 'You' if current user
                    val currentUid = Firebase.auth.currentUser?.uid
                    if (creatorId == currentUid) {
                        creatorDisplay = "You"
                    } else {
                        // Optionally, you could fetch username from users collection here
                        // But for now, just show the UID
                        creatorDisplay = creatorId
                    }
                }
                ChatRoom(doc.id, creatorDisplay)
            } ?: emptyList()
            _chatRooms.value = rooms
        }
    }

    fun createChatRoom(name: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (name.isBlank()) {
            onError("Room name cannot be empty")
            return
        }
        val trimmed = name.trim()
        val currentUser = Firebase.auth.currentUser
        val uid = currentUser?.uid ?: "Unknown"
        if (uid == "Unknown") {
            onError("User not logged in")
            return
        }
        db.collection("users").document(uid).get().addOnSuccessListener { userDoc ->
            val username = userDoc.getString("username") ?: "Unknown"
            db.collection("chatRooms").document(trimmed).get().addOnSuccessListener { doc ->
                if (doc.exists()) {
                    onError("Room name already exists")
                } else {
                    db.collection("chatRooms").document(trimmed).set(
                        hashMapOf(
                            "createdAt" to System.currentTimeMillis(),
                            "creator" to username
                        )
                    )
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { onError("Failed to create room") }
                }
            }.addOnFailureListener { onError("Failed to check room name") }
        }.addOnFailureListener { onError("Failed to fetch user info") }
    }
}

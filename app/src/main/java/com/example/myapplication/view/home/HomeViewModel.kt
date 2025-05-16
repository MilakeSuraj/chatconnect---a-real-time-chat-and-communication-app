package com.example.myapplication.view.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.Constants
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeViewModel : ViewModel() {
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
                db.collection(Constants.MESSAGES).document().set(
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
        Firebase.firestore.collection(Constants.MESSAGES)
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
}
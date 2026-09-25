package com.example.eventmanagement.data.model

import com.google.firebase.Timestamp

data class Event(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val dateTime: Timestamp? = null,
    val location: String = ""
)

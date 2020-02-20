package com.reportedsocks.listofmessages.network.model

import java.util.*

/**
 * data model to parse Retrofit response
 */
data class MessagesApiResponse (
    val count: Int,
    val pageToken: String,
    val messages: List<Message>
)

data class Message (
    val content: String,
    val updated: Date,
    val id: Int,
    val author: Author
)

data class Author (
    val name: String,
    val photoUrl: String
)
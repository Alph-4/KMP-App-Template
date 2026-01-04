package com.jetbrains.kmpapp.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MistralRequest(
    val model: String,
    val messages: List<Message>
)

@Serializable
data class ChatCompletionResponse(
    val id: String,

    // Often "object" in JSON, renamed to avoid keyword conflict
    @SerialName("object")
    val objectType: String,

    // Usually a unix timestamp (Long), check your API docs
    val created: Long,

    val model: String,

    val choices: List<Choice>,

    val usage: Usage
)

@Serializable
data class Choice(
    val index: Int,

    val message: Message,

    @SerialName("finish_reason")
    val finishReason: String? = null // Nullable if the stream isn't finished
)

@Serializable
data class Message(
    val role: String,
    val content: String? = null // Content can be null in some function-calling contexts
)

@Serializable
data class Usage(
    @SerialName("prompt_tokens")
    val promptTokens: Int,

    @SerialName("completion_tokens")
    val completionTokens: Int,

    @SerialName("total_tokens")
    val totalTokens: Int
)

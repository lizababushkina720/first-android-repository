package com.example.hw_04.model


sealed class CoroutineException(message: String) : Exception(message) {

    class NetworkException(message: String) : CoroutineException(message)

    class TimeoutException(message: String) : CoroutineException(message)

    class DatabaseException(message: String) : CoroutineException(message)
}

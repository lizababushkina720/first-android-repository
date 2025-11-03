package com.example.hw_02.utils

object Constants {
    const val MIN_PASSWORD_LENGTH = 8
    object Routes {
        const val LOGIN = "login"
        const val NOTES = "notes/{email}"
        const val ADD_NOTE = "add_note"
        fun notesRoute(email: String) = "notes/$email"
    }

    const val ARG_EMAIL = "email"
}

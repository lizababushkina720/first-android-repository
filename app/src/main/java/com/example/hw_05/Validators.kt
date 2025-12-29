package com.example.hw_05


object Validators {

    fun isEmailValid(email: String): Boolean {
        val e = email.trim()
        return e.contains('@') && e.contains('.') && e.length >= 5
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= 4
    }

    fun parseWeightKg(raw: String): Double? {
        return raw.trim().replace(',', '.').toDoubleOrNull()
    }
}

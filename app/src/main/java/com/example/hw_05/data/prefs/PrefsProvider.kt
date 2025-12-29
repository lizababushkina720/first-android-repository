package com.example.hw_05.data.prefs


import android.content.SharedPreferences

object PrefsProvider {
    private var sp: SharedPreferences? = null

    fun provide(sharedPreferences: SharedPreferences) {
        sp = sharedPreferences
    }

    fun get(): SharedPreferences =
        sp ?: error("SharedPreferences is not provided")
}

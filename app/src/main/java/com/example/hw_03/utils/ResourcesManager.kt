package com.example.hw_03.utils


import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.StringRes

class ResourcesManager(private val context: Context) {

    fun getString(@StringRes stringId: Int): String {
        return context.getString(stringId)
    }

    fun getString(@StringRes stringId: Int, vararg args: Any): String {
        return context.getString(stringId, *args)
    }

    fun getColor(@ColorRes colorId: Int): Int {
        return context.getColor(colorId)
    }
}

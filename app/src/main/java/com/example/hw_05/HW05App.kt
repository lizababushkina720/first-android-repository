package com.example.hw_05

import android.app.Application
import com.example.hw_05.data.prefs.PrefsProvider
import com.example.hw_05.di.ServiceLocator

class HW05App : Application() {

    override fun onCreate() {
        super.onCreate()

        val sp = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE)
        PrefsProvider.provide(sp)

        ServiceLocator.initDatabase(appCtx = this)
    }

    private companion object {
        const val SHARED_PREFS_NAME = "inception_sp"
    }
}
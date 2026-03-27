package com.example.hw_01_sem2

import android.app.Application

import com.example.di.ServiceLocator

class HW01Sem2App : Application() {

    override fun onCreate() {
        super.onCreate()

        ServiceLocator.initDatabase(appCtx = this)
    }
}
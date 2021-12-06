package com.example.appcar.util

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApplication : Application() {
    companion object {
        lateinit var INSTANCE: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        Timber.plant(Timber.DebugTree())
        FirebaseApp.initializeApp(this)
    }
}
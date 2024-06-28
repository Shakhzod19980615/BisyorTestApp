package com.example.testapp

import android.app.Application
import com.example.testapp.common.MySettings
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MySettings.initInstance(this)
    }
}
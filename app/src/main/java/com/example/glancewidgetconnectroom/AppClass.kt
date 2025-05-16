package com.example.glancewidgetconnectroom

import android.app.Application
import android.content.Context
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppClass : Application() {
    companion object {
        lateinit var appContext: Context
            private set
    }

    override fun onCreate() {
        CoroutineScope(Dispatchers.Main).launch {
            GlanceWidget().updateAll(this@AppClass)
        }
        appContext = applicationContext
        super.onCreate()
    }
}
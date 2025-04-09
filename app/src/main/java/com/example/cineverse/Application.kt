package com.example.cineverse

import android.app.Application
import com.example.cineverse.api.RetrofitObject

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        RetrofitObject.initCache(applicationContext)
    }

    override fun onTerminate() {
        super.onTerminate()
        RetrofitObject.clearCache()
    }
}
package com.example.myapplication

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SpecialsApp() : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SpecialsApp)

            //Add modules here
            modules(mainModule)
        }
    }
}
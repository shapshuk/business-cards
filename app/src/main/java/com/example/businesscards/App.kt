package com.example.businesscards

import android.app.Application
import com.example.businesscards.di.AppComponent
import com.example.businesscards.di.DaggerAppComponent

class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(applicationContext)
    }
}
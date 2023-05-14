package com.example.businesscards.di.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.businesscards.App
import com.example.businesscards.di.AppComponent

abstract class BaseActivity : AppCompatActivity() {
    lateinit var  component: AppComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component = (applicationContext as App).appComponent
    }
}
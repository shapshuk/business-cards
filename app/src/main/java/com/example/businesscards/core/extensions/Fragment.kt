package com.example.businesscards.core.extensions

import androidx.fragment.app.Fragment
import com.example.businesscards.di.AppComponent

fun Fragment.getAppComponent(): AppComponent =
    requireContext().appComponent
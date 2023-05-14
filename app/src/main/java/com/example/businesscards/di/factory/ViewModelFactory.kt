package com.example.businesscards.di.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.businesscards.ui.cardsmainfragment.CardsMainViewModel
import com.example.businesscards.ui.main.MainActivityViewModel
import com.example.businesscards.ui.signin.SignInViewModel
import com.example.businesscards.ui.splashscreen.SplashScreenViewModel
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
    mainViewModel: Provider<MainActivityViewModel>,
    signInViewModel: Provider<SignInViewModel>,
    splashScreenViewModel: Provider<SplashScreenViewModel>,
    cardsMainViewModel: Provider<CardsMainViewModel>,
) : ViewModelProvider.Factory {

    private val providers = mapOf< Class<*>, Provider<out ViewModel>>(
        MainActivityViewModel::class.java to mainViewModel,
        SignInViewModel::class.java to signInViewModel,
        SplashScreenViewModel::class.java to splashScreenViewModel,
        CardsMainViewModel::class.java to cardsMainViewModel
    )

    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return providers[modelClass]!!.get() as T
    }
}
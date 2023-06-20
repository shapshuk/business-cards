package com.example.businesscards.di.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.businesscards.ui.cardsmainfragment.CardsMainViewModel
import com.example.businesscards.ui.cardsmainfragment.contacts.ContactsViewModel
import com.example.businesscards.ui.cardsmainfragment.mycards.MyCardsViewModel
import com.example.businesscards.ui.createcard.CreateCardViewModel
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
    contactsViewModel: Provider<ContactsViewModel>,
    myCardsViewModel: Provider<MyCardsViewModel>,
    createCardViewModel: Provider<CreateCardViewModel>,
) : ViewModelProvider.Factory {

    private val providers = mapOf< Class<*>, Provider<out ViewModel>>(
        MainActivityViewModel::class.java to mainViewModel,
        SignInViewModel::class.java to signInViewModel,
        SplashScreenViewModel::class.java to splashScreenViewModel,
        CardsMainViewModel::class.java to cardsMainViewModel,
        ContactsViewModel::class.java to contactsViewModel,
        MyCardsViewModel::class.java to myCardsViewModel,
        CreateCardViewModel::class.java to createCardViewModel,
    )

    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return providers[modelClass]!!.get() as T
    }
}
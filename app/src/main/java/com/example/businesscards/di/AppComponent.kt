package com.example.businesscards.di

import android.content.Context
import com.example.businesscards.di.factory.ViewModelFactory
import com.example.businesscards.ui.acceptedcard.AcceptedCardFragment
import com.example.businesscards.ui.cardsmainfragment.CardsMainFragment
import com.example.businesscards.ui.cardsmainfragment.contacts.ContactsFragment
import com.example.businesscards.ui.cardsmainfragment.mycards.MyCardsFragment
import com.example.businesscards.ui.createcard.CreateCardFragment
import com.example.businesscards.ui.dialog.QuestionDialogFragment
import com.example.businesscards.ui.dialog.SuccessDialogFragment
import com.example.businesscards.ui.editcard.EditCardFragment
import com.example.businesscards.ui.main.MainActivity
import com.example.businesscards.ui.sharecard.ShareCardFragment
import com.example.businesscards.ui.signin.SignInFragment
import com.example.businesscards.ui.splashscreen.SplashScreenFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)
    fun inject(fragment: SignInFragment)
    fun inject(fragment: SplashScreenFragment)
    fun inject(fragment: CardsMainFragment)
    fun inject(fragment: MyCardsFragment)
    fun inject(fragment: ContactsFragment)
    fun inject(fragment: ShareCardFragment)
    fun inject(fragment: CreateCardFragment)
    fun inject(fragment: SuccessDialogFragment)
    fun inject(fragment: QuestionDialogFragment)
    fun inject(fragment: AcceptedCardFragment)
    fun inject(fragment: EditCardFragment)

    fun viewModelsFactory(): ViewModelFactory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

}
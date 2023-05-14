package com.example.businesscards.di.modules

import com.example.businesscards.core.ID_TOKEN
import com.example.businesscards.di.utils.CardsReference
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BusinessCardsModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideGso(): GoogleSignInOptions = GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(ID_TOKEN)
        .requestEmail()
        .build()

    @Provides
    @Singleton
    fun provideUsersDatabaseReference(): DatabaseReference =
        Firebase.database.getReference(USERS_KEY)

    @Provides
    @Singleton
    @CardsReference
    fun provideArticlesDatabaseReference(): DatabaseReference =
        Firebase.database.getReference(CARDS_KEY)

    companion object {
        private const val USERS_KEY = "Users"
        private const val CARDS_KEY = "Cards"
//        private const val CATEGORIES_KEY = "Categories"
//        private const val SUBCATEGORIES_KEY = "SubCategories"
    }
}
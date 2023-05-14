package com.example.businesscards.di.modules

import com.example.businesscards.domain.CardsInteractor
import com.example.businesscards.domain.CardsInteractorImpl
import dagger.Binds
import dagger.Module

@Module
interface InteractorModule {

    @Binds
    fun bindCardsInteractor(interactor: CardsInteractorImpl) : CardsInteractor
}
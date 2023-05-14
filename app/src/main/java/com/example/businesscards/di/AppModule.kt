package com.example.businesscards.di

import com.example.businesscards.di.modules.BusinessCardsModule
import com.example.businesscards.di.modules.InteractorModule
import dagger.Module

@Module(
    includes = [
        InteractorModule::class,
        BusinessCardsModule::class
    ]
)
object AppModule
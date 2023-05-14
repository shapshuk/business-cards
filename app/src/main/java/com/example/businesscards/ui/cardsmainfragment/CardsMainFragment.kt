package com.example.businesscards.ui.cardsmainfragment

import android.content.Context
import androidx.fragment.app.viewModels
import com.example.businesscards.core.coreui.BaseFragment
import com.example.businesscards.core.extensions.getAppComponent
import com.example.businesscards.databinding.FragmentCardsMainBinding
import com.example.businesscards.databinding.FragmentSplashScreenBinding
import com.example.businesscards.ui.splashscreen.SplashScreenViewModel

class CardsMainFragment : BaseFragment() {

    private val binding: FragmentCardsMainBinding by lazy {
        FragmentCardsMainBinding.inflate(layoutInflater)
    }
    private val viewModel: CardsMainViewModel by viewModels {
        getAppComponent().viewModelsFactory()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getAppComponent().inject(this)
    }


    override fun initViews() {
        TODO("Not yet implemented")
    }
}
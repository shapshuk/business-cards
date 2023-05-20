package com.example.businesscards.ui.splashscreen

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.businesscards.R
import com.example.businesscards.core.coreui.BaseFragment
import com.example.businesscards.core.extensions.getAppComponent
import com.example.businesscards.data.CardUiModel
import com.example.businesscards.databinding.FragmentSplashScreenBinding
import com.example.businesscards.ui.common.Error
import com.example.businesscards.util.navigate

class SplashScreenFragment : BaseFragment() {

    private val binding: FragmentSplashScreenBinding by lazy {
        FragmentSplashScreenBinding.inflate(layoutInflater)
    }
    private val viewModel: SplashScreenViewModel by viewModels {
        getAppComponent().viewModelsFactory()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("SplashScreen", "SplashScreen attached")
        getAppComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun handleInternetConnection(isAvailable: Boolean) {
        if (isAvailable) {
            viewModel.splashDelay()
            closeErrorBottomSheet()
        } else {
            showErrorBottomSheet(Error.NO_INTERNET_CONNECTION)
        }
    }

    private fun handleListOfDataFromFirebase(listOfDataFromFirebase: MutableList<CardUiModel>) {
        Log.d("SplashScreen", "handling list of data from firebase")
        if (listOfDataFromFirebase.isEmpty()) {
//            navigate(R.id.action_splashScreenFragment_to_listInterestsFragment)
        } else {
            Log.d("Cards", listOfDataFromFirebase.toString())
            navigate(R.id.action_splashScreenFragment_to_cardsMainFragment)
        }
    }

    private fun handleNavigationSignIn() {
        navigate(R.id.action_splashScreenFragment_to_signInFragment)
    }

    private fun handleDataIsFailed() {
        showErrorBottomSheet(Error.ERROR_WITH_REALTIME_DB)
    }

    override fun initViews() {
        Log.d("SplashScreenFragment", "InitView called")
    }

    override fun initObservers() {
        connectivityLiveData.observe(viewLifecycleOwner, ::handleInternetConnection)
        viewModel.isNavigateToViewPagerNeeded.observe(viewLifecycleOwner) {
            handleNavigationSignIn()
        }
        viewModel.listOfDataFromFirebase.observe(viewLifecycleOwner, ::handleListOfDataFromFirebase)
        viewModel.isDataFailed.observe(viewLifecycleOwner) {
            handleDataIsFailed()
        }
    }
}
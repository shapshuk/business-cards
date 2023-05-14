package com.example.businesscards.core.coreui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.businesscards.ui.common.ErrorBottomSheetFragment
import com.example.businesscards.ui.common.Error
import com.example.businesscards.util.ConnectivityLiveData
import javax.inject.Inject

abstract class BaseFragment: Fragment() {

    @Inject
    lateinit var connectivityLiveData: ConnectivityLiveData

    private var errorBottomSheet: ErrorBottomSheetFragment? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObservers()
    }

    private fun isBottomSheetVisible() = errorBottomSheet?.isVisible

    fun showErrorBottomSheet(error: Error) {
        errorBottomSheet = ErrorBottomSheetFragment.newInstance(error)
        errorBottomSheet?.show(parentFragmentManager, null)
    }

    fun closeErrorBottomSheet() {
        if (isBottomSheetVisible() == true) {
            errorBottomSheet?.dismiss()
        }
    }

    private fun handleNoInternetConnection() {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager.activeNetwork == null) {
            showErrorBottomSheet(Error.NO_INTERNET_CONNECTION)
        }
    }

    open fun handleInternetConnection(isAvailable: Boolean) {
        if (isAvailable) {
            closeErrorBottomSheet()
        } else {
            showErrorBottomSheet(Error.NO_INTERNET_CONNECTION)
        }
    }

    abstract fun initViews()

    open fun initObservers() { }
}
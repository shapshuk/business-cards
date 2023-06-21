package com.example.businesscards.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.businesscards.R
import com.example.businesscards.core.extensions.getAppComponent
import com.example.businesscards.databinding.FragmentSuccessDialogBinding

class SuccessDialogFragment : DialogFragment(R.layout.fragment_success_dialog) {

    private val binding : FragmentSuccessDialogBinding by lazy {
        FragmentSuccessDialogBinding.inflate(layoutInflater)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getAppComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogFragmentStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

    fun show(fragmentManager: FragmentManager) {
        (fragmentManager
            .findFragmentByTag(SuccessDialogFragment::class.simpleName) as? DialogFragment)
            ?.let { it.showsDialog = true }
            ?: show(fragmentManager, SuccessDialogFragment::class.simpleName)
    }
}
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
import com.example.businesscards.core.utils.ConfirmationDialogListener
import com.example.businesscards.databinding.FragmentQuestionDialogBinding

class QuestionDialogFragment(private val cardId: String) : DialogFragment(R.layout.fragment_question_dialog) {

    private var confirmationDialogListener: ConfirmationDialogListener? = null

    private val binding : FragmentQuestionDialogBinding by lazy {
        FragmentQuestionDialogBinding.inflate(layoutInflater)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getAppComponent().inject(this)
        try {
            confirmationDialogListener = parentFragment as ConfirmationDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("Parent fragment must implement ConfirmationDialogListener")
        }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(binding) {
            continueButton.setOnClickListener {
                confirmationDialogListener?.onPositiveButtonClicked(cardId)
                dismiss()
            }
            cancelButton.setOnClickListener {
                dismiss()
            }
        }
    }

    fun show(fragmentManager: FragmentManager) {
        (fragmentManager
            .findFragmentByTag(SuccessDialogFragment::class.simpleName) as? DialogFragment)
            ?.let { it.showsDialog = true }
            ?: show(fragmentManager, SuccessDialogFragment::class.simpleName)
    }
}
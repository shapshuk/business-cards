package com.example.businesscards.ui.common

import android.os.Bundle
import com.example.businesscards.R
import com.example.businesscards.databinding.FragmentErrorBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ErrorBottomSheetFragment : BottomSheetDialogFragment() {

    private val binding: FragmentErrorBottomSheetBinding by lazy {
        FragmentErrorBottomSheetBinding.inflate(layoutInflater)
    }

    private fun chooseCurrentState(state: Error) {
        binding.run {
            when (state) {
                Error.NO_INTERNET_CONNECTION -> {
                    descriptionOfErrorTextView.setText(R.string.you_have_no_internet_connection)
                }
                Error.SERVER_IS_NOT_RESPONDING -> {
                    descriptionOfErrorTextView.setText(R.string.you_have_error_with_server)
                }
                Error.TIME_OUT -> {
                    descriptionOfErrorTextView.setText(R.string.your_waiting_time_is_up)
                }
                Error.ERROR_WITH_AUTHENTICATION -> {
                    descriptionOfErrorTextView.setText(R.string.can_not_upload_data_to_firebase)
                }
                Error.ERROR_WITH_REALTIME_DB -> {
                    descriptionOfErrorTextView.setText(R.string.can_not_upload_data_to_firebase)
                }
                Error.NOTHING_FOUND -> {
                    descriptionOfErrorTextView.setText(R.string.nothing_found)
                }
            }
            cancelTextView.setOnClickListener {
                dismiss()
            }
        }
    }

    companion object {

        private const val ERROR_KEY = "errorKey"

        fun newInstance(error: Error) = ErrorBottomSheetFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ERROR_KEY, error)
            }
        }
    }
}
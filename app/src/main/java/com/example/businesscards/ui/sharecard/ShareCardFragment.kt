package com.example.businesscards.ui.sharecard

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NdefRecord.createMime
import android.nfc.NfcAdapter
import android.nfc.NfcEvent
import android.nfc.tech.Ndef
import android.nfc.tech.NfcF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.businesscards.R
import com.example.businesscards.core.coreui.BaseFragment
import com.example.businesscards.core.extensions.getAppComponent
import com.example.businesscards.databinding.FragmentShareCardBinding
import com.example.businesscards.ui.common.Error

class ShareCardFragment : BaseFragment() {

    private val binding : FragmentShareCardBinding by lazy {
        FragmentShareCardBinding.inflate(layoutInflater)
    }
    val navArgs: ShareCardFragmentArgs by navArgs()

    private val nfcAdapter: NfcAdapter by lazy {
        NfcAdapter.getDefaultAdapter(requireContext())
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        getAppComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun initViews() {
        with(binding) {
            userName.text = navArgs.cardToShare.userName
            email.text = navArgs.cardToShare.email
            phoneNumber.text = navArgs.cardToShare.phoneNumber
            jobPosition.text = navArgs.cardToShare.jobPosition

            Glide.with(requireContext())
                .load(navArgs.cardToShare.imageUrl)
                .placeholder(R.drawable.ic_account_circle_orange)
                .circleCrop()
                .into(userImage)
        }

        if (nfcAdapter == null) {
            Toast.makeText(requireContext(), "NFC is not available", Toast.LENGTH_LONG).show()
//            finish()
            return
        }
    }


    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        val techList = arrayOf(arrayOf(NfcF::class.java.name))
        nfcAdapter.enableForegroundDispatch(
            requireActivity(),
            PendingIntent.getActivity(
                requireContext(),
                0,
                Intent(requireContext(), requireActivity().javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                0
            ),
            arrayOf(intentFilter),
            techList
        )
    }
}
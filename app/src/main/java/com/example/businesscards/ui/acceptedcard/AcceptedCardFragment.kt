package com.example.businesscards.ui.acceptedcard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.businesscards.core.coreui.BaseFragment
import com.example.businesscards.core.extensions.getAppComponent
import com.example.businesscards.databinding.FragmentAcceptedCardBinding

class AcceptedCardFragment : BaseFragment() {

    private val binding: FragmentAcceptedCardBinding by lazy {
        FragmentAcceptedCardBinding.inflate(layoutInflater)
    }
    private val navArgs: AcceptedCardFragmentArgs by navArgs()
    private val navController: NavController by lazy { findNavController() }

    private val viewModel : AcceptedCardViewModel by viewModels {
        getAppComponent().viewModelsFactory()
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

            com.bumptech.glide.Glide.with(requireContext())
                .load(navArgs.cardToShare.imageUrl)
                .placeholder(com.example.businesscards.R.drawable.ic_account_circle_orange)
                .circleCrop()
                .into(userImage)
        }

        viewModel.saveAcceptedCard("elon_musk_card_id")

        binding.returnToMainScreen.setOnClickListener {
            navController.navigate(AcceptedCardFragmentDirections.actionAcceptedCardFragmentToCardsMainFragment())
        }
    }

}
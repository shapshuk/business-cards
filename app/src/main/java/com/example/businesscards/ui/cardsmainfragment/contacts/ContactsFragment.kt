package com.example.businesscards.ui.cardsmainfragment.contacts

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.businesscards.core.coreui.BaseFragment
import com.example.businesscards.core.coreui.gone
import com.example.businesscards.core.coreui.overlappingrecyclerview.OverlappingAdapter
import com.example.businesscards.core.coreui.overlappingrecyclerview.OverlappingItemDecoration
import com.example.businesscards.core.coreui.visible
import com.example.businesscards.core.extensions.getAppComponent
import com.example.businesscards.core.utils.ConfirmationDialogListener
import com.example.businesscards.data.CardUiModel
import com.example.businesscards.databinding.FragmentContactsBinding
import com.example.businesscards.ui.dialog.QuestionDialogFragment

class ContactsFragment : BaseFragment(), ConfirmationDialogListener {

    private val binding : FragmentContactsBinding by lazy {
        FragmentContactsBinding.inflate(layoutInflater)
    }
    val viewModel: ContactsViewModel by viewModels {
        getAppComponent().viewModelsFactory()
    }

    private var selectedPosition : Int? = null

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
        val cardList = listOf<CardUiModel>()

        val recyclerView = binding.recyclerview
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(OverlappingItemDecoration { selectedPosition })
            adapter = OverlappingAdapter(cardList.toMutableList(),
                { position ->
                    selectedPosition = position
                    recyclerView.invalidateItemDecorations()
                    adapter!!.notifyItemChanged(position)
                },

                // NFC action
                { _ ->
//                    Toast.makeText(requireContext(), "Card was clicked", Toast.LENGTH_SHORT).show()
                },
                // Delete action
                { cardItem ->
                    QuestionDialogFragment(cardItem.cardId).show(childFragmentManager)
                },
                // Phone call action
                { cardItem ->
//                    Toast.makeText(requireContext(), "Card was clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", cardItem.phoneNumber, null))
                    startActivity(intent)
                },
                // send Email
                { cardItem ->
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:${cardItem.email}")
                        putExtra(Intent.EXTRA_SUBJECT, "email from Business Cards")
                    }
                    if (intent.resolveActivity(requireActivity().packageManager) != null) {
                        startActivity(intent)
                    } else Log.d("Send email", "Something went wrong")
                }
            )
        }
    }

    override fun initObservers() {
        viewModel.cardsItemFromServer.observe(viewLifecycleOwner) { cards ->
            gone(binding.progressBar)
            if (cards.isEmpty()) {
                visible(binding.noCardsTextView)
            } else {
                (binding.recyclerview.adapter as OverlappingAdapter).setItems(cards)
            }
        }
    }

    override fun onPositiveButtonClicked(cardId: String) {
        viewModel.deleteCard(cardId)
        (binding.recyclerview.adapter as OverlappingAdapter).removeItem(cardId)
        Log.d("ContactsFragment", cardId)
//        Toast.makeText(requireContext(), cardId, Toast.LENGTH_SHORT).show()
    }

    override fun onNegativeButtonClicked() {
        TODO("Not yet implemented")
    }
}
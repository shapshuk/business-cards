package com.example.businesscards.ui.cardsmainfragment.contacts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.businesscards.core.coreui.BaseFragment
import com.example.businesscards.core.coreui.overlappingrecyclerview.OverlappingAdapter
import com.example.businesscards.core.coreui.overlappingrecyclerview.OverlappingItemDecoration
import com.example.businesscards.core.extensions.getAppComponent
import com.example.businesscards.data.CardUiModel
import com.example.businesscards.databinding.FragmentContactsBinding

class ContactsFragment : BaseFragment() {

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
                { _ ->
                    Toast.makeText(requireContext(), "Card was clicked", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    override fun initObservers() {
        viewModel.cardsItemFromServer.observe(viewLifecycleOwner) { cards ->
            (binding.recyclerview.adapter as OverlappingAdapter).setItems(cards)
        }
    }
}
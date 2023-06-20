package com.example.businesscards.ui.cardsmainfragment.mycards

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.businesscards.R
import com.example.businesscards.core.coreui.BaseFragment
import com.example.businesscards.core.coreui.overlappingrecyclerview.OverlappingAdapter
import com.example.businesscards.core.coreui.overlappingrecyclerview.OverlappingItemDecoration
import com.example.businesscards.core.extensions.getAppComponent
import com.example.businesscards.data.CardUiModel
import com.example.businesscards.databinding.FragmentMyCardsBinding
import com.example.businesscards.ui.cardsmainfragment.CardsMainFragment
import com.example.businesscards.ui.cardsmainfragment.CardsMainFragmentDirections

class MyCardsFragment : BaseFragment() {

    private val binding : FragmentMyCardsBinding by lazy {
        FragmentMyCardsBinding.inflate(layoutInflater)
    }
    val viewModel: MyCardsViewModel by viewModels {
        getAppComponent().viewModelsFactory()
    }
    private val navController: NavController by lazy { findNavController() }

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
            adapter = OverlappingAdapter(
                cardList.toMutableList(),
                { position ->
                    selectedPosition = position
                    recyclerView.invalidateItemDecorations()
                    adapter!!.notifyItemChanged(position)
                },
                { cardItem ->
                    navController.navigate(
                        CardsMainFragmentDirections
                            .actionCardsMainFragmentToShareCardFragment(cardItem)
                    )
                }
            )
        }
    }

    override fun initObservers() {
        viewModel.personalCardsFromServer.observe(viewLifecycleOwner) { cards ->
            (binding.recyclerview.adapter as OverlappingAdapter).setItems(cards)
        }
    }
}

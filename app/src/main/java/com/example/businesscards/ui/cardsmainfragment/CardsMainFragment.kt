package com.example.businesscards.ui.cardsmainfragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.businesscards.core.coreui.BaseFragment
import com.example.businesscards.core.coreui.overlappingrecyclerview.OverlappingAdapter
import com.example.businesscards.core.coreui.overlappingrecyclerview.OverlappingItemDecoration
import com.example.businesscards.core.extensions.getAppComponent
import com.example.businesscards.data.CardUiModel
import com.example.businesscards.databinding.FragmentCardsMainBinding
import com.example.businesscards.databinding.FragmentSplashScreenBinding
import com.example.businesscards.ui.common.Error
import com.example.businesscards.ui.splashscreen.SplashScreenViewModel

class CardsMainFragment : BaseFragment() {

    private val binding: FragmentCardsMainBinding by lazy {
        FragmentCardsMainBinding.inflate(layoutInflater)
    }
    private val viewModel: CardsMainViewModel by viewModels {
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
        val cardsList = listOf(
            CardUiModel("Elon Musk", "elonmusk@gmail.com", "", "+712374273"),
            CardUiModel("Jeff Bezos", "jeffbezos@gmail.com", "", "+718764273"),
            CardUiModel("Andrew Shapshuk", "andrewshapshuk@gmail.com", "", "+375333808019"),
            CardUiModel("Andrew Shapshuk", "andrewshapshuk@gmail.com", "", "+375333808019"),
            CardUiModel("Andrew Shapshuk", "andrewshapshuk@gmail.com", "", "+375333808019"),
            CardUiModel("Andrew Shapshuk", "andrewshapshuk@gmail.com", "", "+375333808019"),
            CardUiModel("Andrew Shapshuk", "andrewshapshuk@gmail.com", "", "+375333808019"),
            CardUiModel("Andrew Shapshuk", "andrewshapshuk@gmail.com", "", "+375333808019"),
            CardUiModel("Andrew Shapshuk", "andrewshapshuk@gmail.com", "", "+375333808019"),
        )

        val recyclerView = binding.recyclerview
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(OverlappingItemDecoration { selectedPosition })
            adapter = OverlappingAdapter(cardsList.toMutableList()) { position ->
                selectedPosition = position
                recyclerView.invalidateItemDecorations()
                adapter!!.notifyItemChanged(position)
            }
        }
    }

    override fun initObservers() {
        viewModel.cardsItemFromServer.observe(viewLifecycleOwner) { cards ->
            (binding.recyclerview.adapter as OverlappingAdapter).setItems(cards)
        }
        viewModel.isDataFailed.observe(viewLifecycleOwner) {
            showErrorBottomSheet(Error.ERROR_WITH_REALTIME_DB)
        }
    }
}
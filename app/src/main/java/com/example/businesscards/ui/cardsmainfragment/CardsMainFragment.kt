package com.example.businesscards.ui.cardsmainfragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.businesscards.core.coreui.BaseFragment
import com.example.businesscards.core.coreui.overlappingrecyclerview.OverlappingAdapter
import com.example.businesscards.core.coreui.overlappingrecyclerview.OverlappingItemDecoration
import com.example.businesscards.core.extensions.getAppComponent
import com.example.businesscards.data.CardUiModel
import com.example.businesscards.databinding.FragmentCardsMainBinding
import com.example.businesscards.databinding.FragmentSplashScreenBinding
import com.example.businesscards.ui.cardsmainfragment.contacts.ContactsFragment
import com.example.businesscards.ui.cardsmainfragment.mycards.MyCardsFragment
import com.example.businesscards.ui.common.Error
import com.example.businesscards.ui.splashscreen.SplashScreenViewModel

class CardsMainFragment : BaseFragment() {

    private val binding: FragmentCardsMainBinding by lazy {
        FragmentCardsMainBinding.inflate(layoutInflater)
    }
    val viewModel: CardsMainViewModel by viewModels {
        getAppComponent().viewModelsFactory()
    }
    private val navController: NavController by lazy { findNavController() }
    private lateinit var viewPager: ViewPager2


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
        viewPager = binding.viewPager
        viewPager.isSaveEnabled = false
        val pagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        viewPager.adapter = pagerAdapter

        with(pagerAdapter) {
            addFragment(ContactsFragment())
            addFragment(MyCardsFragment())
        }
    }

    override fun initObservers() {
//        viewModel.cardsItemFromServer.observe(viewLifecycleOwner) { cards ->
//            (binding.recyclerview.adapter as OverlappingAdapter).setItems(cards)
//        }
//        viewModel.isDataFailed.observe(viewLifecycleOwner) {
//            showErrorBottomSheet(Error.ERROR_WITH_REALTIME_DB)
//        }
    }
}

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragmentList: ArrayList<Fragment> = ArrayList()

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }
}
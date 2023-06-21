package com.example.businesscards.ui.cardsmainfragment

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.businesscards.R
import com.example.businesscards.core.coreui.BaseFragment
import com.example.businesscards.core.extensions.getAppComponent
import com.example.businesscards.databinding.FragmentCardsMainBinding
import com.example.businesscards.ui.cardsmainfragment.contacts.ContactsFragment
import com.example.businesscards.ui.cardsmainfragment.mycards.MyCardsFragment
import com.example.businesscards.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.toolbar.inflateMenu(R.menu.menu_main)
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
            addFragment(MyCardsFragment())
            addFragment(ContactsFragment())
        }

        binding.addCardButton.setOnClickListener {
            navController.navigate(R.id.action_cardsMainFragment_to_createCardFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_log_out -> {
                    FirebaseAuth.getInstance().signOut()
                    navController.popBackStack(R.id.signInFragment, true)
                    navController.navigate(R.id.signInFragment)
                    true
                }
                else -> false
            }
        }
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
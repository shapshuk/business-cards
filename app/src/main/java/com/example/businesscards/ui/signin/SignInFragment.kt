package com.example.businesscards.ui.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.businesscards.R
import com.example.businesscards.core.ID_TOKEN
import com.example.businesscards.core.coreui.BaseFragment
import com.example.businesscards.core.extensions.getAppComponent
import com.example.businesscards.data.CardUiModel
import com.example.businesscards.databinding.FragmentSignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.example.businesscards.ui.common.Error
import com.example.businesscards.util.navigate
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignInFragment @Inject constructor(
//    private val gso: GoogleSignInOptions
) : BaseFragment() {

    private val gso : GoogleSignInOptions by lazy {
        GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(ID_TOKEN)
        .requestEmail()
        .build()
    }

    private lateinit var launcher: ActivityResultLauncher<Intent>
    private val viewModel : SignInViewModel by viewModels {
        getAppComponent().viewModelsFactory()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("SignIn", "SignIn attached")
        getAppComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    private val binding: FragmentSignInBinding by lazy {
        FragmentSignInBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        registerUser()

        handleOnBackPressed()

        with(binding) {
            googleSignInButton.setOnClickListener {
                signInWithGoogle()
            }
        }
    }

    private fun handleOnBackPressed() {
        activity
            ?.onBackPressedDispatcher
            ?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.moveTaskToBack(true)
                    activity?.finish()
                }
            })
    }

    private fun handleDataIsFailed() {
        showErrorBottomSheet(Error.ERROR_WITH_REALTIME_DB)
    }
    private fun handleNavigationToToBottomSheet() {
        showErrorBottomSheet(Error.ERROR_WITH_AUTHENTICATION)
    }

    private fun handleListOfDataFromFirebase(listOfDataFromFirebase: List<CardUiModel>) {
        if (listOfDataFromFirebase.isEmpty()) {
            Log.d("SignInFragment", "listOfData is empty")
//            navigate(R.id.action_signInFragment_to_listInterestsFragment)
            navigate(R.id.action_signInFragment_to_cardsMainFragment)
        } else {
            navigate(R.id.action_signInFragment_to_cardsMainFragment)
        }
    }

    override fun initObservers() {
        with(viewModel) {
            isNavigateToBottomSheetNeeded.observe(viewLifecycleOwner) {
                handleNavigationToToBottomSheet()
            }
            listOfDataFromFirebase.observe(viewLifecycleOwner, ::handleListOfDataFromFirebase)
            isDataFailed.observe(viewLifecycleOwner) {
                handleDataIsFailed()
            }
        }

    }

    private fun registerUser() {
        lifecycleScope.launch {
            launcher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        val account = task.getResult(ApiException::class.java)
                        if (account != null) {
                            Log.d("Auth", account.email.toString())
                            viewModel.firebaseAuthWithGoogle(
                                account.idToken.toString()
                            )
                        }
                    } catch(e: ApiException) {
                        Log.d("ApiException", "User canceled login ${e.message}")
                    }
                }
        }
    }

    private fun signInWithGoogle() {
        val signInClient = GoogleSignIn.getClient(requireActivity(), gso)
        launcher.launch(signInClient.signInIntent)
    }
}
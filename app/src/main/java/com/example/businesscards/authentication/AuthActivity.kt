package com.example.businesscards.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.businesscards.mainscreen.MainActivity
import com.example.businesscards.databinding.ActivityAuthBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider


class AuthActivity : AppCompatActivity() {

    lateinit var binding: ActivityAuthBinding
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.googleSignInButton.setOnClickListener {
            getResult.launch(googleSignInClient.signInIntent)
        }

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(com.example.businesscards.R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

//        authViewModel.signInWithGoogle(googleAuthCredential = )
        authViewModel.authenticatedUser.observe(this) { user ->
            Toast.makeText(this, user?.email, Toast.LENGTH_SHORT).show()
            Log.d("AuthObserver", "observer result: $user")
            if (user?.isAuthenticated == true) {
                startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                finish()
            }
        }



    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { it ->
            if (it.resultCode == RESULT_OK) {
                Log.d("AuthActivity", "onActivityResult: Google SignIn intent result")
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {
                    val googleSignInAccount = task.getResult(ApiException::class.java)
                    googleSignInAccount?.let { getGoogleAuthCredential(it) }
                }
                catch(e: Exception) {
                    Log.d("AuthActivity", "onActivityResult: ${e.message}")
                }
            }
        }

    private fun getGoogleAuthCredential(googleSignInAccount: GoogleSignInAccount) {
        val googleTokenId = googleSignInAccount.idToken
        val googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
        authViewModel.signInWithGoogle(googleAuthCredential)

//        authViewModel.authenticatedUserLiveData?.observe(this) { user ->
//            Toast.makeText(this, user.email, Toast.LENGTH_SHORT).show()
//
//            if (user.isAuthenticated) {
//                startActivity(Intent(this@AuthActivity, MainActivity::class.java))
//                finish()
//            }
//        }
    }
}
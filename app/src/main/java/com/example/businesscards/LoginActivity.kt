package com.example.businesscards

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.businesscards.databinding.ActivityLoginBinding
import com.example.businesscards.mainscreen.MainActivity
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private companion object {
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }

    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.currentUser.observe(this) {
            Toast.makeText(this, it.displayName, Toast.LENGTH_SHORT).show()

            it?.let {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }

        val application = requireNotNull(this).application

        val factory = LoginViewModelFactory(application, object : OnLoginStartedListener {
            override fun onLoginStarted(client: GoogleSignInClient?) {
                getResult.launch(client?.signInIntent)
            }
        })

        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]



        binding.googleSignInButton.setOnClickListener {
            viewModel.signIn()
        }
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { it ->
            if (it.resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: Google SignIn intent result")
                val accountTask = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {
                    val account = accountTask.getResult(ApiException::class.java)
                    viewModel.firebaseAuthWithGoogle(account.idToken!!)
                }
                catch(e: Exception) {
                    Log.d(TAG, "onActivityResult: ${e.message}")
                }
            }
        }





}
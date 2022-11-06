package com.example.businesscards.mainscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.businesscards.authentication.AuthActivity
import com.example.businesscards.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        firebaseAuth = FirebaseAuth.getInstance()
//        checkUser()

        binding.email.text = firebaseAuth.currentUser?.email

        binding.logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this@MainActivity, AuthActivity::class.java))
            finish()
        }
    }


//    private fun checkUser() {
//        val firebaseUser = firebaseAuth.currentUser
//        if (firebaseUser == null) {
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
//        } else {
//            val email = firebaseUser.email
//            binding.email.text = email
//        }
//    }
}
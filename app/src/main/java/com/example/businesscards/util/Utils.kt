package com.example.businesscards.util

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth

fun getUid(firebaseAuth: FirebaseAuth): String {
    return if (!firebaseAuth.uid.isNullOrEmpty()) {
        firebaseAuth.uid!!
    } else {
        ""
    }
}

fun Fragment.navigate(id: Int, bundle: Bundle? = null) =
    NavHostFragment.findNavController(this).navigate(id, bundle)
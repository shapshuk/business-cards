package com.example.businesscards.util

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import java.io.Serializable

fun getUid(firebaseAuth: FirebaseAuth): String {
    return if (!firebaseAuth.uid.isNullOrEmpty()) {
        firebaseAuth.uid!!
    } else {
        ""
    }
}
fun Fragment.navigate(id: Int, bundle: Bundle? = null) =
    NavHostFragment.findNavController(this).navigate(id, bundle)

inline fun <reified T : Serializable> Bundle.getSerializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}
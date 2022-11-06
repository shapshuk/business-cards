package com.example.businesscards

import com.google.firebase.firestore.Exclude
import java.io.Serializable


//@Serializable
class User(var uid: String?, var name: String?, var email: String?) {
    var isAuthenticated = false
    var isNew = false
    var isCreated = false
}
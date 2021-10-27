package com.example.unomemo.data

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class Source(context: Context,name:String,options: FirebaseOptions) :FirebaseApp(context,name,options) {

    fun storage():FirebaseFirestore {
        return FirebaseFirestore.getInstance(this)
    }

    fun firebaseAuth():FirebaseAuth {
        return FirebaseAuth.getInstance(this)
    }
}
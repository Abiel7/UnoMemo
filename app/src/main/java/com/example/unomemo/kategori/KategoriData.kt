package com.example.unomemo.kategori

import android.widget.Toast
import com.example.unomemo.R
import com.example.unomemo.bruker.Bruker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class KategoriData {
    companion object{

        @JvmStatic

        var kategoribilde: Array<Int> = arrayOf(
            R.drawable.norge,
            R.drawable.dyr,
            R.drawable.bil,
            R.drawable.mat
        )
        var kategorinavn: Array<String> = arrayOf(
            "Flagg",
            "Dyr",
            "Bil",
            "Mat"
        )
    }
}
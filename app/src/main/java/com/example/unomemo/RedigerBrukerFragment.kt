package com.example.unomemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.unomemo.bruker.Bruker
import com.example.unomemo.databinding.FragmentRedigerBrukerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class RedigerBrukerFragment : Fragment() {
    val auth = FirebaseAuth.getInstance()

    private val brukerDocRef = Firebase.firestore.collection("user")
    lateinit var et_rediger_brukernavn: EditText
    lateinit var btn_lagre_brukernavn: Button
    lateinit var tv_rediger_brukernavn: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val redigerBrukerBinding = DataBindingUtil.inflate<FragmentRedigerBrukerBinding>(
            inflater,
            R.layout.fragment_rediger_bruker,
            container,
            false
        )
        et_rediger_brukernavn = redigerBrukerBinding.etRedigerBruker
        btn_lagre_brukernavn = redigerBrukerBinding.btnLagreBrukernavn
        tv_rediger_brukernavn = redigerBrukerBinding.tvRedigerBruker
        btn_lagre_brukernavn.setOnClickListener {
            val gammelBruker = getBruker()
            val nyBrukerMap = getNyttBrukerEmail()
            updateBruker(gammelBruker, nyBrukerMap)
            tv_rediger_brukernavn.text = et_rediger_brukernavn.text
        }
        return redigerBrukerBinding.root
    }

    private fun getBruker(): Bruker {
        val username = et_rediger_brukernavn.text.toString()
        val email = getBrukerEmail()
        val link = "link"

        return Bruker(email, username, link)
    }

    private fun getNyttBrukerEmail(): Map<String, Any> {
        val navn = et_rediger_brukernavn.text.toString()
        val map = mutableMapOf<String, Any>()
        val db = FirebaseFirestore.getInstance()
        var email = ""
        db.collection("user")
            .get()
            .addOnSuccessListener { result ->
                var bruker = auth.currentUser
                if (bruker != null) {
                    for (document in result) {
                        if (bruker.email.toString() == document.data["email"].toString()) {
                            email = document.data["email"].toString()
                            map["email"] = email
                        }
                    }
                }
            }
        if (navn.isNotEmpty()) {
            map["email"] = email
            map["username"] = navn
            map["url"] = "link"
        }
        return map
    }


    private fun updateBruker(bruker: Bruker, nyBrukerMap: Map<String, Any>) =
        CoroutineScope(Dispatchers.IO).launch {
            val brukerQuery = brukerDocRef
                .whereEqualTo("email", getBrukerEmail())
                .get()
                .await()
            if (brukerQuery.documents.isNotEmpty()) {
                for (doc in brukerQuery) {
                    try {
                        brukerDocRef.document(auth.uid.toString()).set(
                            nyBrukerMap,
                            SetOptions.merge()
                        ).await()
                        Toast.makeText(
                            activity,
                            "Nytt brukernavn lagret $bruker.navn",
                            Toast.LENGTH_LONG
                        ).show()
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            e.message
                        }
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    "No matches in this query"
                }
            }
        }

    fun getBrukerEmail(): String {
        val db = FirebaseFirestore.getInstance()
        var email = ""
        db.collection("user")
            .get()
            .addOnSuccessListener { result ->
                var bruker = auth.currentUser
                if (bruker != null) {
                    for (document in result) {
                        email = document.data["email"].toString()
                    }
                }
            }
        return email
    }
}
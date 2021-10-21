package com.example.unomemo

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.unomemo.bruker.Bruker
import com.example.unomemo.databinding.FragmentRedigerBrukerBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import java.lang.StringBuilder
import kotlinx.coroutines.tasks.await

class RedigerBrukerFragment : Fragment() {
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

        /*
           btn_lagre_brukernavn = redigerBrukerBinding.btnLagreBrukernavn
           et_rediger_brukernavn = redigerBrukerBinding.etRedigerBruker
           var brukernavn: String = et_rediger_brukernavn.text.toString()
           val brukernavn_map: Map<String, String> = mapOf("navn" to brukernavn)
           btn_lagre_brukernavn.setOnClickListener {
               set_bruker_navn(brukernavn_map)
           }*/
/*
        et_rediger_brukernavn =redigerBrukerBinding.etRedigerBruker
        btn_lagre_brukernavn = redigerBrukerBinding.btnLagreBrukernavn
        val map = mutableMapOf<String, Any>()
        val nyttBrukernavn = et_rediger_brukernavn.text.toString()
        tv_rediger_brukernavn = redigerBrukerBinding.tvRedigerBruker
        btn_lagre_brukernavn.setOnClickListener{
            tv_rediger_brukernavn.text = et_rediger_brukernavn.text
            if(et_rediger_brukernavn.text.isNotEmpty()){

            val db = FirebaseFirestore.getInstance()
                Log.d(TAG, "nytt brukernavn ${et_rediger_brukernavn.text}")

                map["navn"] = et_rediger_brukernavn.text

                val brukernavnRef = db.collection("user")
                    .document("loY1De7TwrCzMPXNTrB9")

                //brukernavnRef.update( "navn", et_rediger_brukernavn.text)
                db.runTransaction { transaction ->
                    transaction.update(brukernavnRef, "navn", et_rediger_brukernavn.text)
                }
            }
        }*/
        et_rediger_brukernavn = redigerBrukerBinding.etRedigerBruker
        btn_lagre_brukernavn = redigerBrukerBinding.btnLagreBrukernavn
        tv_rediger_brukernavn = redigerBrukerBinding.tvRedigerBruker
        btn_lagre_brukernavn.setOnClickListener {
            val gammelBruker = getBruker()
            val nyBrukerMap = getNyttBrukernavn()
            updateBruker(gammelBruker, nyBrukerMap)
            tv_rediger_brukernavn.text = et_rediger_brukernavn.text
        }
        return redigerBrukerBinding.root
    }
    //TODO: må hente ut id fra databasen. AKK no er den hardkoda
    private fun getBruker(): Bruker {
        val navn = et_rediger_brukernavn.text.toString()
        val id = "user@gmail.com"
        val link = "link"
        return Bruker(id,navn,link)
    }

    private fun getNyttBrukernavn(): Map<String, Any> {
        val navn = et_rediger_brukernavn.text.toString()
        val map = mutableMapOf<String, Any>()
        if (navn.isNotEmpty()) {
            map["id"] = "user@gmail.com"
            map["navn"] = navn
            map["url"] = "link"
        }
        return map
    }


    //TODO: Denne metoden funker. Eg må bare hente ut id'en til bruker og oppdatere brukernavnet med å finne id'en til brukeren som er logga inn
    private fun updateBruker(bruker: Bruker, nyBrukerMap: Map<String, Any>) =
        CoroutineScope(Dispatchers.IO).launch {
            val brukerQuery = brukerDocRef
                .whereEqualTo("id", bruker.id)
                .get()
                .await()
            if(brukerQuery.documents.isNotEmpty()){
                for(doc in brukerQuery){
                    try {
                        brukerDocRef.document(doc.id).set(
                            nyBrukerMap,
                            SetOptions.merge()
                        ).await()
                        Toast.makeText(activity, "Nytt brukernavn lagret $bruker.navn", Toast.LENGTH_LONG).show()
                    }catch (e: Exception){
                        withContext(Dispatchers.Main){
                            e.message
                        }
                    }
                }
            }
            else{
                withContext(Dispatchers.Main){
                    "No matches in this query"
                }
            }
        }

}
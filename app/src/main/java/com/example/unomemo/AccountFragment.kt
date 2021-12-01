package com.example.unomemo

import android.app.ProgressDialog
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.unomemo.databinding.FragmentAccountBinding
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot


import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.lang.StringBuilder

/*
* Author: Kim Andre Undal
* Denne klassa skal vise fram brukeren sin informasjon.
* Det er brukernavn, email som er registrert og et avatarbilde
*
* Klassa henter ut tre typer Api-er. Det er Firebase Auth, Cloud Firestore og Cloud Storage.
*
*
* */
class AccountFragment : Fragment() {
    lateinit var auth: FirebaseAuth
    lateinit var brukernavnTextView: TextView
    lateinit var brukerAvatarIM: ImageView
    lateinit var emailTextView: TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val accountBinding = DataBindingUtil.inflate<FragmentAccountBinding>(
            inflater,
            R.layout.fragment_account,
            container,
            false
        )
        auth = FirebaseAuth.getInstance()
        val redigerbruker = accountBinding.container.getViewById(R.id.tv_rediger_bruker)
        val brukernavn = accountBinding.cardViewContainer.getViewById(R.id.tv_brukernavn)
        val brukerAvatar = accountBinding.circleCenter.getViewById(R.id.brukerAvatar)
        val user = auth.currentUser
        emailTextView = accountBinding.cardViewContainer.getViewById(R.id.tv_email) as TextView
        brukernavnTextView = brukernavn.findViewById(R.id.tv_brukernavn)
        brukerAvatarIM = brukerAvatar.findViewById(R.id.brukerAvatar)
        val filename = "bilde1"
        val storage = Firebase.storage.reference.child("avatarbilder/$filename")

        val localfile = File.createTempFile("tempimage", "jpg")

        //spinner som viser loadingscreen
        val progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("Fetching image...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        //henter bilde-fila fra storage
        storage.getFile(localfile).addOnSuccessListener {
            if(progressDialog.isShowing)
                progressDialog.dismiss()
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            accountBinding.brukerAvatar.setImageBitmap(bitmap)
        }.addOnFailureListener {
            if(progressDialog.isShowing)
                progressDialog.dismiss()
            Toast.makeText(activity, "Failed to get image from Cloud Storage", Toast.LENGTH_LONG).show()
        }
        redigerbruker.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_accountFragment_to_redigerBrukerFragment)
        }
        getBrukernavn()
        return accountBinding.root
    }

    //Henter brukernavn fra CloudFirestore.
    //Henter ut emailen som brukeren registrerte seg med fra Firebase Auth
    //Kilde for metoden: https://github.com/philipplackner/FirebaseFirestore/blob/Updating-Data/app/src/main/java/com/androiddevs/firebasefirestore/MainActivity.kt
    fun getBrukernavn() {
        val db = FirebaseFirestore.getInstance()
        val builderEmail = StringBuilder()
        val builderBrukerNavn = StringBuilder()
        db.collection("user")
            .get()
            .addOnSuccessListener { result ->
                var bruker = auth.currentUser
                if (bruker != null) {
                    for (document in result) {
                        if (bruker.email.toString() == document.data["id"].toString()) {
                            builderBrukerNavn.append(brukernavnTextView.text).append(" ").append(document.data["navn"].toString())
                            val email = bruker.email.toString()
                            builderEmail.append(emailTextView.text).append(" ").append(email)
                            brukernavnTextView.text = builderBrukerNavn
                            emailTextView.text = builderEmail
                        }
                    }
                }
            }
    }
}

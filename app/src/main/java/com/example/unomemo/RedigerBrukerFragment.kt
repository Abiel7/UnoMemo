package com.example.unomemo

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.example.unomemo.bruker.Bruker

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.util.jar.Manifest

import android.os.Build.*
import android.widget.*
import androidx.core.app.ActivityCompat
import com.example.unomemo.databinding.FragmentRedigerBrukerBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

/*
* Author: Kim Andre Undal
* Denne klassa skal gjør at brukeren kan oppdatere brukernavnet sitt og/eller avatarbildet sitt.
*
* Klassa henter ut tre typer Api-er. Det er Firebase Auth, Cloud Firestore og Cloud Storage.
*
*
* */
class RedigerBrukerFragment : Fragment() {
    val auth = FirebaseAuth.getInstance()
    private var imageUri: Uri? = null
    private val brukerDocRef = Firebase.firestore.collection("user")
    lateinit var et_rediger_brukernavn: EditText
    lateinit var btn_lagre_brukernavn: Button
    lateinit var tv_rediger_brukernavn: TextView
    lateinit var byttAvatarIMG: ImageView
    lateinit var byttAvatarTV: TextView
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
        byttAvatarIMG = redigerBrukerBinding.IWEndreAvatarBilde
        byttAvatarTV = redigerBrukerBinding.tvEndreAvatarBilde
        byttAvatarTV.setOnClickListener{
            updateAvatarBilde()
        }
        //lager filnavnet til bildet som vil bli navnet i Cloud Storage
        val filename = "bilde1"
        var storage = Firebase.storage.reference.child("avatarbilder/$filename")
        val localfile = File.createTempFile("tempimage", "jpg")
        val progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("Fetching image...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        //Dette er for å vise bildet som brukeren har valgt
        //Kilde for metoden: https://www.youtube.com/watch?v=A2-v2VFwLY0&ab_channel=Foxandroid
        storage.getFile(localfile).addOnSuccessListener {
            if(progressDialog.isShowing)
                progressDialog.dismiss()
            //gjør om filen til bitmaps
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            //setter bits til bilde
            redigerBrukerBinding.IWEndreAvatarBilde.setImageBitmap(bitmap)
        }.addOnFailureListener {
            if(progressDialog.isShowing)
                progressDialog.dismiss()
            Toast.makeText(activity, "Failed to get image from Cloud Storage", Toast.LENGTH_LONG).show()
        }

        byttAvatarIMG.setImageURI(imageUri)
        btn_lagre_brukernavn.setOnClickListener {
            val nyBrukerMap = getNyttBrukerMap()
            updateBruker(nyBrukerMap)
            tv_rediger_brukernavn.text = et_rediger_brukernavn.text
            val progressDialog = ProgressDialog(activity)
            progressDialog.setMessage("Uploading file...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            //denne biten skal finne bildet som ligger i Cloud Storage
            val bilde1 = "bilde1"
            val storage = FirebaseStorage.getInstance().getReference("avatarbilder/$bilde1")
            //Tømmer cachen til bildet og gjør klart for å sette på bildet fra Cloud Storage
            redigerBrukerBinding.IWEndreAvatarBilde.setImageURI(null)

            //Dette er biten for å putte eit bilde i Cloud Storage
            //Kilde for metoden: https://www.youtube.com/watch?v=GmpD2DqQYVk&ab_channel=Foxandroid
            storage.putFile(imageUri!!)
                .addOnSuccessListener {
                    //fjerner forrige img uri cache
                    redigerBrukerBinding.IWEndreAvatarBilde.setImageURI(null)
                    Toast.makeText(activity, "Successfully uploaded to cloud storage", Toast.LENGTH_LONG).show()
                    if(progressDialog.isShowing) progressDialog.dismiss()
                }.addOnFailureListener{
                    Toast.makeText(activity, "Failed uploaded to cloud storage", Toast.LENGTH_LONG).show()
                    if(progressDialog.isShowing) progressDialog.dismiss()
                }
        }
        return redigerBrukerBinding.root
    }

    //Koder for ulike handlinger
    companion object{
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1000
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    updateAvatarBilde()
                }
                else{
                    Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            imageUri = data?.data
            byttAvatarIMG.setImageURI(imageUri)
        }
    }

    //Denne metoden er for å hente ut riktig bruker som skal bli oppdatert
    private fun getNyttBrukerMap(): Map<String, Any> {
        val navn = et_rediger_brukernavn.text.toString()
        val map = mutableMapOf<String, Any>()
        val db = FirebaseFirestore.getInstance()
        var id = ""
        db.collection("user")
            .get()
            .addOnSuccessListener { result ->
                var bruker = auth.currentUser
                if (bruker != null) {
                    for (document in result) {
                        //Sjekker id'en i databasen som er emailen, opp mot iden som er registrert på brukeren
                        if (bruker.email.toString() == document.data["id"].toString()) {
                            //Er det ein match går den inn i map som id.
                            id = document.data["id"].toString()
                            map["id"] = id
                        }
                    }
                }
            }
        if (navn.isNotEmpty()) {
            map["id"] = id
            map["navn"] = navn
            map["url"] = "link"
        }
        return map
    }

    //Dette er metoden for å oppdatere brukeren
    //Den bruker coroutines for å gjøre det lettere for hovedtråden
    //Kilde for metoden: https://github.com/philipplackner/FirebaseFirestore/blob/Updating-Data/app/src/main/java/com/androiddevs/firebasefirestore/MainActivity.kt
    private fun updateBruker(nyBrukerMap: Map<String, Any>) =
        CoroutineScope(Dispatchers.IO).launch {
            //Objektet til brukeren
            val bruker = auth.currentUser
            //variabelen som inneholder brukeren sitt dokument i Cloud Firestore
            val brukerQuery = brukerDocRef
                .whereEqualTo("id", bruker?.email.toString())
                .get()
                .await()
            if (brukerQuery.documents.isNotEmpty()) {
                for (doc in brukerQuery) {
                    try {
                        //Dette oppdaterer brukeren sitt brukernavn. For å unngå at dokumentet blir overskrivet
                        //bruker eg SetOptions.merge(). Da blir bare verdien som skal bli oppdatert forandra
                        //og ikke heile dokumentet.
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
    //Dette opner opp galleriet på mobilen og brukeren vil sjå alle bildene sine der.
    private fun updateAvatarBilde(){
        val bildeGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        bildeGallery.type = "image/*"
        startActivityForResult(bildeGallery, IMAGE_PICK_CODE)
    }
}
package com.example.unomemo

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
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
import com.example.unomemo.databinding.FragmentRedigerBrukerBinding
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


class RedigerBrukerFragment : Fragment() {
    val auth = FirebaseAuth.getInstance()

    private val brukerDocRef = Firebase.firestore.collection("user")
    lateinit var et_rediger_brukernavn: EditText
    lateinit var btn_lagre_brukernavn: Button
    lateinit var tv_rediger_brukernavn: TextView
    lateinit var byttAvatarIMG: ImageView
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
            val nyBrukerMap = getNyttBrukerMap()
            //updateBruker(nyBrukerMap)
            if (VERSION.SDK_INT >= VERSION_CODES.M){
                if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else{
                    //permission already granted
                    updateAvatarBilde();
                }
            }
            else{
                //system OS is < Marshmallow
               updateAvatarBilde();
            }
            updateAvatarBilde()
            tv_rediger_brukernavn.text = et_rediger_brukernavn.text
        }
        return redigerBrukerBinding.root
    }

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
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            byttAvatarIMG.setImageURI(data?.data)
        }
    }

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
                        if (bruker.email.toString() == document.data["id"].toString()) {
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

    private fun updateBruker(nyBrukerMap: Map<String, Any>) =
        CoroutineScope(Dispatchers.IO).launch {
            val bruker = auth.currentUser
            val brukerQuery = brukerDocRef
                .whereEqualTo("id", bruker?.email.toString())
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

    private fun updateAvatarBilde(){
        val bildeGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        bildeGallery.type = "image/*"
        startActivity(bildeGallery)
    }
}
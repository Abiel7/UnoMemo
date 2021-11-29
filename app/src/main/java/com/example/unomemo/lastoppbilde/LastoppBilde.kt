package com.example.unomemo.lastoppbilde

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unomemo.databinding.FragmentLastoppBildeFragementBinding

import com.example.unomemo.spilldata.Vanskelighetsgrad
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


class LastoppBilde : Fragment() {
    private lateinit var rvChoseImages :RecyclerView
    private lateinit var gameName :EditText
    private lateinit var btnSave : Button

    private val chosenImages =  mutableListOf<Uri>() // identifies where a particular resource lives //liste som skal inneholde bilder som er valgt fra brukerens telefon

    private var _binding : FragmentLastoppBildeFragementBinding?=null
    private val binding get()= _binding!!

    lateinit var storage: FirebaseStorage

    private val db = Firebase.firestore
    private lateinit var imageLoaderAdapter: VelgBildeAdapter
     var  gameSize : Vanskelighetsgrad  =  Vanskelighetsgrad.ENKEL

    lateinit var  activityResultLauncher: ActivityResultLauncher<Intent>

    var numberOfImages = -1

    interface ImageListener{
        fun onImageClick()
    }

    companion object{
        val TAG =  "LastoppBilde"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentLastoppBildeFragementBinding.inflate(inflater,container,false)


        rvChoseImages =  binding.rvLastopp
        gameName =  binding.giSpillNavn//view.findViewById(R.id.giSpillNavn)
        btnSave =  binding.btnLagre
                // numberOfImages = sumMuchOnGame()
        activityResultLauncher =  registerForActivityResult(
            ActivityResultContracts.
            StartActivityForResult(), object  : ActivityResultCallback<ActivityResult> {
                override fun onActivityResult(result: ActivityResult?) {
                    if (result!!.resultCode!= Activity.RESULT_OK || result.data == null){
                        return
                    }
                    val chosenUri = result.data!!.data
                    val clipData =  result.data!!.clipData
                    if (clipData != null) {
                        for (i in 0  until clipData.itemCount){
                            val item =  clipData.getItemAt(i)
                            if(chosenImages.size < numberOfImages){
                                chosenImages.add(item.uri)
                            }
                        }

                    } else if (chosenUri !=null){
                        chosenImages.add(chosenUri)
                    }
                    imageLoaderAdapter.run { notifyDataSetChanged() }
                    btnSave.isEnabled =  showButton()
                }


            })
        storage = Firebase.storage



        imageLoaderAdapter =  VelgBildeAdapter(requireContext(),chosenImages,gameSize, object: ImageListener{
            override fun onImageClick() {
                imagePickerIntent()
            }

        })
        rvChoseImages.adapter = imageLoaderAdapter
        rvChoseImages.layoutManager =  GridLayoutManager(this.context,gameSize.getBredde())

        return binding.root

    }



    private fun  imagePickerIntent(){
        val intent =  Intent(Intent.ACTION_PICK)
        intent.type =  "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
        startActivity(Intent.createChooser(intent,"Velg Bilder"),)
    }


    private  fun  showButton() :Boolean {
        if(chosenImages.size != numberOfImages){
            return false
        }
        if(gameName.text.isBlank() ){
            return  false
        }
        return  true
    }
    private fun  uploadToDatabse(){
        db.collection("game")
    }



}
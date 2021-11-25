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


class AccountFragment : Fragment() {
    lateinit var auth: FirebaseAuth
    lateinit var brukernavnTextView: TextView
    lateinit var brukerAvatarIM: ImageView

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
        //val brukerAvatar = accountBinding.cardViewContainer.getViewById(R.id.brukerAvatar)
        brukernavnTextView = brukernavn.findViewById(R.id.tv_brukernavn)
        brukerAvatarIM = brukerAvatar.findViewById(R.id.brukerAvatar)
        val filename = "bilde1"
        var storage = Firebase.storage.reference.child("avatarbilder/$filename")

        val localfile = File.createTempFile("tempimage", "jpg")

        //spinner som viser loadingscreen
        val progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("Fetching image...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        //hente fil fra storage
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

    fun getBrukernavn() {

        val db = FirebaseFirestore.getInstance()

        db.collection("user")
            .get()
            .addOnSuccessListener { result ->
                var bruker = auth.currentUser
                if (bruker != null) {
                    for (document in result) {
                        if (bruker.email.toString() == document.data["id"].toString()) {
                            brukernavnTextView.text = document.data["navn"].toString()
                        }
                    }
                }
            }
    }
}

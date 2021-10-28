package com.example.unomemo

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.unomemo.bruker.Bruker
import com.example.unomemo.databinding.FragmentAccountBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.lang.StringBuilder

class AccountFragment : Fragment() {
    lateinit var auth: FirebaseAuth
    lateinit var brukernavnTextView: TextView
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
        val brukernavn = accountBinding.container.getViewById(R.id.tv_brukernavn)
        brukernavnTextView = brukernavn.findViewById(R.id.tv_brukernavn)

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
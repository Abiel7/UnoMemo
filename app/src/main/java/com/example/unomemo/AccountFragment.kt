package com.example.unomemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.unomemo.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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
                        if (bruker.email.toString() == document.data["email"].toString()) {
                            brukernavnTextView.text = document.data["username"].toString()
                        }
                    }
                }
            }
    }
}
package com.example.unomemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unomemo.databinding.FragmentLeaderboardBinding
import com.example.unomemo.leaderboard.Leaderboard
import com.example.unomemo.leaderboard.LeaderboardAdapter
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class LeaderboardFragment : Fragment() {
    private val lbDocRef =
        Firebase.firestore.collection("LeaderBoard").orderBy("poengsum", Query.Direction.DESCENDING)
    private lateinit var leaderboardListe: ArrayList<Leaderboard>
    lateinit var leaderboardAdapter: LeaderboardAdapter
    lateinit var db: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentLeaderboardBinding>(
            inflater,
            R.layout.fragment_leaderboard,
            container,
            false
        )
        leaderboardListe = arrayListOf()
        val recyclerView: RecyclerView = binding.leaderboardRecyclerview
        leaderboardAdapter = LeaderboardAdapter(leaderboardListe)
        recyclerView.adapter = leaderboardAdapter
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        realTimeLeaderboardUpdate()
        return binding.root
    }

    private fun realTimeLeaderboardUpdate() {
        db = FirebaseFirestore.getInstance()
        lbDocRef.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                Toast.makeText(this.context, it.message, Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }
            querySnapshot?.let {
                for (doc in it) {
                    val leaderboard = doc.toObject<Leaderboard>()
                    leaderboardListe.add(
                        Leaderboard(
                            leaderboard.uid,
                            leaderboard.navn,
                            leaderboard.poengsum
                        )
                    )
                }
                leaderboardAdapter.notifyDataSetChanged()
            }
        }
    }
}
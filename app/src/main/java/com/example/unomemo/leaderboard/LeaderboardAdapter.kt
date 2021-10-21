package com.example.unomemo.leaderboard

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.unomemo.R
import com.example.unomemo.bruker.Bruker
import com.example.unomemo.kategori.KategoriData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.lang.StringBuilder

class LeaderboardAdapter(private val leaderboardListe: ArrayList<Leaderboard>) :
    RecyclerView.Adapter<LeaderboardAdapter.LeaderBoardViewHolder>() {

    inner class LeaderBoardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val spillernavn: TextView = view.findViewById(R.id.spiller_navn_textview)
        val poengsum: TextView = view.findViewById(R.id.spiller_poeng_textview)
        var plassering: TextView = view.findViewById(R.id.spiller_plassering_textview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderBoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.spiller_item, parent, false)
        return LeaderBoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaderBoardViewHolder, position: Int) {

        val leaderboard: Leaderboard = leaderboardListe[position]
        holder.spillernavn.text = leaderboard.navn
        holder.poengsum.text = leaderboard.poengsum.toString()
        if (position == 0) {
            setFirstPlace(holder)
        }
        holder.plassering.text = String.format("%d", position + 1)

    }

    fun setFirstPlace(holder: LeaderBoardViewHolder){
        holder.spillernavn.setTextColor(Color.parseColor("#FFD700"))
        holder.plassering.setTextColor(Color.parseColor("#FFD700"))
        holder.poengsum.setTextColor(Color.parseColor("#FFD700"))
    }

    override fun getItemCount(): Int {
        return leaderboardListe.size
    }
}
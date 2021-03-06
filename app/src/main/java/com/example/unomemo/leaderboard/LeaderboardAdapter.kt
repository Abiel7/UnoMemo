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
        //Setter informasjonen fra arraylista på Textview-ene
        val leaderboard: Leaderboard = leaderboardListe[position]
        holder.spillernavn.text = leaderboard.navn
        holder.poengsum.text = leaderboard.poengsum.toString()
        //sjekker om posisjonen er 0, da vil første plass få gull tekst istadenfor vanlig farge
        if (position == 0) {
            setFirstPlace(holder)
        }
        //Bruker string formatering for sette på posisjonen på kortene,
        //uten å oppdatere eller lage ekstra fields i databasen
        holder.plassering.text = String.format("%d", position + 1)

    }

    //Denne metoden setter gullfarge på plassering, navn og poengsum til personen som er på første plass
    fun setFirstPlace(holder: LeaderBoardViewHolder) {
        holder.spillernavn.setTextColor(Color.parseColor("#FFB302"))
        holder.plassering.setTextColor(Color.parseColor("#FFB302"))
        holder.poengsum.setTextColor(Color.parseColor("#FFB302"))
    }

    override fun getItemCount(): Int {
        return leaderboardListe.size
    }
}
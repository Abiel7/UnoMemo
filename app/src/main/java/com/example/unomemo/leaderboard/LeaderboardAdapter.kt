package com.example.unomemo.leaderboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.unomemo.R
import com.example.unomemo.kategori.KategoriData

class LeaderboardAdapter(): RecyclerView.Adapter<LeaderboardAdapter.LeaderBoardViewHolder>()  {
    inner class LeaderBoardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val spillernavn: TextView = view.findViewById(R.id.spiller_navn_textview)
        private val poengsum: TextView = view.findViewById(R.id.spiller_poeng_textview)
        private val plassering: TextView = view.findViewById(R.id.spiller_plassering_textview)
        fun bind(position: Int) {
            spillernavn.text = KategoriData.spillerliste[position]
            poengsum.text = KategoriData.spillerpoengliste[position]
            plassering.text = KategoriData.spillerplassingliste[position]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderBoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.spiller_item, parent, false)
        return LeaderBoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaderBoardViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return KategoriData.spillerliste.size
    }
}
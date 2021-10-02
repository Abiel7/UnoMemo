package com.example.unomemo.spillKort

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.unomemo.R
import kotlin.properties.Delegates


class SpillBrettAdatper(private val spillFragment: Context, private val i: Int) : RecyclerView.Adapter<SpillBrettAdatper.ViewHolder>() {

   private var isFlipped by Delegates.notNull<Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(spillFragment).inflate(R.layout.spillkortlayout,parent,false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(position)

    }

    override fun getItemCount() = i

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val imageButton = itemView.findViewById<ImageButton>(R.id.imageButton)

        fun bind(postion:Int){

            imageButton.setImageResource(R.drawable.ic_baseline_image_24)
            imageButton.setOnClickListener {
                Log.i(ContentValues.TAG, "Clicked on position $postion")
            }

        }
    }

}
package com.example.unomemo.spillKort

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.unomemo.spilldata.Vanskelighetsgrad
import com.example.unomemo.R
import com.example.unomemo.spilldata.KortInfo


class SpillBrettAdatper(
    private val spillFragment: Context,
    private val i: Vanskelighetsgrad,
    private val infoKort: List<KortInfo>,
    private val listner : SpillKort.Click
) : RecyclerView.Adapter<SpillBrettAdatper.ViewHolder>() {

    companion object{
        private const val TAG = "SpillBrettAdatper"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(spillFragment).inflate(R.layout.spillkortlayout,parent,false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(position)

//      notifyItemChanged(holder.adapterPosition)

    }

    override fun getItemCount() = i.antallkort

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val imageButton = itemView.findViewById<ImageButton>(R.id.imageButton)

        fun bind(pos:Int){

            if(infoKort[pos].isUp){
                imageButton.setImageResource(infoKort[pos].id)
            }

            else{

                imageButton.setImageResource(R.drawable.ic_baseline_image_24)

            }
            imageButton.setOnClickListener {

                Log.i(TAG, "Clicked on position $pos")
                listner.onCardClicked(pos)

            }

        }
    }

}
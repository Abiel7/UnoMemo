package com.example.unomemo.lastoppbilde

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.unomemo.R
import com.example.unomemo.spilldata.Vanskelighetsgrad

class VelgBildeAdapter(private val lastoppBilde: Context,
                       private val antallBilderValgt: MutableList<Uri>,
                       private val i: Vanskelighetsgrad
) : RecyclerView.Adapter<VelgBildeAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VelgBildeAdapter.ViewHolder {
        val view = LayoutInflater.from(lastoppBilde).inflate(R.layout.bilde_kort_layout,parent,false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount()=i.getHoyde()

    inner class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        private val iamgeView = itemView.findViewById<ImageView>(R.id.telBilder)


        fun bind (uri: Uri){
            iamgeView.setImageURI(uri)
        }

        fun bind (){
            iamgeView.setOnClickListener {
                // start intent
            }
        }
    }

}

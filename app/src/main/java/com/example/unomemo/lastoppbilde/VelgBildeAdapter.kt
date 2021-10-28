package com.example.unomemo.lastoppbilde

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.unomemo.lastoppbilde.LastoppBilde
import com.example.unomemo.R
import com.example.unomemo.spilldata.Vanskelighetsgrad

class VelgBildeAdapter(private val context: Context,
                       private val image: MutableList<Uri>,
                       private val dificultyLevel: Vanskelighetsgrad,
                       private val imageClickListner: LastoppBilde.ImageListener
) : RecyclerView.Adapter<VelgBildeAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VelgBildeAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.bilde_kort_layout,parent,false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount()=dificultyLevel.getHoyde()

    inner class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        private val iamgeView = itemView.findViewById<ImageView>(R.id.telBilder)
        fun bind (uri: Uri){
            iamgeView.setImageURI(uri)
        }

        fun bind (){
            iamgeView.setOnClickListener {
                imageClickListner.onImageClick()
            }
        }
    }


}

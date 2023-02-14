package com.example.whatsapp.Adapter

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatsapp.Models.UserModel
import com.example.whatsapp.R
import com.example.whatsapp.ViewModels.AuthViewModel

class ContatosAdapter(private val dataSet: MutableList<UserModel>? = mutableListOf()) :
    RecyclerView.Adapter<ContatosAdapter.ViewHolder>() {

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    private lateinit var mListener: onItemClickListener
    private val authViewModel: AuthViewModel = AuthViewModel()

    fun setonItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    class ViewHolder(view: View, listener: onItemClickListener) : RecyclerView.ViewHolder(view) {
        val nome: TextView = view.findViewById(R.id.editTextNome)
        val userImage: ImageView = view.findViewById(R.id.profile_image)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_item_list, viewGroup, false)
        return ViewHolder(view, mListener)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context


        dataSet?.let {
            Log.d(TAG, "onBindViewHolder: ${dataSet}")
            if (it[position].imageUrl != "null") {
                val uri: Uri = Uri.parse(it[position].imageUrl)
                Glide.with(context)
                    .load(uri)
                    .skipMemoryCache(true)//for caching the image url in case phone is offline
                    .into(holder.userImage)
            } else {
                holder.userImage.setImageResource(R.drawable.padrao)
            }
            holder.nome.text = it[position].nome
        }
    }

    override fun getItemCount(): Int {
        return dataSet?.size ?: 0
    }
}



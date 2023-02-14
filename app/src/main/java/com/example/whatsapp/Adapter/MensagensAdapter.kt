package com.example.whatsapp.Adapter

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatsapp.Models.MessageModel
import com.example.whatsapp.R
import com.example.whatsapp.ViewModels.AuthViewModel

class MensagensAdapter(private val dataSet: MutableList<MessageModel>? = mutableListOf()) :
    RecyclerView.Adapter<MensagensAdapter.ViewHolder>() {
    private val tipoDestinatario: Int = 1
    private val tipoRemetente: Int = 0
    private val authViewModel: AuthViewModel = AuthViewModel()
    private val idUser: String = authViewModel.auth.currentUser?.uid.toString()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nome: TextView = view.findViewById(R.id.textViewText)
        val userImage: ImageView = view.findViewById(R.id.imgMessageFoto)
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): MensagensAdapter.ViewHolder {
        val view: View = if (viewType == tipoRemetente) {

            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.adapter_mensagem_remetente, viewGroup, false)

        } else {

            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.adapter_mensagem_destinatario, viewGroup, false)

        }
        return MensagensAdapter.ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MensagensAdapter.ViewHolder, position: Int) {
        val context = holder.itemView.context


        dataSet?.let {
            println(it[position].image)
            if (it[position].image != "null") {
                val uri: Uri = Uri.parse(it[position].image)
                holder.userImage.visibility = VISIBLE
                Glide.with(context)
                    .load(uri)
                    .skipMemoryCache(true)//for caching the image url in case phone is offline
                    .into(holder.userImage)
            }
            holder.nome.text = it[position].mensagem
        }
    }

    override fun getItemCount(): Int {
        return dataSet?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        dataSet?.let {
            Log.d(TAG, "getItemViewType: ${it[position].idUser}")
            Log.d(TAG, "getItemViewType: ${this.idUser}")

            if (it[position].idUser == this.idUser) {
                return tipoRemetente
            } else {
                return tipoDestinatario
            }
        }
        return -1
    }
}
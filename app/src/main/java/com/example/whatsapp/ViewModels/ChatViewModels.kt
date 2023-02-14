package com.example.whatsapp.ViewModels

import android.content.ContentValues
import android.util.Log
import com.example.whatsapp.Models.ChatModel
import com.google.firebase.database.*

class ChatViewModels {
    private val authViewModel: AuthViewModel = AuthViewModel()
    private val userId: String = authViewModel.auth.currentUser?.uid.toString()
    private val databaseViewModel: DatabaseViewModel = DatabaseViewModel()
    private val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val chatReference: DatabaseReference = reference.child("chat")

    fun getAllChats(callback: (result: MutableList<ChatModel>) -> Unit) {

        val specificUser = chatReference.child(this.userId)
        specificUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val allChat: MutableList<ChatModel> = mutableListOf()
                for (dataSnapshot in dataSnapshot.children) {
                    val id: String = dataSnapshot.key.toString()
                    val lastMessage: String = dataSnapshot.child("lastMessage").value.toString()
                    val name: String = dataSnapshot.child("name").value.toString()
                    val urlImage: String = dataSnapshot.child("urlImage").value.toString()
                    val chat: ChatModel = ChatModel(lastMessage, urlImage, name, id)
                    allChat.add(chat)
                }
                callback.invoke(allChat)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })
    }


    fun sendDados(lastMessage: String, remetente: ChatModel): String {
        return try {
            this.databaseViewModel.getDadosBykey(this.userId) {
                val destinatario: ChatModel =
                    ChatModel(lastMessage, it.imageUrl, it.nome, remetente.id)
                chatReference.child(destinatario.id).child(it.id).setValue(destinatario)
                val remetenteId = remetente.id
                remetente.id = it.id
                chatReference.child(it.id).child(remetenteId).setValue(remetente)
            }
            "Usuario Cadastrado com sucesso"
        } catch (error: Error) {
            "Usuario não cadastrado"
        }
    }
}



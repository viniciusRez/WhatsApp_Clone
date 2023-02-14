package com.example.whatsapp.ViewModels

import android.content.ContentValues
import android.util.Log
import com.example.whatsapp.Models.MessageModel
import com.google.firebase.database.*

class MensagemDatabaseViewModel {
    private val authViewModel: AuthViewModel = AuthViewModel()
    private val userId: String = authViewModel.auth.currentUser?.uid.toString()
    private val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val mensageReference: DatabaseReference =
        reference.child("mensagem")

    private val userMensageReference: DatabaseReference =
        reference.child("mensagem").child(this.userId)

    fun getDadosBykey(key: String, callback: (result: MutableList<MessageModel>) -> Unit) {

        val specificUser = userMensageReference.child(key)
        specificUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val allmensagemModels: MutableList<MessageModel> = mutableListOf<MessageModel>()
                for (dataSnapshot in dataSnapshot.children) {
                    val idUser: String = dataSnapshot.child("idUser").value.toString()
                    val mensagem: String = dataSnapshot.child("mensagem").value.toString()
                    val urlImage: String = dataSnapshot.child("image").value.toString()
                    println(idUser)
                    val messageModel: MessageModel = MessageModel(mensagem, urlImage, idUser)
                    allmensagemModels.add(messageModel)
                }
                callback.invoke(allmensagemModels)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })
    }


    fun sendMessage(mensagemModel: MessageModel, idRemetente: String): String {
        return try {
            mensagemModel.idUser = this.userId
            userMensageReference.child(idRemetente).push().setValue(mensagemModel)
            mensageReference.child(idRemetente)
                .child(authViewModel.auth.currentUser?.uid.toString()).push()
                .setValue(mensagemModel)
            "Mensagem enviada com sucesso"
        } catch (error: Error) {
            "Mensagem não enviada"
        }
    }

}
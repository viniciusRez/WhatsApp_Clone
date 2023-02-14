package com.example.whatsapp.ViewModels

import android.content.ContentValues
import android.util.Log
import com.example.whatsapp.Models.UserModel
import com.google.firebase.database.*

class DatabaseViewModel {
    private val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val usuarioReference: DatabaseReference = reference.child("usuarios")
    fun getDados(callback: (result: MutableList<UserModel>) -> Unit) {
        val authViewModel: AuthViewModel = AuthViewModel()
        val idUser: String = if (authViewModel.auth.currentUser != null) {
            authViewModel.auth.currentUser?.uid.toString()
        } else {
            ""
        }
        usuarioReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val allDados: MutableList<UserModel> = mutableListOf<UserModel>()
                for (dataSnapshot in dataSnapshot.children) {
                    val email: String = dataSnapshot.child("email").value.toString()
                    val nome: String = dataSnapshot.child("nome").value.toString()
                    val id: String = dataSnapshot.key.toString()
                    val urlImage: String = dataSnapshot.child("imageUrl").value.toString()
                    val users: UserModel = UserModel(email, id, urlImage, nome)
                    if (!id.equals(idUser)) {
                        allDados.add(users)
                    }
                }

                callback.invoke(allDados)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    fun getDadosBykey(key: String, callback: (result: UserModel) -> Unit) {
        key.let { key ->

            val specificUser = usuarioReference.child(key)
            specificUser.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val email: String = dataSnapshot.child("email").value.toString()
                    val nome: String = dataSnapshot.child("nome").value.toString()
                    val id: String = dataSnapshot.key.toString()
                    val urlImage: String = dataSnapshot.child("imageUrl").value.toString()
                    val user: UserModel = UserModel(email, id, urlImage, nome)

                    callback.invoke(user)

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
                }
            })
        }
    }

    fun sendDados(dados: UserModel): String {
        return try {
            usuarioReference.child(dados.id).setValue(dados)
            "Usuario Cadastrado com sucesso"
        } catch (error: Error) {
            "Usuario não cadastrado"
        }
    }


}
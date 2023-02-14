package com.example.whatsapp.ViewModels

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class StorageViewModel {
    private val storage: StorageReference = FirebaseStorage.getInstance().reference
    private val authViewModel: AuthViewModel = AuthViewModel()
    private val idUser: String = authViewModel.auth.currentUser?.uid.toString()
    fun saveImage(image: ByteArray, callback: (result: String) -> Unit) {

        val reference = storage.child("imagens").child("perfil").child(idUser).child("perfil.jpeg")
        val uploadTask = reference.putBytes(image)
        uploadTask.addOnFailureListener {
            callback.invoke(it.toString())
        }.addOnSuccessListener {
            reference.downloadUrl.addOnCompleteListener {
                callback.invoke(it.result.toString())

            }

        }
    }

    fun saveSendImage(image: ByteArray, callback: (result: String) -> Unit) {
        val data: Date = Date()
        val reference =
            storage.child("imagens").child("send").child(idUser).child("$data.jpeg")
        val uploadTask = reference.putBytes(image)
        uploadTask.addOnFailureListener {
            callback.invoke(it.toString())
        }.addOnSuccessListener {
            reference.downloadUrl.addOnCompleteListener {
                callback.invoke(it.result.toString())

            }
        }
    }
}
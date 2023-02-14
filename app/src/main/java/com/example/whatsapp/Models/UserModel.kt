package com.example.whatsapp.Models

data class UserModel(
    var email: String,
    var id: String,
    var imageUrl: String?,
    var nome: String
    ) : java.io.Serializable

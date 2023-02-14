package com.example.whatsapp.Models

data class MessageModel(
    val mensagem: String,
    val image: String? = null,
    var idUser: String)


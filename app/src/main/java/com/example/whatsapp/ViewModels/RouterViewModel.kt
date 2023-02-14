package com.example.whatsapp.ViewModels

import android.content.Context
import android.content.Intent
import com.example.whatsapp.Activitys.ChatActivity
import com.example.whatsapp.Activitys.ConfiguracoesActivity
import com.example.whatsapp.Activitys.MainActivity
import com.example.whatsapp.Activitys.RegisterActivity
import com.example.whatsapp.Models.UserModel

class RouterViewModel(private val contextAplication: Context) {
    fun goRegister() {
        val intent: Intent = Intent(contextAplication, RegisterActivity::class.java)
        contextAplication.startActivity(intent)
    }

    fun goMain() {
        val intent: Intent = Intent(contextAplication, MainActivity::class.java)
        contextAplication.startActivity(intent)
    }

    fun goConfig() {
        val intent: Intent = Intent(contextAplication, ConfiguracoesActivity::class.java)
        contextAplication.startActivity(intent)
    }

    fun goChat(userSelected: UserModel) {
        val intent: Intent = Intent(contextAplication, ChatActivity::class.java).putExtra(
            "userChat",
            userSelected as java.io.Serializable
        )
        contextAplication.startActivity(intent)
    }
}

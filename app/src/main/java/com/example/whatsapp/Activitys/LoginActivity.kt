package com.example.whatsapp.Activitys

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.whatsapp.Models.MakeAlert
import com.example.whatsapp.Models.RegisterUserModel
import com.example.whatsapp.R
import com.example.whatsapp.ViewModels.AuthViewModel
import com.example.whatsapp.ViewModels.RouterViewModel

class LoginActivity : AppCompatActivity() {
    private val authViewModel: AuthViewModel = AuthViewModel()
    private val routerViewModel: RouterViewModel = RouterViewModel(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val textViewRegister: TextView = findViewById(R.id.textViewRegister)
        textViewRegister.setOnClickListener {
            routerViewModel.goRegister()
        }
        val btnLogin: Button = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener {
            makeLogin(it)
        }
    }

    private fun makeLogin(view:View) {
        val textEmail: TextView = findViewById(R.id.ediTextEmail)
        val textSenha: TextView = findViewById(R.id.ediTextSenha)
        if (textEmail.text.isNotEmpty() && textSenha.text.isNotEmpty()) {
            val userInfo: RegisterUserModel =
                RegisterUserModel(textEmail.text.toString(), textSenha.text.toString(), "")
            authViewModel.login(userInfo, this) { mensagem,titulo ->
                val alert:MakeAlert= MakeAlert(this)
                alert.basicAlert(view,titulo,mensagem,this)
                if (authViewModel.checkIsOn()) {
                    val intent: Intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val isLogged = authViewModel.checkIsOn()
        if (isLogged) {
            routerViewModel.goMain()
        }
    }
}

package com.example.whatsapp.Activitys

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

class RegisterActivity : AppCompatActivity() {
    private val authViewModel: AuthViewModel = AuthViewModel()
    private val routerViewModel: RouterViewModel = RouterViewModel(this)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btnRegister: Button = findViewById(R.id.btnRegister)
        btnRegister.setOnClickListener {
            this.register(it)
        }
    }

    private fun register(view: View) {
        val email: TextView = findViewById(R.id.ediTextEmail)
        val nome: TextView = findViewById(R.id.editTextNome)
        val senha: TextView = findViewById(R.id.editTextSenha)

        if (email.text.isNotEmpty() && nome.text.isNotEmpty() && senha.text.isNotEmpty()) {
            //convertendo para String
            val emailString = email.text.toString()
            val nomeString = nome.text.toString()
            val senhaString = senha.text.toString()

            val userInfo: RegisterUserModel =
                RegisterUserModel(emailString, senhaString, nomeString)
            authViewModel.register(userInfo, this) { mensagem,titulo ->
                val alert: MakeAlert = MakeAlert(applicationContext)
                alert.basicAlert(view,titulo,mensagem,this)
                if (authViewModel.checkIsOn()) {
                    routerViewModel.goMain()
                }
            }
        } else {
            val alert: MakeAlert = MakeAlert(applicationContext)
            alert.basicAlert(view,"Complete os Campos","Complete todos os campos para realizar o cadastro",this)
        }
    }
}
package com.example.whatsapp.ViewModels

import android.app.Activity
import com.example.whatsapp.Models.RegisterUserModel
import com.example.whatsapp.Models.UserModel
import com.google.firebase.auth.FirebaseAuth


class AuthViewModel {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val databaseViewModel: DatabaseViewModel = DatabaseViewModel()

    fun checkIsOn(): Boolean {
        return auth.currentUser != null
    }

    fun singOut(): Boolean {
        return try {
            auth.signOut()
            true
        } catch (error: Error) {
            false
        }
    }


    fun register(info: RegisterUserModel, context: Activity, callback: (message: String,tittle: String) -> Unit) {
        auth.createUserWithEmailAndPassword(info.email, info.senha)
            .addOnCompleteListener(context) { result ->
                if (result.isSuccessful) {
                    val userModel: UserModel = UserModel(
                        info.email,
                        auth.currentUser?.uid.toString(), null, info.nome
                    )
                    callback.invoke(databaseViewModel.sendDados(userModel),"sucesso")

                } else {
                    errorResponse(result.exception.toString())
                    val errorMessage = errorResponse(result.exception.toString())
                    callback.invoke(errorMessage,"Erro")
                }
            }

    }

    private fun errorResponse(error: String):String {
        return if (error.contains( "badly formatted")) {
            "E-mail incorreto."
        } else if (error.contains( "least 6 characters")) {
            "Digite uma senha maior que 5 characters."
        } else if(error.contains(  "by another account")) {
            "E-mail ja esta em uso."
        }else if(error.contains(  "not have a password")) {
            "Senha incorreta."
        } else {
            "Erro inexperado, entre em contato com o suporte"
        }
    }

    fun login(info: RegisterUserModel, context: Activity, callback: (message: String,tittle: String) -> Unit) {
        auth.signInWithEmailAndPassword(info.email, info.senha)
            .addOnCompleteListener(context) { result ->
                if (result.isSuccessful) {
                    callback.invoke("Login Realizado com Sucesso","Sucesso")
                } else {
                    val errorMessage = errorResponse(result.exception.toString())
                    callback.invoke(errorMessage,"Fracasso")

                }
            }


    }


}
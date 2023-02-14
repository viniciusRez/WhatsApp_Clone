package com.example.whatsapp.Activitys

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.whatsapp.Models.MakeAlert
import com.example.whatsapp.Models.UserModel
import com.example.whatsapp.R
import com.example.whatsapp.ViewModels.AuthViewModel
import com.example.whatsapp.ViewModels.DatabaseViewModel
import com.example.whatsapp.ViewModels.StorageViewModel
import java.io.ByteArrayOutputStream


class ConfiguracoesActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel = AuthViewModel()
    private val userId: String = this.authViewModel.auth.currentUser?.uid.toString()
    private var userInformation: UserModel? = null
    private val databaseViewModel: DatabaseViewModel = DatabaseViewModel()
    private val storageViewModel: StorageViewModel = StorageViewModel()
    private var resultLaucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK){
            val data:Intent? = result.data
            if(data != null){
                val uri = data.data
                val imageView: ImageView = findViewById(R.id.profile_image)
                imageView.setBackgroundResource(androidx.appcompat.R.color.secondary_text_default_material_dark)
                imageView.setImageURI(uri)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracoes)
        val toolbar: Toolbar = findViewById(R.id.toolbarPrincipal)
        toolbar.title = "Configurações"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val imageView: ImageView = findViewById(R.id.profile_image)

        val textViewNome: TextView = findViewById(R.id.editTextNomeUsuario)
        if (userInformation == null) {
            this.databaseViewModel.getDadosBykey(userId) { result ->
                textViewNome.text = result.nome
                this.userInformation = result
                if (result.imageUrl != "null") {
                    val uri: Uri = Uri.parse(result.imageUrl)
                    Glide.with(this)
                        .load(uri)
                        .skipMemoryCache(true)//for caching the image url in case phone is offline
                        .into(imageView)
                    imageView.setImageURI(uri)
                }
            }
        }
        imageView.setOnClickListener {
            showImage()
        }

        fun updateUser(view: View) {
            val imageInByte = this.convertToBiteMap(imageView)
            this.storageViewModel.saveImage(imageInByte) { result ->
                this.userInformation?.imageUrl = result
                this.userInformation?.nome = textViewNome.text.toString()
                this.userInformation?.let { it1 -> this.databaseViewModel.sendDados(it1) }
                val alert:MakeAlert = MakeAlert(applicationContext)
                alert.basicAlert(view,"Perfil Atualizado","Seu perfil foi atualizado com sucesso",this)
                val timer = object: CountDownTimer(2000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                    }

                    override fun onFinish() {
                     finish()
                    }
                }
                timer.start()

            }
        }

        val editImageView: ImageView = findViewById(R.id.imageViewSalvar)

        editImageView.setOnClickListener {
            updateUser(it)
        }

    }

    private fun convertToBiteMap(imageView: ImageView): ByteArray {
        val bitmap = imageView.drawable?.let { (it as BitmapDrawable).bitmap }
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageInByte = baos.toByteArray()
        return imageInByte
    }

    private fun showImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        resultLaucher.launch(intent)
    }
}
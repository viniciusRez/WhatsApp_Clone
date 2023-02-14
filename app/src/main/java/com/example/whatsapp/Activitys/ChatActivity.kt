package com.example.whatsapp.Activitys

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatsapp.Adapter.MensagensAdapter
import com.example.whatsapp.Models.ChatModel
import com.example.whatsapp.Models.MessageModel
import com.example.whatsapp.Models.UserModel
import com.example.whatsapp.R
import com.example.whatsapp.ViewModels.ChatViewModels
import com.example.whatsapp.ViewModels.MensagemDatabaseViewModel
import com.example.whatsapp.ViewModels.StorageViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.ByteArrayOutputStream

class ChatActivity : AppCompatActivity() {
    private val chatViewModel: ChatViewModels = ChatViewModels()
    private val databaseViewModel: MensagemDatabaseViewModel =
        MensagemDatabaseViewModel()//Salva as mensagens
    private val storageViewModel: StorageViewModel = StorageViewModel()//Salva as Imagens

    private lateinit var user: UserModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        if (intent.getSerializableExtra("userChat") != null) {
            user = (intent.getSerializableExtra("userChat") as? UserModel)!!
        }
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val username: TextView = findViewById(R.id.username)
        username.text = user.nome

        val profileImage: ImageView = findViewById(R.id.profile_image)
        val uri: Uri = Uri.parse(user.imageUrl)
        if (user.imageUrl.equals("null")) {
            profileImage.setImageDrawable(getDrawable(R.drawable.padrao))
        } else {
            Glide.with(this)
                .load(uri)
                .skipMemoryCache(true)//for caching the image url in case phone is offline
                .into(profileImage)
        }
        val mensageTextView: EditText = findViewById(R.id.editTextMessage)
        val sendButton: FloatingActionButton = findViewById(R.id.sendButton)
        sendButton.setOnClickListener {
            val message = mensageTextView.text.toString()
            if (message.isNotEmpty()) {
                val modelMensage = MessageModel(message, null, "")
                this.databaseViewModel.sendMessage(modelMensage, user.id)
                val remetente: ChatModel = ChatModel(message, user.imageUrl, user.nome, user.id)
                this.chatViewModel.sendDados(message, remetente)
                mensageTextView.setText("")

            }

        }
        val sendImageButton: ImageView = findViewById(R.id.sendImage)
        sendImageButton.setOnClickListener {
            showImage()
        }
        val recyclerView: RecyclerView = findViewById(R.id.RecyclerViewChat)
        databaseViewModel.getDadosBykey(user.id) {
            print("resultado das Mensagens: ${it}")
            recyclerView.adapter = MensagensAdapter(it)

        }
    }

    private fun convertToBiteMap(imageView: Uri): ByteArray {
        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageView)
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageInByte = baos.toByteArray()
        return imageInByte
    }

    private fun showImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(Intent.createChooser(intent, "Escolha uma imagem"), 11)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            if (data != null) {

                val uri = data.data
                val imageInBytes = uri?.let { this.convertToBiteMap(it) }
                if (imageInBytes != null) {
                    this.storageViewModel.saveSendImage(imageInBytes) {
                        val modelMensage = MessageModel("", it, "")
                        this.databaseViewModel.sendMessage(modelMensage, user.id)

                    }
                }
            }

        }
    }
}
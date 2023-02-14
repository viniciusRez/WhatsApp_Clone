package com.example.whatsapp.Models

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.Toast

class MakeAlert(applicationContext:Context) {

    val positiveButtonClick = { dialog: DialogInterface, which: Int ->

    }

    val negativeButtonClick = { dialog: DialogInterface, which: Int ->

    }




    fun basicAlert(view: View, tittle: String, message: String, context: Context) {

        val builder = AlertDialog.Builder(context)

        with(builder)
        {
            setTitle(tittle)
            setMessage(message)
            setNegativeButton(android.R.string.no, negativeButtonClick)
            setPositiveButton(
                "OK",
                DialogInterface.OnClickListener(function = positiveButtonClick)
            )
            show()
        }
    }

}
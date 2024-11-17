package com.example.androiddomain.utils

import androidx.appcompat.app.AlertDialog
import com.example.androiddomain.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object Config {
    private var dialog  : AlertDialog? = null

    fun showDialog(context: android.content.Context){
        dialog = MaterialAlertDialogBuilder(context)
            .setView(R.layout.loading_layout)
            .setCancelable(false)
            .create()


        dialog!!.show()
    }

    fun hideDialog(){
        dialog!!.dismiss()
    }
}
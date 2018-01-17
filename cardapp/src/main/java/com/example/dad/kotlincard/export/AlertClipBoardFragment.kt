package com.example.dad.kotlincard.export

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.example.dad.kotlincard.R

/**
 * Created by Dad on 12/29/2017.
 */
class AlertClipBoardFragment:DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //return super.onCreateDialog(savedInstanceState)
        return AlertDialog.Builder(activity)
                .setTitle(R.string.alertclipboard)
                .setPositiveButton(android.R.string.ok,null)
                .create()
    }

}
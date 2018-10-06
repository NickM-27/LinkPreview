package com.nick.mowen.linkpreview.sample

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

object Utils {

    fun hideKeyboard(context: Context) {
        try {
            val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow((context as Activity).currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (e: Exception) {
            // no-op
        }
    }

    fun showToast(context: Context, msg: String, toastLength: Int = Toast.LENGTH_SHORT) {
        try {
            Toast.makeText(context, msg, toastLength)?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
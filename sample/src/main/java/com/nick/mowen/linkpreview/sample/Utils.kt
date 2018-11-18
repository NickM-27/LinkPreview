package com.nick.mowen.linkpreview.sample

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

object Utils {

    fun hideKeyboard(context: Context) {
        try {
            val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow((context as Activity).currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (e: Exception) {
            // no-op
        }
    }
}
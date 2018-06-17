package com.nick.mowen.linkpreview.listener

import android.view.View

interface LinkClickListener {

    fun onLinkClicked(view: View?, url: String)
}
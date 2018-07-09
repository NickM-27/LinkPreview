package com.nick.mowen.linkpreview.listener

import android.view.View
import com.nick.mowen.linkpreview.view.LinkPreview

interface LinkClickListener {

    /**
     * Listener to provide custom preview click functionality
     *
     * @param view of type [LinkPreview] that was pressed, possibly null
     * @param url of the website that was parsed that should be opened
     */
    fun onLinkClicked(view: View?, url: String)
}
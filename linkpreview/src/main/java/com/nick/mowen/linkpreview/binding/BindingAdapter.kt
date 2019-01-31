package com.nick.mowen.linkpreview.binding

import androidx.databinding.BindingAdapter
import com.nick.mowen.linkpreview.view.LinkPreview

object BindingAdapter {

    @BindingAdapter("parsedLink")
    @JvmStatic
    fun setParsedLink(view: LinkPreview, link: String) = view.parseTextForLink(link)
}
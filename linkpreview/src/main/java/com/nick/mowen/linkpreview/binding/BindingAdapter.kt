package com.nick.mowen.linkpreview.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.nick.mowen.linkpreview.view.LinkPreview

object BindingAdapter {

    @BindingAdapter("parsedLink")
    @JvmStatic
    fun LinkPreview.setParsedLink(link: String) = parseTextForLink(link)

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun ImageView.setImageUrl(imageUrl: String?) {
        imageUrl ?: return
        load(imageUrl) {
            crossfade(true)
        }
    }
}
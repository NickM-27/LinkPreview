package com.nick.mowen.linkpreview.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.api.load
import com.nick.mowen.linkpreview.view.LinkPreview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
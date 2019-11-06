package com.nick.mowen.linkpreview.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.nick.mowen.linkpreview.CardData
import com.nick.mowen.linkpreview.ImageType
import com.nick.mowen.linkpreview.R
import com.nick.mowen.linkpreview.databinding.CardBinding
import com.nick.mowen.linkpreview.extension.*
import com.nick.mowen.linkpreview.listener.CardListener
import com.nick.mowen.linkpreview.listener.LinkClickListener
import kotlinx.coroutines.*

@Suppress("MemberVisibilityCanBePrivate") // Leave values as protected for extensibility
open class LinkCardView : FrameLayout, View.OnClickListener {

    protected lateinit var binding: CardBinding
    /** Coroutine Job */
    private val linkJob = Job()
    /** Coroutine Scope */
    private val linkScope = CoroutineScope(Dispatchers.Main + linkJob)
    /** Map of cached links and their image url */
    private var linkMap: HashMap<Int, String> = hashMapOf()
    /** Type of image to handle in specific way */
    private var imageType = ImageType.NONE
    /** */
    private val thing = 0
    /** Parsed URL */
    protected var url = ""
    /** Optional listener for load callbacks */
    var loadListener: CardListener? = null
    /** Optional click listener to override click behavior */
    var clickListener: LinkClickListener? = null
    /** Set whether or not to default to hidden while loading preview */
    var articleColor: Int = Color.CYAN
    /** Color of the Chrome CustomTab that is launched on view click */
    var hideWhileLoading = false

    constructor(context: Context) : super(context) {
        bindViews(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        bindViews(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        bindViews(context)
    }

    /**
     * Handles article clicking based on TYPE of article
     *
     * @param view [LinkPreview] that was clicked
     */
    override fun onClick(view: View?) {
        if (clickListener != null)
            clickListener?.onLinkClicked(view, url)
        else {
            when (imageType) {
                ImageType.DEFAULT -> {
                    val chromeTab = CustomTabsIntent.Builder()
                        .setToolbarColor(articleColor)
                        .addDefaultShareMenuItem()
                        .enableUrlBarHiding()
                        .build()

                    try {
                        chromeTab.launchUrl(context, url.toUri())
                    } catch (e: Exception) {
                        //context.showToast("Could not open article")
                        e.printStackTrace()
                    }
                }
                ImageType.YOUTUBE -> context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
                else -> Unit
            }
        }
    }

    /**
     * Convenience method to add views to layout
     *
     * @param context for inflating view
     */
    private fun bindViews(context: Context) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.card, this, true)
        binding.root.let {
            this.minimumHeight = it.minimumHeight
            this.minimumWidth = it.minimumWidth
        }

        if (isInEditMode)
            return

        if (hideWhileLoading)
            isGone = true

        setOnClickListener(this)
        GlobalScope.launch { linkMap = context.loadLinkMap() }
    }

    private fun createLinkData() {
        if (url.let { it.contains("youtube") && it.contains("v=") }) {
            val id = url.split("v=")[1].split(" ")[0]
            val imageUrl = "https://img.youtube.com/vi/$id/hqdefault.jpg"
            imageType = ImageType.YOUTUBE
            context.addLink(url, imageUrl)
            binding.data = CardData("YouTube Video", imageUrl, url)
        } else {
            try {
                isVisible = true
                imageType = ImageType.DEFAULT
                linkScope.launch {
                    if (linkScope.isActive)
                        linkScope.cancel()

                    loadCardData(url, linkMap, url.hashCode(), loadListener)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                imageType = ImageType.NONE
                isGone = true
                loadListener?.onError()
            }
        }
    }

    /**
     * Allows easy passing of possible link text to check for links that can be handled by [LinkCardView]
     *
     * @param text entire body to search for link
     * @return if a link was found in the text
     */
    fun parseTextForLink(text: String): Boolean {
        return when {
            text.contains("youtube") && text.contains("v=") -> {
                val id = text.split("v=")[1].split(" ")[0]
                url = "https://www.youtube.com/watch?v=$id"
                createLinkData()
                true
            }
            text.contains("youtu.be") -> {
                val id = text.split("be/")[1].split(" ")[0]
                url = "https://www.youtube.com/watch?v=$id"
                createLinkData()
                true
            }
            text.contains("http") -> {
                url = text.parseUrl()
                createLinkData()
                true
            }
            else -> {
                imageType = ImageType.NONE
                isGone = true
                false
            }
        }
    }

    /**
     * Allows direct setting of url if already known
     *
     * @param link which contains only the url and nothing else
     */
    fun setLink(link: String) {
        if (link.isUrl()) {
            url = link
            createLinkData()
        } else
            throw IllegalArgumentException("String is not a valid link, if you want to parse full text use LinkPreview.parseTextForLink")
    }

    fun setCardData(data: CardData) {
        binding.data = data
        binding.executePendingBindings()
    }
}
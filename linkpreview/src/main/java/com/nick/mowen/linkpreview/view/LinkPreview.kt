package com.nick.mowen.linkpreview.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.nick.mowen.linkpreview.ImageType
import com.nick.mowen.linkpreview.PreviewData
import com.nick.mowen.linkpreview.R
import com.nick.mowen.linkpreview.databinding.PreviewBinding
import com.nick.mowen.linkpreview.extension.*
import com.nick.mowen.linkpreview.listener.LinkClickListener
import com.nick.mowen.linkpreview.listener.LinkListener
import kotlinx.coroutines.*

@Suppress("MemberVisibilityCanBePrivate") // Leave values as protected for extensibility
open class LinkPreview : FrameLayout, View.OnClickListener {

    protected lateinit var binding: PreviewBinding

    /** Coroutine Job */
    private val linkJob = Job()

    /** Coroutine Scope */
    private val linkScope = CoroutineScope(Dispatchers.Main + linkJob)

    /** Link Indicator to keep track of link status */
    private var linkIndicator: Int = 0

    /** Map of cached links and their image url */
    private var linkMap: HashMap<Int, String> = hashMapOf()

    /** Type of image to handle in specific way */
    private var imageType = ImageType.NONE

    /** Parsed URL */
    protected var url = ""

    /** Optional listener for load callbacks */
    var loadListener: LinkListener? = null

    /** Optional click listener to override click behavior */
    var clickListener: LinkClickListener? = null

    /** Set whether or not to default to hidden while loading preview */
    var articleColor: Int = Color.CYAN

    /** Color of the Chrome CustomTab that is launched on view click */
    var hideWhileLoading = false

    /** Current link looking to be processed, useful when in a list and scrolling causes race conditions */
    var currentLink = ""

    constructor(context: Context) : super(context) {
        bindViews(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        bindViews(context)
        bindAttrs(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        bindViews(context)
        bindAttrs(attrs, defStyle)
    }

    /**
     * Handles article clicking based on TYPE of article
     *
     * @param view [LinkPreview] that was clicked
     */
    override fun onClick(view: View?) {
        clickListener?.onLinkClicked(view, url) ?: run {
            when (imageType) {
                ImageType.DEFAULT -> context.launchUrlWithCustomTab(url.toUri(), articleColor)
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
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.preview, this, true)
        binding.root.let {
            this.minimumHeight = it.minimumHeight
            this.minimumWidth = it.minimumWidth
        }

        if (isInEditMode) {
            return
        }

        if (hideWhileLoading) {
            isGone = true
        }

        setOnClickListener(this)
        linkScope.launch { linkMap = context.loadLinkMap() }
    }

    private fun bindAttrs(attrs: AttributeSet, defStyle: Int) {
        context.theme.obtainStyledAttributes(attrs, R.styleable.LinkPreview, defStyle, 0).let { linkAttrs ->
            try {
                linkAttrs.getBoolean(R.styleable.LinkPreview_roundedCorners, false).let { useRoundedCorners ->
                    binding.previewImage.shapeAppearanceModel = binding.previewImage.shapeAppearanceModel.toBuilder().setAllCornerSizes(if (useRoundedCorners) 24f else 0f).build()
                    binding.background.shapeAppearanceModel = binding.background.shapeAppearanceModel.toBuilder().setAllCornerSizes(if (useRoundedCorners) 24f else 0f).build()
                }
            } finally {
                linkAttrs.recycle()
            }
        }
    }

    /**
     * Sets the actual text of the view handling multiple types of images including the link cache
     */
    private fun setText() {
        if (linkMap.containsKey(url.hashCode())) {
            linkMap[url.hashCode()]?.let { code ->
                when {
                    code == "Fail" -> {
                        visibility = View.GONE
                        Log.d(TAG, "$url was a FAIL")
                    }
                    code.contains(LINK_SEPARATOR) -> {
                        imageType = ImageType.DEFAULT
                        code.split(LINK_SEPARATOR).let { parts ->
                            setPreviewData(PreviewData(parts[0], parts[1], url))
                        }
                    }
                    else -> {
                        imageType = ImageType.DEFAULT
                        setPreviewData(PreviewData("", code, url))
                    }
                }
            }
        } else {
            if (url.let { it.contains("youtube") && it.contains("v=") }) {
                val id = url.split("v=")[1].split(" ")[0]
                val imageUrl = "https://img.youtube.com/vi/$id/hqdefault.jpg"
                imageType = ImageType.YOUTUBE
                context.addLink(url, imageUrl)
                binding.data = PreviewData("YouTube Video", imageUrl, url)
            } else {
                try {
                    visibility = View.VISIBLE
                    binding.previewText.text = url
                    imageType = ImageType.DEFAULT
                    linkScope.launch {
                        if (linkIndicator == 1) {
                            linkScope.cancel()
                        }

                        linkIndicator = 1
                        loadPreviewData(url, linkMap, url.hashCode(), loadListener)
                        linkIndicator = 0
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    imageType = ImageType.NONE
                    visibility = View.GONE
                    loadListener?.onError()
                }
            }
        }
    }

    /**
     * Handles loading the article image using Glide
     *
     * @param data to image url and article title
     */
    fun setPreviewData(data: PreviewData) {
        if (currentLink.isEmpty() || data.baseUrl == currentLink) {
            binding.data = data
            binding.executePendingBindings()

            if (!linkMap.containsKey(url.hashCode())) {
                linkMap[url.hashCode()] = data.imageUrl
                context.addLink(url, "${data.title}$LINK_SEPARATOR${data.imageUrl}")
            }

            if (!isVisible) {
                isVisible = true
            }
        }
    }

    /**
     * Allows easy passing of possible link text to check for links that can be handled by [LinkPreview]
     *
     * @param text entire body to search for link
     * @return if a link was found in the text
     */
    fun parseTextForLink(text: String): Boolean =
        when {
            text.contains("youtube") && text.contains("v=") -> {
                text.split("v=")[1].split(" ")[0].let { id ->
                    url = "https://www.youtube.com/watch?v=$id"
                    setText()
                }
                true
            }
            text.contains("youtu.be") -> {
                text.split("be/")[1].split(" ")[0].let { id ->
                    url = "https://www.youtube.com/watch?v=$id"
                    setText()
                }
                true
            }
            text.contains("http") -> {
                url = text.parseUrl()

                if (url.startsWith("http://")) {
                    url = url.replace("http://", "https://")
                }

                setText()
                true
            }
            text == "" -> {
                imageType = ImageType.NONE
                visibility = View.GONE
                false
            }
            else -> {
                imageType = ImageType.NONE
                visibility = View.GONE
                false
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
            setText()
        } else {
            throw IllegalArgumentException("String is not a valid link, if you want to parse full text use LinkPreview.parseTextForLink")
        }
    }

    companion object {

        private const val TAG = "LinkPreview"
        private const val LINK_SEPARATOR = ":;:"
    }
}
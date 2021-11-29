package com.nick.mowen.linkpreview.extension

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import com.nick.mowen.linkpreview.PreviewData
import com.nick.mowen.linkpreview.listener.CardListener
import com.nick.mowen.linkpreview.listener.LinkListener
import com.nick.mowen.linkpreview.view.LinkCardView
import com.nick.mowen.linkpreview.view.LinkPreview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun LinkPreview.loadPreviewData(
    link: String,
    linkMap: HashMap<Int, String>,
    key: Int,
    listener: LinkListener?
) = withContext(Dispatchers.Default) {
    try {
        val result = try {
            val connection = Jsoup.connect(link).userAgent("Mozilla")
            val doc: Document = connection.get()
            val imageElements = doc.select("meta[property=og:image]")

            if (imageElements.size > 0) {
                var it = 0
                var chosen: String? = ""

                while ((chosen == null || chosen.isEmpty()) && it < imageElements.size) {
                    chosen = imageElements[it].attr("content")
                    it += 1
                }

                PreviewData(doc.title(), chosen ?: "", link)
            } else {
                linkMap[key] = "Fail"
                launch(Dispatchers.Main) { listener?.onError() }
                PreviewData("", "", "")
            }
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
            linkMap[key] = "Fail"
            launch(Dispatchers.Main) { listener?.onError() }
            PreviewData("", "", "")
        } catch (e: Exception) {
            e.printStackTrace()
            linkMap[key] = "Fail"
            launch(Dispatchers.Main) { listener?.onError() }
            PreviewData("", "", "")
        }

        launch(Dispatchers.Main) {
            try {
                if (result.isNotEmpty()) {
                    setPreviewData(result)
                    listener?.onSuccess(result)
                } else {
                    Log.d("Article Request", "Image url is empty")
                    visibility = View.GONE
                    listener?.onError()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                listener?.onError()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                listener?.onError()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun LinkCardView.loadCardData(
    link: String,
    linkMap: HashMap<Int, String>,
    key: Int,
    listener: CardListener?
) = withContext(Dispatchers.Default) {
    try {
        val result = try {
            val connection = Jsoup.connect(link).userAgent("Mozilla")
            val doc: Document = connection.get()
            val imageElements = doc.select("meta[property=og:image]")

            if (imageElements.size > 0) {
                var it = 0
                var chosen: String? = ""

                while ((chosen == null || chosen.isEmpty()) && it < imageElements.size) {
                    chosen = imageElements[it].attr("content")
                    it += 1
                }

                PreviewData(doc.title(), chosen ?: "", link)
            } else {
                linkMap[key] = "Fail"
                launch(Dispatchers.Main) { listener?.onError() }
                PreviewData("", "", "")
            }
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
            linkMap[key] = "Fail"
            launch(Dispatchers.Main) { listener?.onError() }
            PreviewData("", "", "")
        } catch (e: Exception) {
            e.printStackTrace()
            linkMap[key] = "Fail"
            launch(Dispatchers.Main) { listener?.onError() }
            PreviewData("", "", "")
        }

        launch(Dispatchers.Main) {
            try {
                if (result.isNotEmpty()) {
                    setCardData(result)
                    listener?.onSuccess(result)
                } else {
                    Log.d("Article Request", "Image url is empty")
                    visibility = View.GONE
                    listener?.onError()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                listener?.onError()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                listener?.onError()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.launchUrlWithCustomTab(uri: Uri, articleColor: Int) {
    CustomTabsIntent.Builder()
        .setDefaultColorSchemeParams(
            CustomTabColorSchemeParams.Builder()
                .setToolbarColor(articleColor)
                .setNavigationBarColor(articleColor)
                .build()
        )
        .setShareState(CustomTabsIntent.SHARE_STATE_ON)
        .setUrlBarHidingEnabled(true)
        .build().let { chromeTab ->
            try {
                chromeTab.launchUrl(this, uri)
            } catch (e: Exception) {
                //context.showToast("Could not open article")
                e.printStackTrace()
            }
        }
}
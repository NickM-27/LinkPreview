package com.nick.mowen.linkpreview.extension

import android.util.Log
import android.view.View
import com.nick.mowen.linkpreview.listener.LinkListener
import com.nick.mowen.linkpreview.view.LinkPreview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

fun LinkPreview.loadImage(
    link: String,
    linkMap: HashMap<Int, String>,
    key: Int,
    listener: LinkListener?
) {
    try {
        GlobalScope.launch {
            val result = try {
                val connection = Jsoup.connect(link).userAgent("Mozilla")
                val doc: Document = connection.get()
                val imageElements = doc.select("meta[property=og:image]")

                if (imageElements.size > 0)
                    imageElements[0].attr("content")
                else {
                    linkMap[key] = "Fail"
                    launch(Dispatchers.Main) { listener?.onError() }
                    ""
                }
            } catch (e: IndexOutOfBoundsException) {
                e.printStackTrace()
                linkMap[key] = "Fail"
                launch(Dispatchers.Main) { listener?.onError() }
                ""
            } catch (e: Exception) {
                e.printStackTrace()
                linkMap[key] = "Fail"
                launch(Dispatchers.Main) { listener?.onError() }
                ""
            }

            launch(Dispatchers.Main) {
                try {
                    if (result != null && result.isNotEmpty()) {
                        setImageData(result)
                        listener?.onSuccess(result)
                    } else {
                        Log.e("Article Request", "Image url is empty")
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
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
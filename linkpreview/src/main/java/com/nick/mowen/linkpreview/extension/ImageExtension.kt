package com.nick.mowen.linkpreview.extension

import android.util.Log
import android.view.View
import com.nick.mowen.linkpreview.listener.LinkListener
import com.nick.mowen.linkpreview.view.LinkPreview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

suspend fun LinkPreview.loadImage(
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

                chosen
            } else {
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
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
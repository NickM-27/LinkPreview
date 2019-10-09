package com.nick.mowen.linkpreview.helper

import android.content.Context
import com.nick.mowen.linkpreview.extension.loadLinkMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object LinkPreviewNetwork {

    /**
     *
     */
    suspend fun loadImage(context: Context, link: String): String? {
        return try {
            withContext(Dispatchers.Default) {
                val linkMap = context.loadLinkMap()
                val key = link.hashCode()

                try {
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
                        ""
                    }
                } catch (e: IndexOutOfBoundsException) {
                    e.printStackTrace()
                    linkMap[key] = "Fail"
                    ""
                } catch (e: Exception) {
                    e.printStackTrace()
                    linkMap[key] = "Fail"
                    ""
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}
package com.nick.mowen.linkpreview

import androidx.annotation.Keep

@Keep
data class PreviewData(val title: String, val imageUrl: String, val baseUrl: String) {

    fun isEmpty(): Boolean = title.isEmpty() && imageUrl.isEmpty() && baseUrl.isEmpty()

    fun isNotEmpty(): Boolean = !isEmpty()
}
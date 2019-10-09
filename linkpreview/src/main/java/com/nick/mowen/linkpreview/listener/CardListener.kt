package com.nick.mowen.linkpreview.listener

import com.nick.mowen.linkpreview.CardData

interface CardListener {

    /**
     * Called when there was an error in loading the image from url, recommended to hide the view
     */
    fun onError()

    /**
     * Called when image from url is loaded successfully
     *
     * @param card to url image
     */
    fun onSuccess(card: CardData)
}
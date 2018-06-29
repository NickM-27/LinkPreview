package com.nick.mowen.linkpreview.listener

interface LinkListener {

    fun onError()
    fun onSuccess(link: String)
}
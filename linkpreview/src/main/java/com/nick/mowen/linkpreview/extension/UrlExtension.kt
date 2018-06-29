package com.nick.mowen.linkpreview.extension

/**
 * Checks whether a string is a url by using Regex
 */
fun String.isUrl(): Boolean = this.matches(Regex("https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_+.~#?&/=]*)"))
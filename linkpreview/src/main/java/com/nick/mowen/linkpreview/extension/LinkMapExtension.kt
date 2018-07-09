package com.nick.mowen.linkpreview.extension

import android.content.Context
import com.nick.mowen.linkpreview.Constants

fun Context.loadLinkMap(): HashMap<Int, String> {
    val map = hashMapOf<Int, String>()
    val prefs = getSharedPreferences(Constants.MAP_PREFERENCES, Context.MODE_PRIVATE)
    val hashSet = prefs.getStringSet("HASH", hashSetOf())

    for (hash in hashSet)
        map[hash.toInt()] = prefs.getString(hash, "")

    return map
}

fun Context.addLink(message: String, url: String) {
    Thread(Runnable {
        val prefs = getSharedPreferences(Constants.MAP_PREFERENCES, Context.MODE_PRIVATE)
        val current = prefs.getStringSet("HASH", hashSetOf())
        prefs.edit().apply {
            val hash = message.hashCode().toString()
            putString(hash, url)
            putStringSet("HASH", current.plus(hash))
            apply()
        }
    }).start()
}
package com.nick.mowen.linkpreview.extension

import android.content.Context
import com.nick.mowen.linkpreview.Constants

/**
 * Asynchronously loads map of hashed urls to the image url that they link to
 */
fun Context.loadLinkMap(): HashMap<Int, String> {
    val map = hashMapOf<Int, String>()
    val prefs = getSharedPreferences(Constants.MAP_PREFERENCES, Context.MODE_PRIVATE)
    val hashSet = prefs.getStringSet("HASH", hashSetOf())

    for (hash in hashSet)
        map[hash.toInt()] = prefs.getString(hash, "")

    return map
}

/**
 * Adds hashed user URL to set and creates link to preference that holds image url
 *
 * @param hashed url that was parsed
 * @param image url that is being saved for future calls
 */
fun Context.addLink(hashed: String, image: String) {
    Thread(Runnable {
        val prefs = getSharedPreferences(Constants.MAP_PREFERENCES, Context.MODE_PRIVATE)
        val current = prefs.getStringSet("HASH", hashSetOf())
        prefs.edit().apply {
            val hash = hashed.hashCode().toString()
            putString(hash, image)
            putStringSet("HASH", current.plus(hash))
            apply()
        }
    }).start()
}
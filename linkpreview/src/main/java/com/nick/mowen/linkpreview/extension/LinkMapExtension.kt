package com.nick.mowen.linkpreview.extension

import android.content.Context
import androidx.core.content.edit
import com.nick.mowen.linkpreview.Constants
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Asynchronously loads map of hashed urls to the image url that they link to
 */
fun Context.loadLinkMap(): HashMap<Int, String> {
    val map = hashMapOf<Int, String>()
    val prefs = getSharedPreferences(Constants.MAP_PREFERENCES, Context.MODE_PRIVATE)
    val hashSet = prefs.getStringSet("HASH", null) ?: return map

    for (hash in hashSet)
        map[hash.toInt()] = prefs.getString(hash, "") ?: ""

    return map
}

/**
 * Clears previously saved links
 */
fun Context.clearMap() {
    getSharedPreferences(Constants.MAP_PREFERENCES, Context.MODE_PRIVATE).edit { clear() }
}

/**
 * Adds hashed user URL to set and creates link to preference that holds image url
 *
 * @param hashed url that was parsed
 * @param image url that is being saved for future calls
 */
fun Context.addLink(hashed: String, image: String) = GlobalScope.launch {
    val prefs = getSharedPreferences(Constants.MAP_PREFERENCES, Context.MODE_PRIVATE)
    val current = prefs.getStringSet("HASH", hashSetOf()) ?: hashSetOf()
    prefs.edit {
        val hash = hashed.hashCode().toString()
        putString(hash, image)
        putStringSet("HASH", current.plus(hash))
    }
}
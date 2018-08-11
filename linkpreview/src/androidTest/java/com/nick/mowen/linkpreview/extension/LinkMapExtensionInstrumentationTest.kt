package com.nick.mowen.linkpreview.extension

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.nick.mowen.linkpreview.Constants
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LinkMapExtensionInstrumentationTest {

    @Test
    fun testLoadLinkMap() {
        val context = InstrumentationRegistry.getTargetContext()
        context.addLink("test", "testImage")
        val prefs = context.getSharedPreferences(Constants.MAP_PREFERENCES, Context.MODE_PRIVATE).getStringSet("HASH", null) ?: hashSetOf()
        require(context.loadLinkMap().size == prefs.size)
    }

    @Test
    fun testAddLink() {
        val context = InstrumentationRegistry.getTargetContext()
        context.addLink("test", "testImage")
        val map = context.loadLinkMap()
        require(map.containsValue("testImage") && map.containsKey("test".hashCode()))
    }
}
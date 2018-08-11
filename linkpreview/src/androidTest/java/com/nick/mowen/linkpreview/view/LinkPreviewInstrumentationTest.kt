package com.nick.mowen.linkpreview.view

import android.view.View
import android.widget.TextView
import androidx.test.InstrumentationRegistry
import com.nick.mowen.linkpreview.R
import com.nick.mowen.linkpreview.listener.LinkClickListener
import com.nick.mowen.linkpreview.listener.LinkListener
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LinkPreviewInstrumentationTest {

    @Test
    fun testCorrectlyShowsSetLink() {
        val context = InstrumentationRegistry.getTargetContext()
        val view = LinkPreview(context)
        view.setLink("https://www.google.com")
        require(view.findViewById<TextView>(R.id.preview_text).text == "https://www.google.com")
    }

    @Test
    fun testCorrectlyParsedLink() {
        val context = InstrumentationRegistry.getTargetContext()
        val view = LinkPreview(context)
        view.parseTextForLink("The link is https://www.google.com thanks for asking")
        require(view.findViewById<TextView>(R.id.preview_text).text == "https://www.google.com")
    }

    @Test
    fun testHidesOnFail() {
        val context = InstrumentationRegistry.getTargetContext()
        val view = LinkPreview(context)
        view.parseTextForLink("There is no link here")
        require(view.visibility == View.GONE)
    }

    @Test
    fun testClickListeners() {
        var text = "Fail"
        val context = InstrumentationRegistry.getTargetContext()
        val view = LinkPreview(context)
        view.clickListener = object : LinkClickListener {

            override fun onLinkClicked(view: View?, url: String) {
                text = "Pass"
            }
        }
        view.parseTextForLink("The link is https://www.google.com thanks for asking")
        view.performClick()
        require(text == "Pass")
    }

    @Test
    fun testLoadListeners() {
        var text = "Fail"
        val context = InstrumentationRegistry.getTargetContext()
        val view = LinkPreview(context)
        view.loadListener = object : LinkListener {

            override fun onSuccess(link: String) {
                text = link
            }

            override fun onError() {
                text = "Error"
            }
        }
        view.parseTextForLink("The link is https://www.google.com thanks for asking")
    }
}
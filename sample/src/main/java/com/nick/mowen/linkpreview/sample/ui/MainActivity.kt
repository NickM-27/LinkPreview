package com.nick.mowen.linkpreview.sample.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.nick.mowen.linkpreview.PreviewData
import com.nick.mowen.linkpreview.listener.LinkClickListener
import com.nick.mowen.linkpreview.listener.LinkListener
import com.nick.mowen.linkpreview.sample.R
import com.nick.mowen.linkpreview.sample.Utils
import com.nick.mowen.linkpreview.view.LinkCardView
import com.nick.mowen.linkpreview.view.LinkPreview

class MainActivity : AppCompatActivity() {

    private lateinit var search: EditText
    private lateinit var preview: LinkPreview
    private lateinit var card: LinkCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        search = findViewById(R.id.url)
        preview = findViewById(R.id.preview)
        card = findViewById(R.id.card)
        preview.articleColor = ContextCompat.getColor(this, R.color.colorPrimary)
        preview.loadListener = object : LinkListener {

            override fun onError() {
                Toast.makeText(this@MainActivity, "Link loading failed", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(link: PreviewData) {

            }
        }
        preview.clickListener = object : LinkClickListener {

            override fun onLinkClicked(view: View?, url: String) {

            }
        }
    }

    /**
     * Search for the link when the search icon is clicked
     *
     * @param view of search icon
     */
    fun tryText(@Suppress("UNUSED_PARAMETER") view: View?) {
        if (search.text.toString().indexOf(' ') == -1) {
            preview.setLink(search.text.toString())
            card.setLink(search.text.toString())
        } else {
            preview.parseTextForLink(search.text.toString())
            card.parseTextForLink(search.text.toString())
        }

        Utils.hideKeyboard(this@MainActivity)
    }
}
package com.nick.mowen.linkpreview.sample.ui

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.nick.mowen.linkpreview.listener.LinkListener
import com.nick.mowen.linkpreview.sample.R
import com.nick.mowen.linkpreview.view.LinkPreview

class MainActivity : AppCompatActivity() {

    private lateinit var search: EditText
    private lateinit var preview: LinkPreview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        search = findViewById(R.id.url)
        preview = findViewById(R.id.preview)
        preview.articleColor = ContextCompat.getColor(this, R.color.colorPrimary)
        preview.loadListener = object : LinkListener {

            override fun onError() {
                Toast.makeText(this@MainActivity, "Link loading failed", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(link: String) {

            }
        }
    }

    /**
     * Search for the link when the search icon is clicked
     *
     * @param view of search icon
     */
    fun tryText(@Suppress("UNUSED_PARAMETER") view: View?) {
        preview.parseTextForLink(search.text.toString())
    }
}
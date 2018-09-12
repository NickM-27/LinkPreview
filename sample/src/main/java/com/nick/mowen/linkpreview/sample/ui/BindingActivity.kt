package com.nick.mowen.linkpreview.sample.ui

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.nick.mowen.linkpreview.sample.R
import com.nick.mowen.linkpreview.sample.databinding.ActivityBindingBinding

class BindingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityBindingBinding = DataBindingUtil.setContentView(this, R.layout.activity_binding)
        binding.link = "Here is the link www.google.com" //Setting the link variable automatically binds the link to the [LinkPreview]
    }
}
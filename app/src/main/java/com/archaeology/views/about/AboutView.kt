package com.archaeology.views.about

import android.os.Bundle
import com.archaeology.R
import com.archaeology.views.home.HomeView
import kotlinx.android.synthetic.main.drawer_main.*
import org.jetbrains.anko.AnkoLogger

class AboutView : HomeView(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content_frame.removeAllViews()
        layoutInflater.inflate(R.layout.activity_about, content_frame)
    }
} 
package com.example.archaeology.activities

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.archaeology.main.MainApp
import com.example.archaeology.R
import com.example.archaeology.models.SiteModel
import kotlinx.android.synthetic.main.activity_site.*

class SiteActivity : AppCompatActivity(), AnkoLogger {

    var site = SiteModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site)
        info("Archaeology Activity started.")

        app = application as MainApp

        btnAdd.setOnClickListener() {
            site.title = siteTitle.text.toString()
            site.description = siteDescription.text.toString()
            if (site.title.isNotEmpty()) {
                app.sites.add(site.copy())
                info("Add Site Button Pressed: $siteTitle")
                for (i in app.sites.indices) {
                    info("Sites[$i]:${this.app.sites[i]}")
                }
                setResult(AppCompatActivity.RESULT_OK)
                finish()
            }
            else {
                toast("Please Enter a Title")
            }
        }
    }
}

package com.example.archaeology.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_site.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import com.example.archaeology.R
import com.example.archaeology.helpers.readImage
import com.example.archaeology.helpers.readImageFromPath
import com.example.archaeology.helpers.showImagePicker
import com.example.archaeology.main.MainApp
import com.example.archaeology.models.Location
import com.example.archaeology.models.SiteModel

class SiteActivity : AppCompatActivity(), AnkoLogger {

    var site = SiteModel()
    lateinit var app: MainApp
    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2

    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site)
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)
        info("Site Activity started..")

        app = application as MainApp
        var edit = true

        if (intent.hasExtra("site_edit")) {
            edit = true
            site = intent.extras?.getParcelable<SiteModel>("site_edit")!!
            siteName.setText(site.name)
            siteDescription.setText(site.description)
            siteImage.setImageBitmap(readImageFromPath(this, site.image))
            if (site.image != null) {
                btnSelectImage.setText(R.string.change_site_image)
            }
            btnAddSite.setText(R.string.save_site)
        }

        btnAddSite.setOnClickListener() {
            site.name = siteName.text.toString()
            site.description = siteDescription.text.toString()
            if (site.name.isEmpty()) {
                toast(R.string.enter_site_name)
            } else {
                if (edit) {
                    app.sites.update(site.copy())
                } else {
                    app.sites.create(site.copy())
                }
            }
            info("add Button Pressed: $siteName")
            setResult(AppCompatActivity.RESULT_OK)
            finish()
        }

        btnSelectImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)
        }

        btnSelectSiteLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (site.zoom != 0f) {
                location.lat = site.lat
                location.lng = site.lng
                location.zoom = site.zoom
            }
            startActivityForResult(intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_site, menu)
        if (edit && menu != null)
            menu.getItem(0).setVisible(true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_delete -> {
                app.sites.delete(site)
                finish()
            }
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    site.image = data.getData().toString()
                    siteImage.setImageBitmap(readImage(this, resultCode, data))
                    btnSelectImage.setText(R.string.change_site_image)
                }
            }
            LOCATION_REQUEST -> {
                if (data != null) {
                    val location = data.extras?.getParcelable<Location>("location")!!
                    site.lat = location.lat
                    site.lng = location.lng
                    site.zoom = location.zoom
                }
            }
        }
    }
}


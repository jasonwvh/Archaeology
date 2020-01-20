package com.archaeology.views.site

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_site.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import com.example.archaeology.R
import com.archaeology.helpers.readImageFromPath
import com.archaeology.models.SiteModel
import com.archaeology.views.BaseView

class SiteView : BaseView(), AnkoLogger {

    lateinit var presenter: SitePresenter
    var site = SiteModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site)

        init(toolbarAdd)

        presenter = initPresenter(SitePresenter(this)) as SitePresenter

        btnSelectImage.setOnClickListener { presenter.doSelectImage() }

        btnSelectSiteLocation.setOnClickListener { presenter.doSetLocation() }
    }

    override fun showSite(site: SiteModel) {
        siteName.setText(site.name)
        siteDescription.setText(site.description)
        siteImage.setImageBitmap(readImageFromPath(this, site.image))
        if (site.image != null) {
            btnSelectImage.setText(R.string.change_site_image)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_site, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.item_save -> {
                if (siteName.text.toString().isEmpty()) {
                    toast(R.string.enter_site_name)
                } else {
                    presenter.doAddOrSave(siteName.text.toString(), siteDescription.text.toString())
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            presenter.doActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() {
        presenter.doCancel()
    }
}
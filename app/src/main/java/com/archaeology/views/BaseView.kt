package com.archaeology.views

import android.content.Intent
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.archaeology.models.Location
import com.archaeology.models.SiteModel
import com.archaeology.views.editlocation.EditLocationView
import com.archaeology.views.login.LoginView
import com.archaeology.views.map.SiteMapsView
import com.archaeology.views.site.SiteView
import com.archaeology.views.sitelist.SiteListView
import org.jetbrains.anko.AnkoLogger

val IMAGE_REQUEST = 1
val LOCATION_REQUEST = 2

enum class VIEW {
    LOCATION, SITE, MAPS, LIST, LOGIN
}

open abstract class BaseView() : AppCompatActivity(), AnkoLogger {

    var basePresenter: BasePresenter? = null

    fun navigateTo(view: VIEW, code: Int = 0, key: String = "", value: Parcelable? = null) {
        var intent = Intent(this, SiteListView::class.java)
        when (view) {
            VIEW.LOCATION -> intent = Intent(this, EditLocationView::class.java)
            VIEW.SITE -> intent = Intent(this, SiteView::class.java)
            VIEW.MAPS -> intent = Intent(this, SiteMapsView::class.java)
            VIEW.LIST -> intent = Intent(this, SiteListView::class.java)
            VIEW.LOGIN -> intent = Intent(this, LoginView::class.java)
        }
        if (key != "") {
            intent.putExtra(key, value)
        }
        startActivityForResult(intent, code)
    }

    fun initPresenter(presenter: BasePresenter): BasePresenter {
        basePresenter = presenter
        return presenter
    }

    fun init(toolbar: Toolbar, upEnabled: Boolean) {
        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(upEnabled)
    }

    override fun onDestroy() {
        basePresenter?.onDestroy()
        super.onDestroy()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            basePresenter?.doActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        basePresenter?.doRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    open fun showSite(Site: SiteModel) {}
    open fun showSites(Sites: List<SiteModel>) {}
    open fun showLocation(location: Location) {}
    open fun showProgress() {}
    open fun hideProgress() {}
}
package com.archaeology.views.sitelist

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_site_list.*
import com.example.archaeology.R
import com.archaeology.models.SiteModel
import com.archaeology.views.BaseView

class SiteListView : BaseView(), SiteListener {

    lateinit var presenter: SiteListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site_list)
        setSupportActionBar(toolbar)

        presenter = initPresenter(SiteListPresenter(this)) as SiteListPresenter

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        presenter.loadSites()
    }

    override fun showSites(sites: List<SiteModel>) {
        recyclerView.adapter = SiteAdapter(sites, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add -> presenter.doAddSite()
            R.id.item_map -> presenter.doShowSitesMap()
            R.id.item_logout ->presenter.doLogout()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSiteClick(site: SiteModel) {
        presenter.doEditSite(site)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.loadSites()
        super.onActivityResult(requestCode, resultCode, data)
    }
}
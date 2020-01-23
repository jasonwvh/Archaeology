package com.archaeology.views.sitelist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.archaeology.R
import com.archaeology.adapters.SiteListAdapter
import com.archaeology.adapters.SiteListener
import com.archaeology.models.site.SiteModel
import com.archaeology.views.BaseView
import kotlinx.android.synthetic.main.activity_site_list.*
import kotlinx.android.synthetic.main.drawer_main.*

class SiteListView : BaseView(), SiteListener {

    private lateinit var presenter: SiteListPresenter
    private lateinit var ascendingItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_site_list, content_frame)

        presenter = initPresenter(SiteListPresenter(this)) as SiteListPresenter

        val layoutManager = LinearLayoutManager(this)
        siteRecyclerView.layoutManager = layoutManager
        presenter.loadSites()

        siteListFloatingBtn.setOnClickListener {
            presenter.doAddSite()
        }
    }

    override fun showSites(sites: List<SiteModel>) {
        val context = siteRecyclerView.context

        siteRecyclerView.adapter = SiteListAdapter(sites, this)
        siteRecyclerView.adapter?.notifyDataSetChanged()
        siteRecyclerView.scheduleLayoutAnimation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_site_list, menu)
        toolbar.overflowIcon = getDrawable(R.drawable.ic_filter)
        ascendingItem = menu!!.findItem(R.id.sortAscending)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onSiteClick(site: SiteModel) {
        presenter.doEditSite(site)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.loadSites()
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sortByRating -> {
                presenter.doSortByRating()
                item.isChecked = true
                ascendingItem.isChecked = true
            }

            R.id.sortAscending -> {
                presenter.doAscendingOrder()
                item.isChecked = true
            }

            R.id.sortDescending -> {
                presenter.doDescendingOrder()
                item.isChecked = true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
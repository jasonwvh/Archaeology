package com.example.archaeology.activities

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_site_list.*
import kotlinx.android.synthetic.main.card_site.view.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import com.example.archaeology.R
import com.example.archaeology.main.MainApp
import com.example.archaeology.models.SiteModel

class SiteListActivity : AppCompatActivity(), SiteListener {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site_list)
        app = application as MainApp
        toolbar.title = title
        setSupportActionBar(toolbar)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = SiteAdapter(app.sites.findAll(), this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add -> startActivityForResult<SiteActivity>(0)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSiteClick(site: SiteModel) {
        startActivityForResult(intentFor<SiteActivity>().putExtra("site_edit", site), AppCompatActivity.RESULT_OK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }
}
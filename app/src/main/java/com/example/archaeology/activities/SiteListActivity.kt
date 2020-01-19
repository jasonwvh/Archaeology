package com.example.archaeology.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_site_list.*
import kotlinx.android.synthetic.main.card_sites.view.*
import com.example.archaeology.R
import com.example.archaeology.main.MainApp
import com.example.archaeology.models.SiteModel

class SiteListActivity : AppCompatActivity() {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site_list)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = SiteAdapter(app.sites)
    }
}

class SiteAdapter constructor(private var sites: List<SiteModel>) :
    RecyclerView.Adapter<SiteAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.card_sites,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val site = sites[holder.adapterPosition]
        holder.bind(site)
    }

    override fun getItemCount(): Int = sites.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(site: SiteModel) {
            itemView.siteTitle.text = site.title
            itemView.siteDescription.text = site.description
        }
    }
}
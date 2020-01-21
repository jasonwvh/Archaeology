package com.archaeology.views.sitelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.archaeology.R
import com.archaeology.helpers.readImageFromPath
import com.archaeology.models.SiteModel
import kotlinx.android.synthetic.main.card_site.view.*

interface SiteListener {
    fun onSiteClick(site: SiteModel)
}

class SiteAdapter constructor(private var sites: List<SiteModel>,
                                   private val listener: SiteListener
) : RecyclerView.Adapter<SiteAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_site, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val site = sites[holder.adapterPosition]
        holder.bind(site, listener)
    }

    override fun getItemCount(): Int = sites.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(site: SiteModel, listener: SiteListener) {
            itemView.siteName.text = site.name
            itemView.siteDescription.text = site.description
            itemView.imageIcon.setImageBitmap(readImageFromPath(itemView.context, site.image))
            itemView.setOnClickListener { listener.onSiteClick(site) }
        }
    }
}
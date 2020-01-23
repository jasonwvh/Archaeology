package com.archaeology.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.archaeology.R
import com.archaeology.models.site.SiteModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.recycle_item_site.view.*

interface SiteListener {
    fun onSiteClick(site: SiteModel)
}

class SiteListAdapter constructor(
    private var sites: List<SiteModel>,
    private val listener: SiteListener
) : RecyclerView.Adapter<SiteListAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recycle_item_site,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val site = sites[holder.adapterPosition]
        holder.bind(site, listener)
    }

    override fun getItemCount(): Int = sites.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(site: SiteModel, listener: SiteListener) {

            val location =
                "LAT: ${"%.4f".format(site.location.lat)} | LNG: ${"%.4f".format(site.location.lng)}"
            val isVisited = if (site.visited) "Yes" else "No"

            itemView.siteRecycleItemName.text = site.name
            itemView.siteRecycleItemLocation.text = location
            itemView.siteRecycleItemVisited.text = "Visited: $isVisited"
            itemView.siteRecycleItemRating.rating = site.rating.toFloat()
            if (site.isFavourite) {
                itemView.siteRecycleItemFavouriteIcon.setBackgroundResource(
                    R.drawable.ic_favorite
                )
            }

            if (site.images.size != 0) {
                Glide.with(itemView.context).load(site.images[0].uri).centerCrop()
                    .into(itemView.siteRecycleItemImageIcon)
            } else {
                itemView.siteRecycleItemImageIcon.setImageResource(R.drawable.placeholder)
            }
            itemView.setOnClickListener { listener.onSiteClick(site) }
        }
    }
}
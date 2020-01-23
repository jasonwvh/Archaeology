package com.archaeology.views.site

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.archaeology.R
import com.archaeology.adapters.NoteListener
import com.archaeology.adapters.SiteImageAdapter
import com.archaeology.adapters.SiteNotesAdapter
import com.archaeology.helpers.RecyclerViewHelper
import com.archaeology.models.site.ImageModel
import com.archaeology.models.site.Location
import com.archaeology.models.site.NoteModel
import com.archaeology.models.site.SiteModel
import com.archaeology.views.BaseView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import kotlinx.android.synthetic.main.activity_site.*
import kotlinx.android.synthetic.main.content_site_fab.*
import kotlinx.android.synthetic.main.drawer_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast

class SiteView : BaseView(), NoteListener, AnkoLogger {

    private lateinit var presenter: SitePresenter
    private var location = Location()
    private var isFabOpen = false

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_site, content_frame)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        val imageViewPager = findViewById<ViewPager>(R.id.viewPager)
        imageViewPager.setBackgroundResource(R.drawable.placeholder)

        presenter = initPresenter(SitePresenter(this)) as SitePresenter

        with(siteMapView) {
            onCreate(savedInstanceState)
            getMapAsync {
                presenter.doConfigureMap(it)
                it.setOnMapClickListener { presenter.doSetLocation() }
            }
        }

        siteDateVisited.setOnClickListener {
            showDatePickerDialog()
        }

        val fabOpen = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_menu_open)
        val fabClose = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_menu_close)
        val fabClockwise =
            AnimationUtils.loadAnimation(applicationContext, R.anim.fab_rotate_clockwise)
        val fabAntiClockwise =
            AnimationUtils.loadAnimation(applicationContext, R.anim.fab_rotate_anticlockwise)

        fabMore.setOnClickListener {
            if (isFabOpen) {
                fabTextFavourite.visibility = View.INVISIBLE
                fabTextDelete.visibility = View.INVISIBLE

                fabMoreFavourite.startAnimation(fabClose)
                fabMoreDelete.startAnimation(fabClose)
                fabMore.startAnimation(fabAntiClockwise)

                fabMoreFavourite.isClickable = false
                fabMoreDelete.isClickable = false

                isFabOpen = false
            } else {
                fabTextFavourite.visibility = View.VISIBLE
                fabTextDelete.visibility = View.VISIBLE

                fabMoreFavourite.startAnimation(fabOpen)
                fabMoreDelete.startAnimation(fabOpen)
                fabMore.startAnimation(fabClockwise)

                fabMoreFavourite.isClickable = true
                fabMoreDelete.isClickable = true

                isFabOpen = true
            }
        }

        fabMoreFavourite.setOnClickListener {
            it.startAnimation(
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.fab_favourite_click
                )
            )
            presenter.doFavourite()
        }

        fabMoreDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to delete this Site?")
            builder.setPositiveButton("Yes") { dialog, _ ->
                presenter.doDelete()
                dialog.dismiss()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        siteSaveFAB.setOnClickListener {
            if (listOf(
                    siteName.text.toString(),
                    siteDescription.text.toString()
                ).contains("")
            ) {
                toast("Please fill out all fields")
            } else if (siteVisited.isChecked && siteDateVisited.text.toString() == "") {
                toast("Site is visited, please provide a date")
            } else {
                val tempSite = SiteModel()
                tempSite.name = siteName.text.toString().trim()
                tempSite.description = siteDescription.text.toString().trim()
                tempSite.visited = siteVisited.isChecked
                tempSite.dateVisited = siteDateVisited.text.toString().trim()
                tempSite.location = location
                tempSite.rating = siteRatingBar.rating.toInt()

                presenter.doAddOrSave(tempSite)

            }
        }

        siteAddNoteBtn.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_note, null)
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Enter note details: ")
            builder.setView(mDialogView)

            val dialog: AlertDialog = builder.create()
            dialog.show()

            val addBtn = dialog.findViewById(R.id.noteDialogAddBtn) as Button
            val cancelBtn = dialog.findViewById(R.id.noteDialogCancelBtn) as Button
            val noteTitle = dialog.findViewById(R.id.noteDialogTitle) as? EditText
            val noteContent = dialog.findViewById(R.id.noteDialogContent) as? EditText

            addBtn.setOnClickListener {

                if (listOf(noteTitle!!.text.toString(), noteContent!!.text.toString())
                        .contains("")
                ) {
                    toast("Please fill out all fields!")
                } else {
                    presenter.doAddNote(noteTitle.text.toString(), noteContent.text.toString())
                    dialog.dismiss()
                }
            }
            cancelBtn.setOnClickListener {
                dialog.dismiss()
            }
        }

        siteChooseImageBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("This will reset the existing images, continue?")
            builder.setPositiveButton("YES") { dialog, _ ->
                presenter.doSelectImage()
                dialog.dismiss()
            }

            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    override fun onNoteClick(noteModel: NoteModel) {
        presenter.doClickNote(noteModel)
    }

    override fun showSite(site: SiteModel) {
        siteName.setText(site.name)
        siteDescription.setText(site.description)
        siteVisited.isChecked = site.visited
        siteDateVisited.setText(site.dateVisited)
        siteRatingBar.rating = site.rating.toFloat()

        showNotes(site.notes)
        showImages(site.images)
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun showDatePickerDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                siteDateVisited.setText("$dayOfMonth/${monthOfYear + 1}/$year")
            },
            year, month, day
        )
        dpd.show()
    }


    private fun setMapLocation(map: GoogleMap, location: LatLng) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 5f))
        with(map) {
            addMarker(
                MarkerOptions().position(
                    location
                )
            )
            mapType = GoogleMap.MAP_TYPE_NORMAL
        }
    }

    override fun showNotes(notes: ArrayList<NoteModel>?) {
        val layoutManager = LinearLayoutManager(this)
        val recyclerNotes = findViewById<RecyclerView>(R.id.recyclerNotes)
        recyclerNotes.layoutManager = layoutManager
        if (notes != null) {
            recyclerNotes.adapter =
                SiteNotesAdapter(notes, this)
            recyclerNotes.adapter?.notifyDataSetChanged()

            val swipeHandler = object : RecyclerViewHelper(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = SiteNotesAdapter(notes, this@SiteView)
                    adapter.removeAt(viewHolder.adapterPosition)
                    (recyclerNotes.adapter as SiteNotesAdapter).notifyDataSetChanged()
                }
            }

            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(recyclerNotes)
        }
    }


    override fun showImages(images: ArrayList<ImageModel>) {
        val imageViewPager = findViewById<ViewPager>(R.id.viewPager)
        val dotsIndicator = findViewById<DotsIndicator>(R.id.dotsIndicator)
        if (images != null) {
            imageViewPager.adapter =
                SiteImageAdapter(
                    images,
                    this
                )
            dotsIndicator.setViewPager(imageViewPager)
            imageViewPager.adapter?.notifyDataSetChanged()
        }
    }

    override fun showUpdatedMap(latLng: LatLng) {

        siteCurrentLocationText.text =
            "LAT: ${"%.4f".format(latLng.latitude)} | LNG: ${"%.4f".format(latLng.longitude)}"

        siteMapView.getMapAsync { it.clear() }
        siteMapView.getMapAsync {
            setMapLocation(it, latLng)
        }
    }

    public override fun onResume() {
        super.onResume()
        siteMapView.onResume()
        presenter.doRestartLocationUpdates()
    }

    public override fun onPause() {
        super.onPause()
        siteMapView.onPause()
    }

    public override fun onDestroy() {
        super.onDestroy()
        siteMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        siteMapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        siteMapView.onSaveInstanceState(outState)
    }
}
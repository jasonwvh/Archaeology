package com.archaeology.views.main

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.archaeology.R
import com.archaeology.main.MainApp
import com.archaeology.views.login.LoginView
import com.archaeology.views.map.SiteMapsView
import com.archaeology.views.more.ExtraView
import com.archaeology.views.sitelist.SiteListView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_main.*
import org.jetbrains.anko.AnkoLogger

open class MainView : AppCompatActivity(), AnkoLogger {

    lateinit var bottomNavBar: BottomNavigationView
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_main)
        layoutInflater.inflate(R.layout.activity_main, content_frame)

        app = application as MainApp

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val user: FirebaseUser? = app.activeUser
        if (user != null) {
            mainActivityUsernameText.text = user.email!!.split('@')[0]
        }

        bottomNavBar = findViewById(R.id.bottom_navigation)
        bottomNavBar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomNavBar.menu.findItem(R.id.navigation_home).isChecked = true

    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(
                        Intent(this, MainView::class.java),
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                    )

                }
                R.id.navigation_sites -> {
                    startActivity(
                        Intent(this, SiteListView::class.java),
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                    )
                }
                R.id.navigation_map -> {
                    startActivity(
                        Intent(this, SiteMapsView::class.java),
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                    )

                }
                R.id.navigation_more -> {
                    startActivity(
                        Intent(this, ExtraView::class.java),
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                    )
                }
                R.id.navigation_logout -> {
                    app.sites.logout()
                    app.activeUser = null
                    startActivity(
                        Intent(this, LoginView::class.java),
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                    )
                }
            }
            true
        }

    open fun setNavigationBarItem() {}

}
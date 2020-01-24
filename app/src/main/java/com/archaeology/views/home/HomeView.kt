package com.archaeology.views.home

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.archaeology.R
import com.archaeology.main.MainApp
import com.archaeology.views.about.AboutView
import com.archaeology.views.account.AccountView
import com.archaeology.views.login.LoginView
import com.archaeology.views.map.SiteMapsView
import com.archaeology.views.site.SiteView
import com.archaeology.views.sitelist.SiteListView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_main.*
import org.jetbrains.anko.AnkoLogger

open class HomeView : AppCompatActivity(), AnkoLogger {
    private lateinit var mDrawerLayout: DrawerLayout
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_main)
        layoutInflater.inflate(R.layout.activity_main, content_frame)

        app = application as MainApp

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        mDrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)

        navigationView.itemIconTintList = null

        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            mDrawerLayout.closeDrawers()

            when (menuItem.itemId) {
                R.id.nav_sites -> {
                    startActivity(
                        Intent(this@HomeView, SiteListView::class.java),
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                    )
                }

                R.id.nav_site_maps -> {
                    startActivity(
                        Intent(this@HomeView, SiteMapsView::class.java),
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                    )
                }

                R.id.nav_add -> {
                    startActivity(
                        Intent(this@HomeView, SiteView::class.java),
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                    )
                }

                R.id.nav_account -> {
                    startActivity(
                        Intent(this@HomeView, AccountView::class.java),
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                    )
                }

                R.id.nav_about -> {
                    startActivity(
                        Intent(this@HomeView, AboutView::class.java),
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                    )
                }

                R.id.nav_logout -> {
                    app.users.logout()
                    app.activeUser = null
                    startActivity(
                        Intent(this@HomeView, LoginView::class.java),
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                    )
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
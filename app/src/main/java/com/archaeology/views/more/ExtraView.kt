package com.archaeology.views.more

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.archaeology.R
import com.archaeology.views.BaseView
import com.archaeology.views.more.fragments.about.AboutFragment

import kotlinx.android.synthetic.main.drawer_main.*
import org.wit.hillfortapp.views.more.fragments.account.AccountFragment

class ExtraView : BaseView() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content_frame.removeAllViews()
        layoutInflater.inflate(R.layout.activity_extra, content_frame)

        bottomNavBar.menu.findItem(R.id.navigation_more).isChecked = true

        // Source: https://code.luasoftware.com/tutorials/android/setup-android-viewpager2-with-tablayout-and-fragment/
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> {
                        AccountFragment.newInstance()
                    }
                    1 -> {
                        AboutFragment.newInstance()
                    }
                    2 -> {
                        StatsFragment.newInstance()
                    }
                    else -> {
                        AccountFragment.newInstance()
                    }
                }
            }

            override fun getItemCount(): Int {
                return 3
            }
        }

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Account"
                1 -> "About"
                2 -> "Stats"
                else -> "Account"
            }
        }.attach()
    }
}


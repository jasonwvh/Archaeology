package com.archaeology.views.more

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.archaeology.R
import com.archaeology.views.BaseView
import com.archaeology.views.more.fragments.about.AboutFragment
import com.archaeology.views.more.fragments.account.AccountFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_extra.*
import kotlinx.android.synthetic.main.drawer_main.*

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
                    else -> {
                        AboutFragment.newInstance()
                    }
                }
            }

            override fun getItemCount(): Int {
                return 2
            }
        }

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Account"
                else -> "About"
            }
        }.attach()
    }
}


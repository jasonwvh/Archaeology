package com.archaeology.views.more.fragments.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.archaeology.R
import com.archaeology.views.more.fragments.BaseFragment
import org.jetbrains.anko.AnkoLogger

class AboutFragment : BaseFragment(), AnkoLogger {

    companion object {
        fun newInstance() = AboutFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }
}
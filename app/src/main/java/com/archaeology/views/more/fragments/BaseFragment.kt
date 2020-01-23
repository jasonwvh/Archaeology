package com.archaeology.views.more.fragments

import androidx.fragment.app.Fragment
import org.jetbrains.anko.AnkoLogger

abstract class BaseFragment : Fragment(), AnkoLogger {

    private var baseFragmentPresenter: BaseFragmentPresenter? = null

    fun initPresenter(presenter: BaseFragmentPresenter): BaseFragmentPresenter {
        baseFragmentPresenter = presenter
        return presenter
    }

}
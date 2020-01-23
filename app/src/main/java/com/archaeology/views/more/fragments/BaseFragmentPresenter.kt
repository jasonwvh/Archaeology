package com.archaeology.views.more.fragments

import com.archaeology.main.MainApp

open class BaseFragmentPresenter(fragment: BaseFragment) {
    open var app: MainApp = fragment.activity?.application as MainApp
}
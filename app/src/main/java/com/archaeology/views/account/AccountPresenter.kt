package com.archaeology.views.account

import com.archaeology.views.BasePresenter
import com.archaeology.views.BaseView
import com.archaeology.views.VIEW
import org.jetbrains.anko.toast

class AccountPresenter(view: BaseView) : BasePresenter(view) {

    init {
        view.showAccount(app.activeUser!!)
    }

    fun doUpdateEmail(email: String) {
        app.activeUser?.updateEmail(email)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    view?.toast("Email address updated.")
                    app.users.logout()
                    app.activeUser = null
                    view?.navigateTo(VIEW.LOGIN)
                } else {
                    view?.toast("Email change failed: ${task.exception?.message}")
                }
            }
    }

    fun doUpdatePassword(password: String) {
        app.activeUser?.updatePassword(password)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    view?.toast("Password updated.")
                    app.users.logout()
                    app.activeUser = null
                    view?.navigateTo(VIEW.LOGIN)
                } else {
                    view?.toast("Password change failed: ${task.exception?.message}")
                }
            }
    }
}


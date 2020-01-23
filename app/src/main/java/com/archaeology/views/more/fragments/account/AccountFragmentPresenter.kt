package com.archaeology.views.more.fragments.account

import android.content.Intent
import com.archaeology.views.login.LoginView
import com.archaeology.views.more.fragments.BaseFragmentPresenter
import com.google.firebase.auth.FirebaseUser
import org.jetbrains.anko.toast


class AccountFragmentPresenter(private val fragment: AccountFragment) :
    BaseFragmentPresenter(fragment) {

    fun doGetUser(): FirebaseUser? {
        return app.activeUser
    }

    fun doUpdateEmail(email: String) {
        app.activeUser?.updateEmail(email)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                fragment.activity?.toast("User email address updated.")
                app.users.logout()
                app.activeUser = null
                fragment.startActivity(Intent(fragment.activity, LoginView::class.java))
            } else {
                fragment.activity?.toast("Email Change Failed: ${task.exception?.message}")
            }
        }
    }

    fun doUpdatePassword(password: String) {
        app.activeUser?.updatePassword(password)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                fragment.activity?.toast("User password updated.")
                app.users.logout()
                app.activeUser = null
                fragment.startActivity(Intent(fragment.activity, LoginView::class.java))
            } else {
                fragment.activity?.toast("Password Change Failed: ${task.exception?.message}")
            }
        }
    }
}
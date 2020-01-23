package com.archaeology.views.login

import com.archaeology.models.firebase.SiteFireStore
import com.archaeology.views.BasePresenter
import com.archaeology.views.BaseView
import com.archaeology.views.VIEW
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class LoginPresenter(view: BaseView) : BasePresenter(view) {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var fireStore: SiteFireStore? = null

    init {
        if (app.sites is SiteFireStore) {
            fireStore = app.sites as SiteFireStore
        }
    }

    fun doLogin(email: String, password: String) {
        view?.showProgress()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                app.activeUser = auth.currentUser!!
                view?.info(fireStore)
                if (fireStore != null) {
                    doAsync {
                        fireStore!!.fetchSites {
                            view?.hideProgress()
                            view?.navigateTo(VIEW.MAIN, 0, "user", auth.currentUser)
                        }
                    }
                } else {
                    view?.hideProgress()
                    view?.navigateTo(VIEW.MAIN)
                }
            } else {
                view?.hideProgress()
                view?.toast("Sign Up Failed: ${task.exception?.message}")
            }
        }
    }
}
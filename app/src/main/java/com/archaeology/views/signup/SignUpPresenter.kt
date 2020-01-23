package com.archaeology.views.signup

import com.archaeology.models.UserModel
import com.archaeology.views.BasePresenter
import com.archaeology.views.BaseView
import com.archaeology.views.VIEW
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.toast

class SignUpPresenter(view: BaseView) : BasePresenter(view) {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun doSignup(user: UserModel) {
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener(view!!) { task ->
                if (task.isSuccessful) {
                    view?.navigateTo(VIEW.LOGIN)
                } else {
                    view?.toast("Sign Up Failed: ${task.exception?.message}")
                }
            }
    }
}
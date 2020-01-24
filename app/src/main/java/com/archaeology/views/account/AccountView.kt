package com.archaeology.views.account

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import com.archaeology.R
import com.archaeology.views.BaseView
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.drawer_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast


class AccountView : BaseView(), AnkoLogger {

    private lateinit var presenter: AccountPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content_frame.removeAllViews()
        layoutInflater.inflate(R.layout.activity_account, content_frame)

        presenter = AccountPresenter(this)

        accountChangeEmail.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_account_email, null)
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Enter your new email address.")
            builder.setView(mDialogView)

            val dialog: AlertDialog = builder.create()
            dialog.show()

            val updateBtn = dialog.findViewById(R.id.accountDialogUpdate) as Button
            val cancelBtn = dialog.findViewById(R.id.accountDialogCancel) as Button
            val emailField = dialog.findViewById(R.id.accountDialogEmail) as? EditText

            updateBtn.setOnClickListener {
                when {
                    emailField?.text.toString() == "" -> toast("Please fill out all fields")
                    !isEmailValid(emailField?.text.toString()) -> toast("Please enter a valid email")
                    else -> {
                        presenter.doUpdateEmail(emailField?.text.toString())
                    }
                }
            }
            cancelBtn.setOnClickListener {
                dialog.dismiss()
            }
        }

        accountChangePassword.setOnClickListener {
            val mDialogView =
                LayoutInflater.from(this).inflate(R.layout.dialog_account_password, null)
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Enter your new password")
            builder.setView(mDialogView)

            val dialog: AlertDialog = builder.create()
            dialog.show()

            val updateBtn = dialog.findViewById(R.id.accountDialogPasswordUpdate) as Button
            val cancelBtn = dialog.findViewById(R.id.accountDialogPasswordCancel) as Button
            val password = dialog.findViewById(R.id.accountDialogPassword) as? EditText
            val passwordConfirm =
                dialog.findViewById(R.id.accountDialogPasswordConfirm) as? EditText

            updateBtn.setOnClickListener {
                when {
                    listOf(
                        password?.text.toString(),
                        passwordConfirm?.text.toString()
                    ).contains(
                        ""
                    ) -> toast("Please enter both fields")
                    password!!.text.toString() != passwordConfirm!!.text.toString() -> toast("Passwords do not match")
                    else -> {
                        presenter.doUpdatePassword(password.text.toString())
                    }
                }
            }
            cancelBtn.setOnClickListener {
                dialog.dismiss()
            }

        }
    }

    override fun showAccount(user: FirebaseUser) {
        accountEmail.text = user.email
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}
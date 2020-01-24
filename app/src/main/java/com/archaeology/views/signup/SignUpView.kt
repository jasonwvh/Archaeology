package com.archaeology.views.signup

import android.os.Bundle
import android.widget.EditText
import com.archaeology.R
import com.archaeology.models.user.UserModel
import com.archaeology.views.BaseView
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import kotlin.random.Random

class SignUpView : BaseView(), AnkoLogger {

    private var user = UserModel()
    private lateinit var presenter: SignUpPresenter

    private var email: EditText? = null
    private var password: EditText? = null
    private var password2: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        presenter = SignUpPresenter(this)

        email = findViewById(R.id.signUpEmailInput)
        password = findViewById(R.id.signUpPasswordInput)
        password2 = findViewById(R.id.signUpPasswordInput2)

        signUpButton.setOnClickListener { signUp() }
    }

    private fun signUp() {
        val emailText = email?.text.toString().trim()
        val password1Text = password?.text.toString().trim()
        val password2Text = password2?.text.toString().trim()

        if (!validationCheck(emailText, password1Text, password2Text)) {
            user.email = emailText
            user.password = password1Text
            user.id = Random.nextInt()
            presenter.doSignup(user)
            toast("Please login with your new account")
        }
    }

    private fun validationCheck(
        emailText: String,
        password1Text: String,
        password2Text: String
    ): Boolean {

        var hasErrors = false

        when {
            listOf(emailText, password1Text, password2Text).contains("") -> {
                toast("Please enter every fields")
                hasErrors = true
            }
            password1Text != password2Text -> {
                toast("Passwords do not match")
                hasErrors = true
            }

            !isEmailValid(emailText) -> {
                toast("Email is not valid")
                hasErrors = true
            }
        }
        return hasErrors
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
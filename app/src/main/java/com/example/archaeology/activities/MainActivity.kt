package com.example.archaeology.activities

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.archaeology.R
import com.example.archaeology.models.MainModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AnkoLogger {

    var main = MainModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        info("Archaeology Activity started.")

        btnAdd.setOnClickListener() {
            main.title = siteTitle.text.toString()
            if (main.title.isNotEmpty()) {
                info("Add Site Button Pressed: $siteTitle")
            }
            else {
                toast("Please Enter a Title")
            }
        }
    }
}

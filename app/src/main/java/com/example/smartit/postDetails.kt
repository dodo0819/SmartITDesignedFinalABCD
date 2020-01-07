package com.example.smartit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_post_details.*

class postDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)

        textView1.text= intent.getStringExtra("Username")
        textView2.text= intent.getStringExtra("Date")
        textView3.text= intent.getStringExtra("Title")
        textView4.text= intent.getStringExtra("Content")

    }
}

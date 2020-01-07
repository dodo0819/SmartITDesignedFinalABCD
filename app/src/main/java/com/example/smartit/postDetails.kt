package com.example.smartit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post_details.*

class postDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)

        textView1.text= intent.getStringExtra("Username")
        textView2.text= intent.getStringExtra("Date")
        textView3.text= intent.getStringExtra("Title")
        textView4.text= intent.getStringExtra("Content")

        Picasso.get().load(intent.getStringExtra("ProfilePhoto")).placeholder(R.drawable.profile).into(profilePic)
        Picasso.get().load(intent.getStringExtra("PostPhoto")).into(postPhoto)
    }
}

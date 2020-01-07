package com.example.smartit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post_details.*

class postDetails : AppCompatActivity() {

    lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)

        textView1.text= intent.getStringExtra("Username")
        textView2.text= intent.getStringExtra("Date")
        textView3.text= intent.getStringExtra("Title")
        textView4.text= intent.getStringExtra("Content")

        Picasso.get().load(intent.getStringExtra("ProfilePhoto")).placeholder(R.drawable.profile).into(profilePic)
        Picasso.get().load(intent.getStringExtra("PostPhoto")).into(postPhoto)

        like.setOnClickListener{
            love()
        }
    }

    private fun love(){

        ref = FirebaseDatabase.getInstance().getReference("Like")

        val likeID = ref.push().key.toString()

        val storeLike = Like(
            likeID,
            intent.getStringExtra("UserID")!!,
            intent.getStringExtra("PostID")!!,
            "loved"

        )

        ref.child(likeID).setValue(storeLike)

        like.setImageResource(R.drawable.heart_clicked)

    }
}

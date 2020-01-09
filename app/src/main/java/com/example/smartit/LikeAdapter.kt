package com.example.smartit

import android.app.ProgressDialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class LikeAdapter(val likes : MutableList<Like>) : RecyclerView.Adapter<LikeAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeAdapter.ViewHolder {

        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.posts,parent,false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = likes.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}
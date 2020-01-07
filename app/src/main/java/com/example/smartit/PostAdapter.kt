package com.example.smartit

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class PostAdapter(val posts : MutableList<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {

        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.posts,parent,false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = posts.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = posts[position].title
        holder.date.text = posts[position].date
        holder.content.text = posts[position].content
        holder.username.text = posts[position].username

        val profilePhoto = "https://firebasestorage.googleapis.com/v0/b/smartit-3dd97.appspot.com/o/Default%20images%2F80829044_599648000836119_5366044058433093632_n.jpg?alt=media&token=52f26a9a-9481-46d2-a6fe-506980d0a47e"

        Picasso.get().load(profilePhoto).placeholder(R.drawable.profile).into(holder.profilePic)


        holder.title.setOnClickListener{
            Toast.makeText(holder.title.context, "You click the title", Toast.LENGTH_SHORT).show()
            //holder.date.text = "You click the title"
            val intent = Intent(holder.title.context, postDetails::class.java)
            intent.putExtra("Title", posts[position].title)
            intent.putExtra("Date", posts[position].date)
            intent.putExtra("Content", posts[position].content)
            intent.putExtra("Username", posts[position].username)
            holder.title.context.startActivity(intent)

        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val date: TextView = itemView.findViewById(R.id.date)
        val content: TextView = itemView.findViewById(R.id.content)
        val username: TextView = itemView.findViewById(R.id.username)
        val profilePic : CircleImageView = itemView.findViewById((R.id.profilePic))

    }

}
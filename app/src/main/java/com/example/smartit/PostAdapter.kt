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

class PostAdapter(val posts : MutableList<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>(){

    lateinit var ref : DatabaseReference
    lateinit var query: Query


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

        val postPhoto = posts[position].postPhoto

        val intent = Intent(holder.title.context, postDetails::class.java)

        query = FirebaseDatabase.getInstance().getReference("User").orderByChild("username").equalTo(posts[position].username)

        query.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {

                if(p0.exists()){

                    for(h in p0.children){

                        val targetUser = h.getValue(User::class.java)
                        val user = targetUser!!.username
                        //Toast.makeText(holder.content.context, "WTF is this " + user, Toast.LENGTH_SHORT).show()
                        val profilePhoto = targetUser!!.profilePic
                        Picasso.get().load(profilePhoto).placeholder(R.drawable.profile).into(holder.profilePic)
                        intent.putExtra("ProfilePhoto", profilePhoto)
                    }
                    //Log.d("Profile", "profile = " + user)
                    //val profilePhoto = targetUser!!.profilePic
                    //Log.d("Profile", "profile = " + profilePhoto)
                    //Picasso.get().load(profilePhoto).placeholder(R.drawable.profile).into(holder.profilePic)
                }
            }

        })

        //val profilePhoto = posts[position]
        Picasso.get().load(postPhoto).placeholder(R.drawable.profile).into(holder.postPhoto)

        holder.title.setOnClickListener{
            Toast.makeText(holder.title.context, "You click the title", Toast.LENGTH_SHORT).show()
            //holder.date.text = "You click the title"
            //val intent = Intent(holder.title.context, postDetails::class.java)
            intent.putExtra("Title", posts[position].title)
            intent.putExtra("Date", posts[position].date)
            intent.putExtra("Content", posts[position].content)
            intent.putExtra("Username", posts[position].username)
            intent.putExtra("PostPhoto", posts[position].postPhoto)
            holder.title.context.startActivity(intent)

        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val date: TextView = itemView.findViewById(R.id.date)
        val content: TextView = itemView.findViewById(R.id.content)
        val username: TextView = itemView.findViewById(R.id.username)
        val profilePic : CircleImageView = itemView.findViewById((R.id.profilePic))
        val postPhoto : ImageView = itemView.findViewById((R.id.postPhoto))
    }

}
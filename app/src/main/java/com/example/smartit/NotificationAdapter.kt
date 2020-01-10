
package com.example.smartit

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.concurrent.schedule

class NotificationAdapter(val notifications : MutableList<Notification>) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>(){

    lateinit var query: Query
    lateinit var postList:MutableList<Post>
    lateinit var post123 : Post
    var stop : Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.ViewHolder {

        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.notifications,parent,false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = notifications.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        postList = mutableListOf()
        //Toast.makeText(holder.message.context, "WTF is this " + notifications[position].type, Toast.LENGTH_SHORT).show()
        holder.message.text = notifications[position].type
        holder.date.text = notifications[position].date
        val sender = notifications[position].sender
        //holder.date.text = posts[position].date

        if(notifications[position].status.equals("false")){
            holder.details.setCardBackgroundColor(Color.rgb(135,206,250))
        }


        query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("uid").equalTo(sender)

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
                        val profilePhoto = targetUser!!.image

                        holder.sender.text = user
                        Picasso.get().load(profilePhoto).placeholder(R.drawable.profile).into(holder.senderPic)

                    }

                }
            }

        })


            holder.details.setOnClickListener{
                stop==false

                query = FirebaseDatabase.getInstance().getReference("Post").child(notifications[position].postID)

                query.addValueEventListener(object: ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        if (p0.exists()) {



                            val post = p0.getValue<Post>(Post::class.java)
                            postList.add(post!!)
                            CountOrder.getPost = post
                            Log.d("hellowtf122",notifications[position].postID )

                            //Post ID de user
                            query = FirebaseDatabase.getInstance().getReference("Users")
                                .orderByChild("uid").equalTo(postList[0].userID)

                            query.addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }

                                override fun onDataChange(p0: DataSnapshot) {

                                    if (p0.exists()) {

                                        for (h in p0.children) {

                                            val targetUser = h.getValue(User::class.java)
                                            CountOrder.getUser= targetUser!!
                                            val user = targetUser!!.username
                                            //Toast.makeText(holder.content.context, "WTF is this " + user, Toast.LENGTH_SHORT).show()
                                            val profilePhoto = targetUser!!.image

                                            //intent.putExtra("Username", user)

                                            //intent.putExtra("ProfilePhoto", profilePhoto)

                                            val intent = Intent(holder.details.context, postDetails::class.java)
                                            intent.putExtra("PostID", CountOrder.getPost.postID)
                                            intent.putExtra("Title", CountOrder.getPost.title)
                                            intent.putExtra("Date", CountOrder.getPost.date)
                                            intent.putExtra("Content", CountOrder.getPost.content)
                                            intent.putExtra("UserID", CountOrder.getPost.userID)
                                            intent.putExtra("PostPhoto", CountOrder.getPost.postPhoto)
                                            intent.putExtra("Username", CountOrder.getUser.username)
                                            intent.putExtra("ProfilePhoto", CountOrder.getUser.image)
                                            holder.details.context.startActivity(intent)
                                        }

                                    }
                                }

                            })
                        }


                    }

                })
                Timer("SettingUp", false).schedule(1500) {
                    query = FirebaseDatabase.getInstance().getReference("Notification")
                        .child(notifications[position].notifcationID)
                    query.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onDataChange(p0: DataSnapshot) {

                            if (p0.exists()) {
                                val noti = p0.getValue(Notification::class.java)
                                if(stop==false){
                                    noti!!.status = "true"
                                    stop = true
                                }


                                FirebaseDatabase.getInstance().getReference("Notification")
                                    .child(notifications[position].notifcationID)
                                    .setValue(noti)

                            }
                        }
                    })
                }


            }

        //}
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message: TextView = itemView.findViewById(R.id.message)
        val sender: TextView = itemView.findViewById(R.id.sender)
        val date: TextView = itemView.findViewById(R.id.date)
        val senderPic: CircleImageView = itemView.findViewById(R.id.senderPic)
        val details: CardView = itemView.findViewById(R.id.details)
        val notificationIDHolder : TextView = itemView.findViewById(R.id.hidden)
    }

}

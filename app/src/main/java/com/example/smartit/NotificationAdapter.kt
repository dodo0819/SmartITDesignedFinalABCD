
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

class NotificationAdapter(val notifications : MutableList<Notification>) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>(){

    lateinit var query: Query


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.ViewHolder {

        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.notifications,parent,false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = notifications.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //Toast.makeText(holder.message.context, "WTF is this " + notifications[position].type, Toast.LENGTH_SHORT).show()
        holder.message.text = notifications[position].type
        val sender = notifications[position].sender
        //holder.date.text = posts[position].date

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
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message: TextView = itemView.findViewById(R.id.message)
        val sender: TextView = itemView.findViewById(R.id.sender)
        val senderPic: CircleImageView = itemView.findViewById(R.id.senderPic)
    }

}
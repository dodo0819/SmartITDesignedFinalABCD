
package com.example.smartit

import android.app.ProgressDialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*
import java.util.Timer
import kotlin.concurrent.schedule

class CommentAdapter(val comments : MutableList<Comment>) : RecyclerView.Adapter<CommentAdapter.ViewHolder>(){

    lateinit var query: Query
    lateinit var query1: Query

    lateinit var ref1:DatabaseReference
    lateinit var ref2:DatabaseReference
    lateinit var ref3:DatabaseReference

    var stop : Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.ViewHolder {

        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.comments,parent,false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = comments.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentUser = FirebaseAuth.getInstance().currentUser!!.uid

        if (!(currentUser.equals(comments[position].userID))) {
            holder.removeButton.isVisible = false
        }
        //get Comment Like Count
        query = FirebaseDatabase.getInstance().getReference("CommentLike")
            .child(comments[position].commentID)


        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {
                    CountOrder.commentCount = (p0.childrenCount.toString()).toInt()
                    //Log.d("totalccc", "TOtal = " + CountOrder.total)
                    holder.likeCount.text = CountOrder.commentCount.toString()
                }
                else{
                    holder.likeCount.text = "0"
                }

            }
        })



        //get Key first
        query1 = FirebaseDatabase.getInstance().getReference("CommentNotification")
            .child(comments[position].postID)
            .child(currentUser)
            .child(comments[position].commentID)


        query1.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    getKeyy.key = p0.value.toString()
                }
            }
        })

        holder.content.text = comments[position].commentContent
        holder.date.text = comments[position].date

        query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("uid")
            .equalTo(comments[position].userID)

        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {

                    for (h in p0.children) {

                        val targetUser = h.getValue(User::class.java)
                        val user = targetUser!!.username
                        //Toast.makeText(holder.content.context, "WTF is this " + user, Toast.LENGTH_SHORT).show()
                        val profilePhoto = targetUser!!.image


                        holder.username.text = user
                        Picasso.get().load(profilePhoto).placeholder(R.drawable.profile)
                            .into(holder.profilePic)

                    }

                }
            }

        })

        holder.removeButton.setOnClickListener {

            query1 =FirebaseDatabase.getInstance().getReference("CommentNotification")
                .child(comments[position].postID)
                .child(currentUser)
                .child(comments[position].commentID)


            query1.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        //for(h in p0.children){
                        getKeyy.key = p0.value.toString()
                        FirebaseDatabase.getInstance().getReference("Notification")
                            .child(getKeyy.key)
                            .removeValue()
                        //}
                    }
                }
            })



            ////Remove all like and like notification of comment////
            //remove comment Like//

            //Get all user

            ref1 = FirebaseDatabase.getInstance().getReference("Users")

            ref1.addValueEventListener(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (h in p0.children) {
                            Log.d("Userrrrr", "yser ud = " + h.key)
                            query = FirebaseDatabase.getInstance().getReference("CommentLikeNotification")
                                .child(comments[position].commentID)
                                .child(h.key!!)

                            //get the fcking notifcation ID first
                            query.addValueEventListener(object: ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    if(p0.exists()) {
                                                        getKey.key1 = p0.value.toString()
                                                        FirebaseDatabase.getInstance().getReference("Notification")
                                                            .child(getKey.key1)
                                                            .removeValue()

                                    }
                                    else{
                                        Toast.makeText(holder.content.context, "wtf = Not exist zzz" ,Toast.LENGTH_SHORT).show()
                                    }

                                }
                            })

                        }
                    }
                }
            })



            //Get notification key  to remove the Notification

            //Remove commentLIke
            FirebaseDatabase.getInstance().getReference("CommentLike")
                .child(comments[position].commentID)
                .removeValue()

            FirebaseDatabase.getInstance().getReference("CommentNotification")
                .child(comments[position].postID)
                .child(currentUser)
                .child(comments[position].commentID)
                .removeValue()

            //Remove notification


            //remove comment like notification


            FirebaseDatabase.getInstance().getReference("Comment")
                .child(comments[position].commentID)
                .removeValue()

            query1 = FirebaseDatabase.getInstance().getReference("CommentNotification")
                .child(comments[position].postID)
                .child(currentUser)
                .child(comments[position].commentID)



            CountOrder.number--

            //////ERROR//////
            Timer("SettingUp", false).schedule(100500){
               FirebaseDatabase.getInstance().getReference("CommentLikeNotification")
                   .child(comments[position].commentID)
                   .removeValue()
            }




        }

        // Comment like
        val postID1 = comments[position].postID
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        query = FirebaseDatabase.getInstance().getReference("CommentLikeNotification")
            .child(postID1!!)
            .child(currentUserID)


        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {

                    getKey.key = p0.value.toString()


                }
            }
        })


        query = FirebaseDatabase.getInstance().getReference("CommentLike")
            .child(comments[position].commentID)
            .child(currentUser)


        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    holder.like.setImageResource(R.drawable.heart_clicked)
                    //////get Key first///////
                    query = FirebaseDatabase.getInstance().getReference("CommentLikeNotification")
                        .child(comments[position].commentID)
                        .child(currentUserID)

                    query.addValueEventListener(object: ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if(p0.exists()){
                                getKeyc.key=p0.value.toString()
                            }
                        }
                    })

                    ////////////////////////

                    //unlove
                    holder.like.setOnClickListener {
                        stop = false
                        FirebaseDatabase.getInstance().getReference("CommentLike")
                            .child(comments[position].commentID)
                            .child(currentUser)
                            .removeValue()

                        FirebaseDatabase.getInstance().getReference("CommentLikeNotification")
                            .child(comments[position].commentID)
                            .child(currentUser)
                            .removeValue()

                        //////get Key first///////
                        query = FirebaseDatabase.getInstance().getReference("CommentLikeNotification")
                            .child(comments[position].commentID)
                            .child(currentUserID)

                        query.addValueEventListener(object: ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                if(p0.exists()){
                                    getKeyc.key=p0.value.toString()
                                }
                            }
                        })

                        ////////////////////////

                        FirebaseDatabase.getInstance().getReference("Notification")
                            .child(getKeyc.key)
                            .removeValue()

                        //Add Score
                        query = FirebaseDatabase.getInstance().getReference("Users")
                            .child(comments[position].userID)

                        query.addValueEventListener(object: ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                if(p0.exists()){

                                    val user = p0.getValue(User::class.java)
                                    if(stop==false){
                                        if(!(currentUserID.equals(comments[position].userID))) {
                                            user!!.score = user.score - 10
                                        }
                                        stop = true
                                    }

                                    FirebaseDatabase.getInstance().getReference("Users")
                                        .child(comments[position].userID).setValue(user)
                                }

                            }
                        })

                    }
                } else {
                    holder.like.setImageResource(R.drawable.heart_not_clicked)

                    //love
                    holder.like.setOnClickListener{
                        stop = false
                        ref3 = FirebaseDatabase.getInstance().getReference("Notification")
                        val notificationKey = ref3.push().key

                        ref1 = FirebaseDatabase.getInstance().getReference("CommentLike")
                        //store Comment Like
                        ref1.child(comments[position].commentID).child(currentUser).setValue(true)


                        //store Notification
                        if(!(currentUserID.equals(comments[position].userID))) {
                            //send notification
                            //class Notification(val notificationID : String, val date : String, val type : String, val receiverPostID : String, val sender : String)
                            val message = " likes on your comment"

                            val storeNotification = Notification(
                                notificationKey!!,
                                getTime(),
                                message,
                                comments[position].userID,
                                currentUserID,
                                comments[position].postID
                            )
                            //store Comment Like Notification
                            ref2 = FirebaseDatabase.getInstance().getReference("CommentLikeNotification")
                            ref2.child(comments[position].commentID).child(currentUser).setValue(notificationKey)

                            //store notification
                            ref3.child(notificationKey!!).setValue(storeNotification)

                            //Add Score
                            query = FirebaseDatabase.getInstance().getReference("Users")
                                .child(comments[position].userID)

                            query.addValueEventListener(object: ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    if(p0.exists()){

                                        val user = p0.getValue(User::class.java)
                                        if(stop==false){
                                            user!!.score = user.score+10
                                            stop = true
                                        }

                                        FirebaseDatabase.getInstance().getReference("Users")
                                            .child(comments[position].userID).setValue(user)
                                    }

                                }
                            })

                        }
                    }
                }

            }
        })
    }


    private fun getTime(): String {
        var formate = SimpleDateFormat("dd MMM, YYYY, HH:mm:ss", Locale.US)

        val now = Calendar.getInstance()
        val currentYear = now.get(Calendar.YEAR)
        val currentMonth = now.get(Calendar.MONTH)
        val currentDate = now.get(Calendar.DAY_OF_MONTH)
        val currentHour = now.get(Calendar.HOUR_OF_DAY) - 4
        val currentMin = now.get(Calendar.MINUTE)
        val currentSec = now.get(Calendar.SECOND)

        val current = Calendar.getInstance()
        current.set(Calendar.YEAR, currentYear)
        current.set(Calendar.MONTH, currentMonth)
        current.set(Calendar.DATE, currentDate)
        current.set(Calendar.HOUR, currentHour)
        current.set(Calendar.MINUTE, currentMin)
        current.set(Calendar.SECOND, currentSec)

        val postTime = formate.format(current.time)

        /*Toast.makeText(
            applicationContext,
            //"Year = " + currentYear + "\nMonth " + currentMonth+ "\nDate " + currentDate+ "\nHour "
            //       + currentHour+ "\nMin " + currentMin +"\nSec " + currentSec,
            postTime.toString(),
            Toast.LENGTH_SHORT
        ).show()
        */

        return postTime.toString()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val content: TextView = itemView.findViewById(R.id.content)
        val date: TextView = itemView.findViewById(R.id.date)
        val username: TextView = itemView.findViewById(R.id.username)
        val profilePic: CircleImageView = itemView.findViewById(R.id.profilePic)
        val removeButton : Button = itemView.findViewById(R.id.removeComment)
        val like : ImageView = itemView.findViewById(R.id.like)
        val likeCount : TextView = itemView.findViewById(R.id.likeCount)

    }

}
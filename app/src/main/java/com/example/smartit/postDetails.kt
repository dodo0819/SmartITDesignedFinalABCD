package com.example.smartit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post_details.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class postDetails : AppCompatActivity() {

    lateinit var ref : DatabaseReference
    lateinit var ref1 : DatabaseReference
    lateinit var ref2 : DatabaseReference

    lateinit var query : Query
    lateinit var query1 : Query

    var likeNot = LikeNotification()
    lateinit var notificationList: MutableList<Notification>
    lateinit var userList: MutableList<User>
    lateinit var likeNotificationList: MutableList<LikeNotification>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)

        textView1.text= intent.getStringExtra("Username")
        textView2.text= intent.getStringExtra("Date")
        textView3.text= intent.getStringExtra("Title")
        textView4.text= intent.getStringExtra("Content")

        notificationList = mutableListOf()
        userList = mutableListOf()
        likeNotificationList = mutableListOf()
        Picasso.get().load(intent.getStringExtra("ProfilePhoto")).placeholder(R.drawable.profile).into(profilePic)
        Picasso.get().load(intent.getStringExtra("PostPhoto")).into(postPhoto)

        //val userID = intent.getStringExtra("UserID")
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val postID = intent.getStringExtra("PostID")

        comment.setOnClickListener{
            test()
        }

        query = FirebaseDatabase.getInstance().getReference("Like")
            .child(postID!!)
            .child(currentUserID)


        //Toast.makeText(this@postDetails, "User ID = " + userID, Toast.LENGTH_SHORT).show()
        //Toast.makeText(this@postDetails, "postID = " + postID, Toast.LENGTH_SHORT).show()

        /*like.setOnClickListener {
            love()


        }*/

        query.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){

                    like.setImageResource(R.drawable.heart_clicked)
                        getKey()
                        like.setOnClickListener {
                            unlove()
                            Toast.makeText(this@postDetails, "UnLoved", Toast.LENGTH_SHORT).show()

                        }
                }
                else{
                    like.setOnClickListener {
                        love()
                        Toast.makeText(this@postDetails, "Loved", Toast.LENGTH_SHORT).show()

                    }
                }


            }
        })




    }

    private fun love(){

        ref = FirebaseDatabase.getInstance().getReference("Like")
        ref1 = FirebaseDatabase.getInstance().getReference("Notification")
        ref2 = FirebaseDatabase.getInstance().getReference("LikeNotification")

        val ntfKey = ref1.push().key
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid

        val userMap=HashMap<String,Any>()
        userMap[currentUserID]=  "true"
        userMap["notificationID"]=  ntfKey!!

        //send notification
        //class Notification(val notificationID : String, val date : String, val type : String, val receiverPostID : String, val sender : String)
        val message = " likes your post \"" + intent.getStringExtra("Title") + "\" "

        val storeNotification = Notification(ntfKey!!, getTime(), message, intent.getStringExtra("UserID")!!, currentUserID)

        ref1.child(ntfKey!!).setValue(storeNotification)


        ref.child(intent.getStringExtra("PostID")!!).child(currentUserID).setValue(true)

        ref2.child(intent.getStringExtra("PostID")!!).child(currentUserID).setValue(ntfKey)

        like.setImageResource(R.drawable.heart_clicked)

    }


    private fun unlove(){

        //val userID1 = intent.getStringExtra("UserID")
        val postID1 = intent.getStringExtra("PostID")
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseDatabase.getInstance().getReference("Like")
            .child(postID1!!)
            .child(currentUserID)
            .removeValue()


        query = FirebaseDatabase.getInstance().getReference("LikeNotification")
            .child(postID1!!)
            .child(currentUserID)


        //get the fcking notifcation ID first
        query.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){

                    likeNot.notificationID = p0.value.toString()
                    //likeNotificationList.add(likeNot!!)

                    getKey.key=p0.value.toString()
                    //Toast.makeText(applicationContext, "NO KEYYYYYY  zzz= " , Toast.LENGTH_SHORT).show()
                    Log.d("abcdddd", getKey.key)

                   // Toast.makeText(applicationContext, "Abc  = " + getKey.key, Toast.LENGTH_SHORT).show()
                }


            }
        })
        //Log.d("Fcking ID", "Hello this = " + abc)

        //query.child(intent.getStringExtra("PostID")!!).removeValue()
        FirebaseDatabase.getInstance().getReference("LikeNotification")
            .child(postID1!!)
            .child(currentUserID)
            .removeValue()

        //Toast.makeText(applicationContext, "Why no key  = " + getKey.key, Toast.LENGTH_SHORT).show()

        //After get key
        ///ERRORRRR//
        FirebaseDatabase.getInstance().getReference("Notification")
            .child(getKey.key)
            .removeValue()


        like.setImageResource(R.drawable.heart_not_clicked)

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

    private fun test(){
        Toast.makeText(applicationContext, "Abc123  = " + getKey.key, Toast.LENGTH_SHORT).show()
    }


    private fun getKey(){
        val postID1 = intent.getStringExtra("PostID")
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        query = FirebaseDatabase.getInstance().getReference("LikeNotification")
            .child(postID1!!)
            .child(currentUserID)


        //get the fcking notifcation ID first
        query.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){

                    likeNot.notificationID = p0.value.toString()
                    //likeNotificationList.add(likeNot!!)

                    getKey.key=p0.value.toString()
                    //Toast.makeText(applicationContext, "NO KEYYYYYY  zzz= " , Toast.LENGTH_SHORT).show()
                    Log.d("abcdddd", getKey.key)

                    // Toast.makeText(applicationContext, "Abc  = " + getKey.key, Toast.LENGTH_SHORT).show()
                }


            }
        })
    }

}



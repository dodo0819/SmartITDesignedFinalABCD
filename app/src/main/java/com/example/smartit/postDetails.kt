package com.example.smartit

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post_details.*
import kotlinx.android.synthetic.main.activity_post_details.profilePic
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

class postDetails : AppCompatActivity() {

    lateinit var ref : DatabaseReference
    lateinit var ref1 : DatabaseReference
    lateinit var ref2 : DatabaseReference
    lateinit var ref3 : DatabaseReference

    lateinit var query : Query
    lateinit var query1 : Query
    var stop : Boolean = false

    var likeNot = LikeNotification()
    lateinit var notificationList: MutableList<Notification>
    lateinit var userList: MutableList<User>
    lateinit var likeList: MutableList<Like>
    lateinit var likeNotificationList: MutableList<LikeNotification>
    lateinit var commentList: MutableList<Comment>
    lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        textView1.text= intent.getStringExtra("Username")
        textView2.text= intent.getStringExtra("Date")
        textView3.text= intent.getStringExtra("Title")
        textView4.text= intent.getStringExtra("Content")
        val userID = intent.getStringExtra("UserID")

        textView1.setOnClickListener{
            val intent = Intent(applicationContext, SearchProfile::class.java)
            intent.putExtra("selectedUserID", userID)
            startActivity(intent)
        }

        notificationList = mutableListOf()
        userList = mutableListOf()
        likeList = mutableListOf()
        commentList = mutableListOf()
        likeNotificationList = mutableListOf()
        recyclerView = commentRecycleView
        Picasso.get().load(intent.getStringExtra("ProfilePhoto")).placeholder(R.drawable.profile).into(profilePic)
        Picasso.get().load(intent.getStringExtra("PostPhoto")).into(postPhoto)

        //val userID = intent.getStringExtra("UserID")
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val postID = intent.getStringExtra("PostID")

        addComment.setOnClickListener{
            addComment(it)
        }
        //getCount2()
        comment.setOnClickListener {
            hide.isVisible = false
            show1.isVisible = true
            show.isVisible = true
            show.setOnClickListener {
                hide.isVisible = true
                show1.isVisible = false
                show.isVisible = false

            }
        }

        query = FirebaseDatabase.getInstance().getReference("Like")
            .child(postID!!)
            .child(currentUserID)


        query.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){

                    like.setImageResource(R.drawable.heart_clicked)
                        getKey()
                        like.setOnClickListener {
                            getKey()
                            unlove()
                            getCount1()
                        }
                }
                else{
                    getKey()
                    like.setOnClickListener {
                        love()
                        getCount1()
                    }
                }

            }
        })

        getCount1()
        //getCount2()

        query1 = FirebaseDatabase.getInstance().getReference("Comment")
            .orderByChild("postID")
            .equalTo(postID)


        query1.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    commentList.clear()
                    recyclerView.isVisible=true
                    for(h in p0.children){
                        val post = h.getValue(Comment::class.java)
                        commentList.add(post!!)
                        CountOrder.number = commentList.size
                        commentCount.text = CountOrder.number.toString()
                    }

                    val adapter = CommentAdapter(commentList)
                    val mLayoutManager = LinearLayoutManager(applicationContext)
                    mLayoutManager.reverseLayout = true
                    recyclerView.layoutManager = mLayoutManager
                    recyclerView.scrollToPosition(commentList.size-1)
                    recyclerView.adapter = adapter

                }
                else{
                    recyclerView.isVisible=false
                    commentCount.text = "0"
                }
            }
        })

    }

    private fun love(){
        stop=false
        ref = FirebaseDatabase.getInstance().getReference("Like")
        ref1 = FirebaseDatabase.getInstance().getReference("Notification")
        ref2 = FirebaseDatabase.getInstance().getReference("LikeNotification")


        val ntfKey = ref1.push().key
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid

        val userMap=HashMap<String,Any>()
        userMap[currentUserID]=  "true"
        userMap["notificationID"]=  ntfKey!!


        if(!(currentUserID.equals(intent.getStringExtra("UserID")))) {
            //send notification
            //class Notification(val notificationID : String, val date : String, val type : String, val receiverPostID : String, val sender : String)
            val message = " likes your \"" + intent.getStringExtra("Title") + "\" post"

            val storeNotification = Notification(
                ntfKey!!,
                getTime(),
                message,
                intent.getStringExtra("UserID")!!,
                currentUserID,
                intent.getStringExtra("PostID")!!,
                "false"
            )
            //store notification
            ref1.child(ntfKey!!).setValue(storeNotification)
            //store likenotification
            ref2.child(intent.getStringExtra("PostID")!!).child(currentUserID).setValue(ntfKey)
            //Add Score
            query = FirebaseDatabase.getInstance().getReference("Users")
                .child(intent.getStringExtra("UserID")!!)

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
                            .child(intent.getStringExtra("UserID")!!).setValue(user)
                    }
                }
            })
        }
        //store like
        ref.child(intent.getStringExtra("PostID")!!).child(currentUserID).setValue(true)
        like.setImageResource(R.drawable.heart_clicked)

    }

    private fun unlove(){
        stop=false
        //val userID1 = intent.getStringExtra("UserID")
        val postID1 = intent.getStringExtra("PostID")
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseDatabase.getInstance().getReference("Like")
            .child(postID1!!)
            .child(currentUserID)
            .removeValue()


        /*query = FirebaseDatabase.getInstance().getReference("LikeNotification")
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
        })*/


        FirebaseDatabase.getInstance().getReference("LikeNotification")
            .child(postID1!!)
            .child(currentUserID)
            .removeValue()

        //After get key
        ///ERRORRRR//
        Log.d("erirrrrr", getKey.key)
        FirebaseDatabase.getInstance().getReference("Notification")
            .child(getKey.key)
            .removeValue()

        if(!(currentUserID.equals(intent.getStringExtra("UserID")))) {
            //Minus Score
            query = FirebaseDatabase.getInstance().getReference("Users")
                .child(intent.getStringExtra("UserID")!!)

            query.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {

                        val user = p0.getValue(User::class.java)
                        if (stop == false) {
                            user!!.score = user.score - 10
                            stop = true
                        }

                        FirebaseDatabase.getInstance().getReference("Users")
                            .child(intent.getStringExtra("UserID")!!).setValue(user)
                    }

                }
            })
        }



        like.setImageResource(R.drawable.heart_not_clicked)

    }

    private fun getTime(): String {

        val today = LocalDateTime.now(ZoneId.systemDefault())

        return today.format(DateTimeFormatter.ofPattern("d MMM uuuu HH:mm:ss "))
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

    private fun addComment(view : View){

        val alertBox = AlertDialog.Builder(this@postDetails)

        alertBox.setTitle("Error")

        alertBox.setIcon(R.mipmap.ic_launcher)

        alertBox.setNegativeButton("Close"){dialog, which ->
            dialog.dismiss()
        }


        if(commentText.text.length==0){
            alertBox.setMessage("Your comment is empty !!!")
            alertBox.show()
        }
        else {

            //val intent = Intent(applicationContext, CommentActivity::class.java)
            //startActivity(intent)
            val postID1 = intent.getStringExtra("PostID")
            val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid

            ref = FirebaseDatabase.getInstance().getReference("Comment")
            val commentID = ref.push().key
            val comment = Comment(
                commentID!!,
                postID1!!,
                currentUserID,
                commentText.text.toString(),
                getTime()
            )

            ref.child(commentID).setValue(comment).addOnSuccessListener {
                commentText.setText("")
                Toast.makeText(applicationContext, "Comment Successfully", Toast.LENGTH_SHORT)
                    .show()
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
            ref1 = FirebaseDatabase.getInstance().getReference("Notification")
            ref2 = FirebaseDatabase.getInstance().getReference("CommentNotification")
            ref3 = FirebaseDatabase.getInstance().getReference("PostComment")
            val ntfKey = ref1.push().key

            //ref3.child(intent.getStringExtra("PostID")!!).child(currentUserID).child(commentID).setValue(true).addOnSuccessListener {
            //    Toast.makeText(applicationContext, "Success add comment wtf", Toast.LENGTH_SHORT).show()
            //}

            //If send own self notification no  need
            if (!(currentUserID.equals(intent.getStringExtra("UserID")))) {
                //send notification
                //class Notification(val notificationID : String, val date : String, val type : String, val receiverPostID : String, val sender : String)
                val message = " comments on your  \"" + intent.getStringExtra("Title") + "\" post"

                val storeNotification = Notification(
                    ntfKey!!,
                    getTime(),
                    message,
                    intent.getStringExtra("UserID")!!,
                    currentUserID,
                    intent.getStringExtra("PostID")!!,
                    "false"
                )
                //store notification
                ref1.child(ntfKey!!).setValue(storeNotification)
                //store comment notification
                ref2.child(intent.getStringExtra("PostID")!!).child(currentUserID).child(commentID)
                    .setValue(ntfKey)


                //hide keyboard
                //val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                //inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

                //Toast.makeText(this@postDetails, "Comment Successfully", Toast.LENGTH_SHORT).show()

            }

        }
    }

    private fun getCount1() {

        CountOrder.total = 0
        val postID1 = intent.getStringExtra("PostID")
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid

        query = FirebaseDatabase.getInstance().getReference("Like")
            .child(postID1!!)


        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {

                    CountOrder.total = (p0.childrenCount.toString()).toInt()
                    //Log.d("totalccc", "TOtal = " + CountOrder.total)
                    likeCount.text = CountOrder.total.toString()
                }
                else{
                    likeCount.text = "0"
                }

            }
        })


    }

    private fun getCount2() {


        val postID1 = intent.getStringExtra("PostID")
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        CountOrder.number = 0
        query = FirebaseDatabase.getInstance().getReference("Comment")
            .orderByChild("postID")
            .equalTo(postID1!!)


        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {
                    for(h in p0.children){

                    }
                    CountOrder.number++
                    commentCount.text = CountOrder.number.toString()
                }
                else{
                    commentCount.text = "0"
                }

            }
        })


    }


}



package com.example.smartit

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*

class SearchProfile : AppCompatActivity() {

    var currentUserID= FirebaseAuth.getInstance().currentUser!!.uid
    var user=User()
    lateinit var postList : MutableList<Post>
    lateinit var ref : DatabaseReference
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_profile)

        postList = mutableListOf()
        ref = FirebaseDatabase.getInstance().getReference("Post")
        recyclerView  = recycleLayout
        displayProfile()

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading...")
        progressDialog.show()
        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    postList.clear()

                    for (h in p0.children) {
                        val post = h.getValue(Post::class.java)
                        if(post!!.userID.equals(intent.getStringExtra("selectedUserID"))){
                            postList.add(post!!)
                        }

                    }

                    val adapter = PostAdapter(postList)
                    val mLayoutManager = LinearLayoutManager(this@SearchProfile)
                    mLayoutManager.reverseLayout = true
                    recyclerView.layoutManager = mLayoutManager

                    recyclerView.scrollToPosition(postList.size-1)
                    recyclerView.adapter = adapter
                    progressDialog.dismiss()
                }
            }
        })

    }

    private fun displayProfile() {


        val usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(intent.getStringExtra("selectedUserID"))

        usersRef.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    user= p0.getValue<User>(User::class.java)!!
                    usernameDP.text=user!!.username
                    emailDP.text=user!!.email
                    scoreDP.text=user!!.score.toString()
                    Picasso.get().load(user!!.image).placeholder(R.drawable.profile).into(profileImage)
                }
            }

        })
    }
}

package com.example.smartit

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartit.databinding.FragmentHomeBinding
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    lateinit var ref : DatabaseReference
    lateinit var ref2 : DatabaseReference
    lateinit var binding : FragmentHomeBinding
    lateinit var postList : MutableList<Post>
    lateinit var postList1 : MutableList<Post>
    lateinit var recyclerView: RecyclerView
    lateinit var query: Query
    var count = 0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //count = 0
        ref = FirebaseDatabase.getInstance().getReference("Post")
        //query = ref.orderByChild("order")
        //query = ref.orderByChild("title").equalTo("wtf")


        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )
        //getCount1()
        postList = mutableListOf()
        postList1 = mutableListOf()
        recyclerView  = binding.recycleLayout

        return binding.root
        //return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button123.setOnClickListener {
            val intent = Intent(activity, PostActivity::class.java)

            startActivity(intent)

            //post()
        }


        val progressDialog = ProgressDialog(activity)
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
                                postList.add(post!!)

                            }


                    val adapter = PostAdapter(postList)
                    val mLayoutManager = LinearLayoutManager(activity)
                    mLayoutManager.reverseLayout = true
                    recyclerView.layoutManager = mLayoutManager

                    recyclerView.scrollToPosition(postList.size-1)
                    recyclerView.adapter = adapter
                    progressDialog.dismiss()
                }
            }
        })



            searchText.addTextChangedListener {
                postList.clear()
                //Toast.makeText(activity,"text changed", Toast.LENGTH_SHORT).show()
                val progressDialog = ProgressDialog(activity)
                progressDialog.setTitle("Loading...")
                progressDialog.show()
                ref.addValueEventListener(object: ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if(p0.exists()){
                            postList.clear()
                            var count = 0
                            for (h in p0.children) {
                                val post = h.getValue(Post::class.java)
                                postList1.add(post!!)

                                val title = postList1[count].title.toLowerCase()
                                val content = postList1[count].content.toLowerCase()
                                val userID = postList1[count].userID
                                val sub = searchText.text.toString().toLowerCase()
                                count++
                                if(title.contains(sub,false) || content.contains(sub,false)){
                                        postList.add(post)
                                }

                                ref2 = FirebaseDatabase.getInstance().getReference("Users").child(userID)

                                ref2.addValueEventListener(object: ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {
                                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                    }

                                    override fun onDataChange(p0: DataSnapshot) {
                                        if (p0.exists()) {
                                            //for (h in p0.children) {
                                                val user = p0.getValue(User::class.java)
                                                val username = user!!.username.toLowerCase()
                                                if (username.contains(sub, false)) {
                                                    postList.add(post)
                                                }
                                            //}
                                        }
                                    }
                                })

                            }


                            val adapter = PostAdapter(postList)
                            val mLayoutManager = LinearLayoutManager(activity)
                            mLayoutManager.reverseLayout = true
                            recyclerView.layoutManager = mLayoutManager

                            recyclerView.scrollToPosition(postList.size-1)
                            recyclerView.adapter = adapter
                            progressDialog.dismiss()
                        }
                    }
                })


        }


    }






}

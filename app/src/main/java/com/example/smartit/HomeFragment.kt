package com.example.smartit

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartit.databinding.FragmentHomeBinding
import com.google.firebase.database.*



/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    lateinit var ref : DatabaseReference
    //lateinit var ref2 : DatabaseReference
    lateinit var binding : FragmentHomeBinding
    lateinit var postList : MutableList<Post>
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
        query = ref.orderByChild("order")
        //query = ref.orderByChild("title").equalTo("wtf")


        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )
        //getCount1()
        postList = mutableListOf()

        recyclerView  = binding.recycleLayout

        return binding.root
        //return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button123.setOnClickListener {
            val intent = Intent(activity, PostActivity::class.java)

            startActivity(intent)
            activity!!.finish()
            //post()
        }

        val progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Loading...")
        progressDialog.show()
        query.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    postList.clear()

                    for(h in p0.children){
                        val post = h.getValue(Post::class.java)
                        postList.add(post!!)

                    }

                    val adapter = PostAdapter(postList)
                    val mLayoutManager = LinearLayoutManager(activity)
                    //mLayoutManager.reverseLayout = true
                    recyclerView.layoutManager = mLayoutManager
                    recyclerView.adapter = adapter
                    progressDialog.dismiss()
                }
            }
        })


    }

    /*private fun post(){

        val title = binding.text1.text.toString()
        val content = binding.text2.text.toString()
        val postID = ref.push().key.toString()
        val countt = getCount1()
        val defaultProfile = "https://firebasestorage.googleapis.com/v0/b/smartit-3dd97.appspot.com/o/Default%20images%2Fprofile.png?alt=media&token=98b86e91-c02a-4e1b-884a-aee28a4b5014"
        //Toast.makeText(this@MainActivity,"Please fill in the blank"+ userID, Toast.LENGTH_SHORT).show()
        val storePost = Post(postID, title, content, getTime(), "cshong1999", countt, defaultProfile)
        //count=0
        //CountOrder.number = 0
        ref.child(postID).setValue(storePost).addOnCompleteListener {
            Toast.makeText(
                activity,
                "Successfully Stored into the fire base!!! " + countt,
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun getTime():String{
        var formate = SimpleDateFormat("dd MMM, YYYY, HH:mm:ss", Locale.US)

        val now = Calendar.getInstance()
        val currentYear = now.get(Calendar.YEAR)
        val currentMonth = now.get(Calendar.MONTH)
        val currentDate = now.get(Calendar.DAY_OF_MONTH)
        val currentHour = now.get(Calendar.HOUR_OF_DAY)+8
        val currentMin = now.get(Calendar.MINUTE)
        val currentSec = now.get(Calendar.SECOND)

        val current = Calendar.getInstance()
        current.set(Calendar.YEAR,currentYear)
        current.set(Calendar.MONTH,currentMonth)
        current.set(Calendar.DATE,currentDate)
        current.set(Calendar.HOUR,currentHour)
        current.set(Calendar.MINUTE,currentMin)
        current.set(Calendar.SECOND,currentSec)

        val postTime = formate.format(current.time)

        /*Toast.makeText(
            activity,
            //"Year = " + currentYear + "\nMonth " + currentMonth+ "\nDate " + currentDate+ "\nHour "
            //       + currentHour+ "\nMin " + currentMin +"\nSec " + currentSec,
            postTime.toString(),
            Toast.LENGTH_SHORT
        ).show()*/

        return postTime.toString()
    }

    private fun getCount1():Int{

        count = 0

        query.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                if(p0.exists()){
                    postList.clear()

                    for(h in p0.children){
                        //CountOrder.number--
                        val post = h.getValue(Post::class.java)
                        postList.add(post!!)

                    }

                    val adapter = PostAdapter(postList)

                    CountOrder.total = adapter.itemCount

                }
            }
        })

        return CountOrder.number - CountOrder.total
    }*/






}

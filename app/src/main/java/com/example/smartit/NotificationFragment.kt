package com.example.smartit


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_notification.*

/**
 * A simple [Fragment] subclass.
 */
class NotificationFragment : Fragment() {

    lateinit var ref : DatabaseReference
    lateinit var notificationList : MutableList<Notification>
    lateinit var recyclerView: RecyclerView
    lateinit var query: Query
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment




        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ref = FirebaseDatabase.getInstance().getReference("Notification")
        val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        query = ref.orderByChild("receiver").equalTo(currentUser)


        notificationList = mutableListOf()

        recyclerView = recycleLayout123

        query.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    notificationList.clear()

                    for(h in p0.children){
                        val notification1 = h.getValue<Notification>(Notification::class.java)
                        notificationList.add(notification1!!)

                        //Toast.makeText(activity,"Wtf is this bro"  + notificationList[0].receiver, Toast.LENGTH_SHORT).show()
                    }

                    val adapter = NotificationAdapter(notificationList)

                    val mLayoutManager = LinearLayoutManager(activity)
                    mLayoutManager.reverseLayout = true

                    recyclerView.layoutManager = mLayoutManager
                    recyclerView.scrollToPosition(notificationList.size-1)

                    recyclerView.adapter = adapter

                }
            }
        })
    }
}

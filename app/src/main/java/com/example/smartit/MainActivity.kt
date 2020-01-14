package com.example.smartit

import android.app.LauncherActivity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_notification.*


class MainActivity : AppCompatActivity() {

    lateinit var homeFragment: HomeFragment
    lateinit var libraryFragment: LibraryFragment
    lateinit var quizFragment: QuizFragment
    lateinit var notificationFragment: NotificationFragment
    lateinit var profileFragment: ProfileFragment

    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder : android.app.Notification.Builder
    private val channelID = "com.example.text"
    private val description = "Test notification"


    lateinit var ref : DatabaseReference
    lateinit var query: Query


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentUser= FirebaseAuth.getInstance().currentUser









        if(currentUser==null){
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()

        }
        else{
            notificationManager =  getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            ref = FirebaseDatabase.getInstance().getReference("Notification")

            query = ref.orderByChild("receiver")
                .equalTo(currentUser!!.uid)





            query.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {


                        for (h in p0.children) {
                            val notification1 =
                                h.getValue<com.example.smartit.Notification>(com.example.smartit.Notification::class.java)
                            if(notification1!!.status=="false"){
                                if(getKey.stop==false) {
                                    getKey.size++

                                }
                            }

                        }
                        if(getKey.size>0){


                            if(getKey.first==false){
                                if(getKey.stop==false) {
                                    notification()
                                    getKey.stop = true
                                }
                            }
                            else{
                                if(getKey.stop==false) {
                                    notification1()
                                    getKey.stop = true
                                }
                            }


                        }else{
                            //Toast.makeText(this@MainActivity, "Boolean = " + getKey.first, Toast.LENGTH_SHORT).show()
                            getKey.first =true
                        }



                    }
                }
            })
        }

        val bottomNav1: BottomNavigationView = findViewById(R.id.btmNav)

        if(intent.getStringExtra("GoNotification")!=null&&intent.getStringExtra("GoNotification")!!.equals("true")){
            notificationFragment= NotificationFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame1,notificationFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()

            btmNav.selectedItemId = R.id.notification
        }
        else{
            homeFragment= HomeFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame1,homeFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }


        bottomNav1.setOnNavigationItemSelectedListener { item ->

            when(item.itemId){

                R.id.home ->{
                    homeFragment= HomeFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame1,homeFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
                R.id.library ->{
                    libraryFragment=LibraryFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame1,libraryFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()

                    //val intent = Intent(this, LibraryMou::class.java)
                    //startActivity(intent)


                }
                R.id.quiz ->{
                    quizFragment= QuizFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame1,quizFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
                R.id.notification ->{
                    notificationFragment= NotificationFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame1,notificationFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
                R.id.profile ->{
                    profileFragment=ProfileFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame1,profileFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }

            }
            true
        }
    }


    private fun notification(){

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("GoNotification", "true")
        val pendingIntent =  PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val contentView = RemoteViews(packageName, R.layout.notification_layout)
        contentView.setTextViewText(R.id.tv_title, "SMART IT")
        contentView.setTextViewText(R.id.tv_content, "You have " + getKey.size + " unread notifications")

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            notificationChannel =
                NotificationChannel(channelID, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this,channelID)
                .setContentTitle("SMART IT")
                .setContentText("You have " + getKey.size + " unread notification")
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon((BitmapFactory.decodeResource(this.resources, R.drawable.logo)))
                .setBadgeIconType(R.drawable.logo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        }
        else{
            builder = Notification.Builder(this)
                .setContentTitle("SMART IT")
                .setContentText("You have " + getKey.size + " unread notification")
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon((BitmapFactory.decodeResource(this.resources, R.drawable.logo)))
                .setBadgeIconType(R.drawable.logo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        }

        notificationManager.notify(1234,builder.build())

    }

    private fun notification1(){

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("GoNotification", "true")
        val pendingIntent =  PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val contentView = RemoteViews(packageName, R.layout.notification_layout)
        contentView.setTextViewText(R.id.tv_title, "SMART IT")
        contentView.setTextViewText(R.id.tv_content, "You have " + getKey.size + " unread notifications")

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            notificationChannel =
                NotificationChannel(channelID, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this,channelID)
                .setContentTitle("SMART IT")
                .setContentText("You have new notification! Please check it out.")
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon((BitmapFactory.decodeResource(this.resources, R.drawable.logo)))
                .setBadgeIconType(R.drawable.logo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        }
        else{
            builder = Notification.Builder(this)
                .setContentTitle("SMART IT")
                .setContentText("You have new notification! Please check it out.")
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon((BitmapFactory.decodeResource(this.resources, R.drawable.logo)))
                .setBadgeIconType(R.drawable.logo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        }

        notificationManager.notify(1234,builder.build())

    }

}

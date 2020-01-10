package com.example.smartit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference


class MainActivity : AppCompatActivity() {

    lateinit var ref : DatabaseReference

    lateinit var homeFragment: HomeFragment
    lateinit var libraryFragment: LibraryFragment
    lateinit var quizFragment: QuizFragment
    lateinit var notificationFragment: NotificationFragment
    lateinit var profileFragment: ProfileFragment

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

        val bottomNav1: BottomNavigationView = findViewById(R.id.btmNav)


        homeFragment= HomeFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame1,homeFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

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
}

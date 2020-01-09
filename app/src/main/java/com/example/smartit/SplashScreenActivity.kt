package com.example.smartit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        val background = object : Thread(){
            override fun run(){
                try{
                    sleep(4000)
                    val intent = Intent(baseContext, LoginActivity::class.java)
                    startActivity(intent)
                } catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }
}
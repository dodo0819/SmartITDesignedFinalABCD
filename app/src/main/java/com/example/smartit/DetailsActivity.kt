package com.example.smartit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import androidx.databinding.DataBindingUtil
import com.example.smartit.databinding.ActivityCarDetailsBinding
import kotlinx.android.synthetic.main.activity_car_details.*



class DetailsActivity : AppCompatActivity() {
    lateinit var binding : ActivityCarDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_car_details)

        name.text = getIntent().getStringExtra("NAME")
        description.text = getIntent().getStringExtra("DESC")
        image.setImageResource(getIntent().getStringExtra("LOGO").toInt())
        link.text = getIntent().getStringExtra("LINK")
    }
}

package com.example.smartit


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_library.*


/**
 * A simple [Fragment] subclass.
 */
class LibraryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val profilePhoto = "https://firebasestorage.googleapis.com/v0/b/smartit-3dd97.appspot.com/o/Default%20images%2F80829044_599648000836119_5366044058433093632_n.jpg?alt=media&token=52f26a9a-9481-46d2-a6fe-506980d0a47e"
        //Picasso.get().load(profilePhoto).placeholder(R.drawable.profile).into(view?.testImage)
        return inflater.inflate(R.layout.fragment_library, container, false)
    }


}

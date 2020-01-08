package com.example.smartit


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.change_or_edit.view.*
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    var currentUserID=FirebaseAuth.getInstance().currentUser!!.uid
    var user=User()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment




        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    private fun changeOrEdit() {
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.change_or_edit,null)
        val mBuilder = AlertDialog.Builder(activity!!)
            .setView(mDialogView)
            .setTitle("Profile Settings")
        val mAlertDialog=mBuilder.show()
        mDialogView.changeBtn.setOnClickListener {
            startActivity(Intent(activity,changePassActivity::class.java)) //changepass
            mAlertDialog.dismiss()
        }
        mDialogView.editBtn.setOnClickListener {
            startActivity(Intent(activity,editProfileActivity::class.java)) //editprofile
            mAlertDialog.dismiss()
        }
    }

    private fun displayProfile() {
        val usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID)

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        displayProfile()


        settingBtn.setOnClickListener {
            changeOrEdit()
        }

        signOutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity!!.finish()
        }
    }
}

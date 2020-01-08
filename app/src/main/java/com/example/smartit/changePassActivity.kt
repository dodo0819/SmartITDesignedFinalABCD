package com.example.smartit

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.layout_update_profile.*

class changePassActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_update_profile)

        confirmBtn.setOnClickListener {
            changePassword()
        }
    }

    private fun changePassword() {
        var auth= FirebaseAuth.getInstance()

        if(currentPassTB.text.toString().isEmpty()){
            currentPassTB.error="Please enter current password"
            currentPassTB.requestFocus()
        }else if(newPassTB.text.toString().isEmpty()){
            newPassTB.error="Please enter new password"
            newPassTB.requestFocus()
        }else if(newPassTB.length()<6){
            newPassTB.error="Password at least 6 characters"
            newPassTB.requestFocus()
        }else if(confirmPassTB.text.toString().isEmpty()){
            confirmPassTB.error="Please enter confirm password"
            confirmPassTB.requestFocus()
        }else{
            if(newPassTB.text.toString().equals(confirmPassTB.text.toString())){
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Changing Password")
                progressDialog.setMessage("Please wait, this may take a while...")
                progressDialog.show()
                val user=auth.currentUser
                val credential = EmailAuthProvider
                    .getCredential(user!!.email!!, currentPassTB.text.toString())
                Log.d("changePW","${user.email}")
// Prompt the user to re-provide their sign-in credentials
                user?.reauthenticate(credential)
                    ?.addOnCompleteListener {
                        if(it.isSuccessful){
                            user?.updatePassword(newPassTB.text.toString())
                                ?.addOnCompleteListener{task ->
                                    if(task.isSuccessful){
                                        progressDialog.dismiss()
                                        Toast.makeText(this,"Password Changed", Toast.LENGTH_SHORT).show()
                                        this.finish()
                                        //startActivity(Intent(this,ProfileFragment::class.java)) //jump to profile
                                    }
                                }
                        }else{
                            progressDialog.dismiss()
                            currentPassTB.error="Wrong current password"
                            currentPassTB.requestFocus()
                        }
                    }

            }else{
                confirmPassTB.error="Password mismatching"
                confirmPassTB.requestFocus()
            }
        }

    }
}

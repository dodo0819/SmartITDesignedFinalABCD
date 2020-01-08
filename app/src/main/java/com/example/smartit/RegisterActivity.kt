package com.example.smartit

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerBtn.setOnClickListener {
            createAccount()
        }


    }

    private fun createAccount() {

        val username=usernameTB.text.toString()
        val email=emailTB.text.toString()
        val password=passwordTB.text.toString()

        if(usernameTB.text.toString().isEmpty()){
            usernameTB.error="Please enter username"
            usernameTB.requestFocus()
        }else if(emailTB.text.toString().isEmpty()){
            emailTB.error="Please enter email"
            emailTB.requestFocus()
        }else if(!Patterns.EMAIL_ADDRESS.matcher(emailTB.text.toString()).matches()){
            emailTB.error="Please enter correct format eg.xx@xxxx.com"
            emailTB.requestFocus()
        }else if(passwordTB.text.toString().isEmpty()){
            passwordTB.error="Please enter password"
            passwordTB.requestFocus()
        }else{
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("SignUp")
            progressDialog.setMessage("Please wait, this may take a while...")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()
            val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

            mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        saveUserInfo(username,email, password,progressDialog)
                    }
                    else
                    {
                        val message = task.exception!!.toString()
                        Toast.makeText(this,"Error: $message", Toast.LENGTH_LONG).show()
                        mAuth.signOut()
                        progressDialog.dismiss()

                    }
                }
        }

    }

    private fun saveUserInfo(username: String, email: String, password: String, progressDialog: ProgressDialog) {
        val currentUserID= FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")

        val userMap=HashMap<String,Any>()
        userMap["uid"]=currentUserID
        userMap["username"]=username
        userMap["email"]=email
        userMap["score"]=0
        userMap["image"]="https://firebasestorage.googleapis.com/v0/b/smartit-3dd97.appspot.com/o/Default%20images%2Fprofile.png?alt=media&token=98b86e91-c02a-4e1b-884a-aee28a4b5014"

        usersRef.child(currentUserID).setValue(userMap)
            .addOnCompleteListener{task ->
                if(task.isSuccessful){
                    progressDialog.dismiss()
                    Toast.makeText(this,"Account has been created successfully.", Toast.LENGTH_LONG)
                    val user=FirebaseAuth.getInstance().currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener{task ->
                            if(task.isSuccessful){
                                FirebaseAuth.getInstance().signOut()

                                val intent = Intent(this,LoginActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                Toast.makeText(this,"Account created",Toast.LENGTH_SHORT).show()
                                finish()

                            }
                        }
                }
                else
                {
                    val message = task.exception!!.toString()
                    Toast.makeText(this,"Error: $message", Toast.LENGTH_LONG).show()
                    FirebaseAuth.getInstance().signOut()
                    progressDialog.dismiss()
                }
            }
    }
}

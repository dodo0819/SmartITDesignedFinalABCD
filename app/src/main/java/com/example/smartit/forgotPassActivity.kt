package com.example.smartit

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.forgot_pass_dialog.*

class forgotPassActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_pass_dialog)

        confirmResetBtn.setOnClickListener {
            resetPassword()
        }

        cancelResetBtn.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }

    private fun resetPassword() {
        if(forgotEmail.text.toString().isEmpty()){
            forgotEmail.error="Please enter your email to reset password"
            forgotEmail.requestFocus()
        }else if(!Patterns.EMAIL_ADDRESS.matcher(forgotEmail.text.toString()).matches()){
            forgotEmail.error="Please enter correct format eg.xx@xxxx.com"
            forgotEmail.requestFocus()
        }else {
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Reset Password")
            progressDialog.setMessage("Please wait, this may take a while...")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()
            val auth = FirebaseAuth.getInstance()
            auth.sendPasswordResetEmail(forgotEmail.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this,"Email Sent", Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                        startActivity(Intent(this,LoginActivity::class.java))
                    }else{
                        Toast.makeText(this,"This Email is not exist", Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }
                }
        }
    }
}

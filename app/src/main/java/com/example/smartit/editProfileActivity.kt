package com.example.smartit

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.edit_profile.*
import java.io.IOException
import java.util.*

class editProfileActivity : AppCompatActivity() {
    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var filePath: Uri
    private var PICK_IMAGE_REQUEST = 1234
    lateinit var ref: DatabaseReference
    lateinit var token: String
    var currentUserID= FirebaseAuth.getInstance().currentUser!!.uid
    var user=User()
    var check=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile)
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        displayInfo()

        editProfileImage.setOnClickListener{
            chooseProfile()
        }


        editConfirmBtn.setOnClickListener{
            editProfile()
        }
    }
    private fun chooseProfile() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "SELECT PICTURE"), PICK_IMAGE_REQUEST)
        //Toast.makeText(this@MainActivity, "FAIL 99", Toast.LENGTH_SHORT).show()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Toast.makeText(this@MainActivity, "FAIL 99", Toast.LENGTH_SHORT).show()

        if (requestCode == PICK_IMAGE_REQUEST &&
            resultCode == Activity.RESULT_OK &&
            data != null && data.data!! != null
        ) {
            check=1
            filePath = data.data!!
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                editProfileImage!!.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
                //Toast.makeText(this, "FAIL 99", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun displayInfo() {
        val usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID)

        usersRef.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    user= p0.getValue<User>(User::class.java)!!
                    editUsername.setText(user!!.username)
                    Picasso.get().load(user!!.image).placeholder(R.drawable.profile).into(editProfileImage)
                }
            }

        })
    }

    private fun editProfile() {
        if(editUsername.text.toString().isEmpty()){
            editUsername.error="Username cannot be empty"
            editUsername.requestFocus()
            return
        }else if(check==0 && user.username.equals(editUsername.text.toString())){
            this.finish()
            //startActivity(Intent(this,MainActivity::class.java)) //jump to profile
        }else{

            if (check == 1){
                user.username = editUsername.text.toString()
                val dbUser = FirebaseDatabase.getInstance().getReference("Users")
                //dbUser.child(currentUserID).setValue(user)

                //Toast.makeText(this, "Profile Updated", Toast.LENGTH_LONG).show()
                if (filePath != null) {
                    val progressDialog = ProgressDialog(this)
                    progressDialog.setTitle("Changing Profile Image...")
                    progressDialog.show()

                    val imageRef = storageReference!!.child("images/" + UUID.randomUUID().toString())

                    imageRef.putFile(filePath!!)
                        .addOnSuccessListener {
                            progressDialog.dismiss()
                            Toast.makeText(
                                applicationContext,
                                "Updated Successfully",
                                Toast.LENGTH_LONG
                            ).show()

                            imageRef.downloadUrl.addOnSuccessListener {
                                //Log.d("Testing", "File location : $it")
                                token = "$it"
                                //Log.d("hellowtf", "File location : " + token)
                                user.image = token
                                dbUser.child(currentUserID).setValue(user)

                                //Toast.makeText(this, "Profile Updated", Toast.LENGTH_LONG).show()
                                this.finish()
                                //startActivity(Intent(this,MainActivity::class.java)) //jump to profile

                            }

                        }
                        .addOnFailureListener {
                            progressDialog.dismiss()
                            Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()

                        }

                }
                check=0
            }else if(check==0 && !(user.username.equals(editUsername.text.toString()))){
                user.username = editUsername.text.toString()
                val dbUser = FirebaseDatabase.getInstance().getReference("Users")
                dbUser.child(currentUserID).setValue(user)
                Toast.makeText(this,"Updated Successfully", Toast.LENGTH_LONG).show()
                this.finish()
                //startActivity(Intent(this,MainActivity::class.java)) //jump to profile
            }
        }
    }
}

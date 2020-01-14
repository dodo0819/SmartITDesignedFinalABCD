package com.example.smartit

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_post.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class PostActivity : AppCompatActivity() {


    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var filePath: Uri
    private var PICK_IMAGE_REQUEST = 1234
    lateinit var ref: DatabaseReference
    lateinit var token: String
    lateinit var postList : MutableList<Post>
    var count = 0
    var valid : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)


        ref = FirebaseDatabase.getInstance().getReference("Post")

        postList = mutableListOf()

        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        getCount1()

        button.setOnClickListener {
            choose()
        }

        button2.setOnClickListener {

            upload()


        }

    }

    private fun choose() {
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
            valid = true
            filePath = data.data!!
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imageView!!.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
                //Toast.makeText(this, "FAIL 99", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun upload() {

        val alertBox = AlertDialog.Builder(this@PostActivity)

        alertBox.setTitle("Error")

        alertBox.setIcon(R.mipmap.ic_launcher)

        alertBox.setNegativeButton("Close"){dialog, which ->
            dialog.dismiss()
        }

        if(titleText.text.length==0 || contentText.text.length==0){
            alertBox.setMessage("Please fill in the empty space")
            alertBox.show()
        }
        else if(valid == false){
            alertBox.setMessage("Please choose a photo")
            alertBox.show()
        }
        else {
            val title = titleText.text.toString()
            val content = contentText.text.toString()
            val postID = ref.push().key.toString()

            if (filePath != null) {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading...")
                progressDialog.show()

                val countt = getCount1()
                val imageRef = storageReference!!.child("images/" + UUID.randomUUID().toString())

                imageRef.putFile(filePath!!)
                    .addOnSuccessListener {
                        progressDialog.dismiss()

                        imageRef.downloadUrl.addOnSuccessListener {
                            //Log.d("Testing", "File location : $it")
                            token = "$it"
                            //Log.d("hellowtf", "File location : " + token)
                            val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
                            //wtf
                            val storePost = Post(
                                postID,
                                title,
                                content,
                                getTime(),
                                currentUserID,
                                token

                            )
                            count = 0
                            ref.child(postID).setValue(storePost).addOnCompleteListener {
                                Toast.makeText(
                                    applicationContext,
                                    "Successfully Posted !!!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this, MainActivity::class.java)

                                startActivity(intent)
                            }
                        }

                    }
                    .addOnFailureListener {
                        progressDialog.dismiss()
                        Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()


                    }
                    .addOnProgressListener { taskSnapshot ->
                        val progress =
                            100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                        progressDialog.setMessage("Uploaded " + progress.toInt() + "%...")


                    }


            }
        }

    }

    /*private fun post(){

        val title = titleText.text.toString()
        val content = contentText.text.toString()
        val postID = ref.push().key.toString()


        val storePost = Post(postID, title, content, getTime(), "cshong1999", (500-getCount1()), token)
        count=0
        ref.child(postID).setValue(storePost).addOnCompleteListener {
            Toast.makeText(
                applicationContext,
                "Successfully Stored into the fire base!!!",
                Toast.LENGTH_SHORT
            ).show()
        }

    }*/

    private fun getTime(): String {

        val today = LocalDateTime.now(ZoneId.systemDefault())

        return today.format(DateTimeFormatter.ofPattern("d MMM uuuu HH:mm:ss "))
    }

    private fun getCount1(): Int {

        count = 0

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {
                    postList.clear()

                    for (h in p0.children) {
                        //CountOrder.number--
                        val post = h.getValue(Post::class.java)
                        postList.add(post!!)
                    }

                    val adapter = PostAdapter(postList)

                    CountOrder.total = adapter.itemCount

                }
            }
        })
        Log.d("TotalCCC", "Total = " +  CountOrder.total)
        return CountOrder.total
    }


}

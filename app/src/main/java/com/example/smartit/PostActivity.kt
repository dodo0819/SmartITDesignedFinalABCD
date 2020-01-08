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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_post.*
import java.io.IOException
import java.text.SimpleDateFormat
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
                    Toast.makeText(
                        applicationContext,
                        "File Uploaded" + imageRef.downloadUrl,
                        Toast.LENGTH_SHORT
                    ).show()

                    imageRef.downloadUrl.addOnSuccessListener {
                        //Log.d("Testing", "File location : $it")
                        token = "$it"
                        //Log.d("hellowtf", "File location : " + token)
                        val currentUserID= FirebaseAuth.getInstance().currentUser!!.uid
                        //wtf
                        val storePost = Post(
                            postID,
                            title,
                            content,
                            getTime(),
                            currentUserID,
                            token,
                            countt
                        )
                        count = 0
                        ref.child(postID).setValue(storePost).addOnCompleteListener {
                            Toast.makeText(
                                applicationContext,
                                "Successfully Stored into the fire base!!!" + getCount1(),
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
        var formate = SimpleDateFormat("dd MMM, YYYY, HH:mm:ss", Locale.US)

        val now = Calendar.getInstance()
        val currentYear = now.get(Calendar.YEAR)
        val currentMonth = now.get(Calendar.MONTH)
        val currentDate = now.get(Calendar.DAY_OF_MONTH)
        val currentHour = now.get(Calendar.HOUR_OF_DAY) - 4
        val currentMin = now.get(Calendar.MINUTE)
        val currentSec = now.get(Calendar.SECOND)

        val current = Calendar.getInstance()
        current.set(Calendar.YEAR, currentYear)
        current.set(Calendar.MONTH, currentMonth)
        current.set(Calendar.DATE, currentDate)
        current.set(Calendar.HOUR, currentHour)
        current.set(Calendar.MINUTE, currentMin)
        current.set(Calendar.SECOND, currentSec)

        val postTime = formate.format(current.time)

        /*Toast.makeText(
            applicationContext,
            //"Year = " + currentYear + "\nMonth " + currentMonth+ "\nDate " + currentDate+ "\nHour "
            //       + currentHour+ "\nMin " + currentMin +"\nSec " + currentSec,
            postTime.toString(),
            Toast.LENGTH_SHORT
        ).show()
        */

        return postTime.toString()
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

        return CountOrder.number - CountOrder.total
    }


}

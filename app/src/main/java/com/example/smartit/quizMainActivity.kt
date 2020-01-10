package com.example.smartit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.quiz_main.*

class quizMainActivity : AppCompatActivity() {

    var total=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_main)

        val sub1= intent.getStringExtra("subject")
        displayMark()





        button1.setOnClickListener {

            val subjectLevel=sub1.toString()+button1.hint.toString()
            val intent = Intent(this, QuizActivity::class.java).putExtra("subjectLevel",subjectLevel).putExtra("subject",sub1)
            startActivity(intent)
        }

        button2.setOnClickListener {
            val subjectLevel=sub1.toString()+button2.hint.toString()
            val intent = Intent(this, QuizActivity::class.java).putExtra("subjectLevel",subjectLevel).putExtra("subject",sub1)
            startActivity(intent)
        }

        button3.setOnClickListener {
            val subjectLevel=sub1.toString()+button3.hint.toString()
            val intent = Intent(this, QuizActivity::class.java).putExtra("subjectLevel",subjectLevel).putExtra("subject",sub1)
            startActivity(intent)
        }

    }


    fun displayMark() {
        var checking=0
        var sub= intent.getStringExtra("subject")
        var currentUserID= FirebaseAuth.getInstance().currentUser!!.uid
        val refLevel1 = FirebaseDatabase.getInstance().getReference(sub+"LEVEL1C").child(currentUserID).child(currentUserID)
        val refLevel2 = FirebaseDatabase.getInstance().getReference(sub+"LEVEL2C").child(currentUserID).child(currentUserID)
        val refLevel3 = FirebaseDatabase.getInstance().getReference(sub+"LEVEL3C").child(currentUserID).child(currentUserID)



        refLevel1.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    button1.text = "Level 1    Result: ${p0.value}/5 "
                    total=total+p0.value.toString().toInt()

                }else{
                    button1.text = "Level 1    Result: 0/5 "

                }
            }

        })

        refLevel2.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    button2.text = "Level 2    Result: ${p0.value}/5 "
                    total=total+p0.value.toString().toInt()
                }else{
                    button2.text = "Level 2    Result: 0/5 "
                }
            }

        })

        refLevel3.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    checking=1
                    button3.text = "Level 3    Result: ${p0.value}/5 "
                    total=total+p0.value.toString().toInt()

                    button4.text = "Result :  "+total+"/15"
                }
                if(checking==0){
                    button3.text = "Level 3    Result: 0/5 "
                    button4.text = "Result :  "+total+"/15"

                }
            }

        })




    }




}

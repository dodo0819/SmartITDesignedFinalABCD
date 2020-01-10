package com.example.smartit

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlinx.android.synthetic.main.show_quiz_mark.*
import kotlinx.android.synthetic.main.show_quiz_mark.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.schedule

class QuizActivity : AppCompatActivity() {


    var quizs = ArrayList<Quiz>()
    var numberOfGoodAnswers: Int = 0
    var currentQuizIndex: Int = 0

    var total=0
    lateinit var  quizList : MutableList<Quiz>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        quizList= mutableListOf()
        val finalSub=intent.getStringExtra("subjectLevel")
        finalSubjectDP.text=finalSub

        val query = FirebaseDatabase.getInstance().getReference(finalSub)


        query.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){

                    //quizs.clear()
                    quizList.clear()
                    for(h in p0.children) {
                        //val quiz = h.getValue(Quiz::class.java)
                        //Toast.makeText(applicationContext,"${h.child("question").value}",Toast.LENGTH_LONG).show()

                        val quiz = Quiz(h.child("answer").value.toString().toInt(),h.child("question").value.toString(),h.child("choice3").value.toString(),h.child("choice1").value.toString(),h.child("choice2").value.toString())
                        //quizs.add(quiz!!)
                        quizList.add(quiz)
                        //Toast.makeText(applicationContext,"${quizList.size}",Toast.LENGTH_LONG).show()
                    }


                    //Toast.makeText(applicationContext,"${quizList.size}",Toast.LENGTH_LONG).show()


                }

                Collections.shuffle(quizList)
                showQuestion(quizList[currentQuizIndex],currentQuizIndex)

                answer1.setOnClickListener {
                    if (quizList[currentQuizIndex].isCorrect(1)) {
                        numberOfGoodAnswers++
                        scoreRecordTV.text=numberOfGoodAnswers.toString()
                    } else {
                        scoreRecordTV.text=numberOfGoodAnswers.toString()
                    }

                    if(currentQuizIndex==quizList.size-1){

                        var currentUserID= FirebaseAuth.getInstance().currentUser!!.uid
                        val ref =FirebaseDatabase.getInstance().getReference(finalSub+"C")

                        val quizMarkMap = HashMap<String,Any>()
                        quizMarkMap[currentUserID]=numberOfGoodAnswers

                        ref.child(currentUserID).setValue(quizMarkMap)

                        showCongratulation(numberOfGoodAnswers)
                    }else{
                        showQuestion(quizList[currentQuizIndex+1],currentQuizIndex+1)
                    }


                    currentQuizIndex++
                }
                answer2.setOnClickListener {
                    if (quizList[currentQuizIndex].isCorrect(2)) {
                        numberOfGoodAnswers++
                        scoreRecordTV.text=numberOfGoodAnswers.toString()
                    } else {
                        scoreRecordTV.text=numberOfGoodAnswers.toString()
                    }
                    if(currentQuizIndex==quizList.size-1){
                        var currentUserID= FirebaseAuth.getInstance().currentUser!!.uid
                        val ref =FirebaseDatabase.getInstance().getReference(finalSub+"C")

                        val quizMarkMap = HashMap<String,Any>()
                        quizMarkMap[currentUserID]=numberOfGoodAnswers

                        ref.child(currentUserID).setValue(quizMarkMap)


                        showCongratulation(numberOfGoodAnswers)
                    }else{
                        showQuestion(quizList[currentQuizIndex+1],currentQuizIndex+1)
                    }


                    currentQuizIndex++
                }
                answer3.setOnClickListener {
                    if (quizList[currentQuizIndex].isCorrect(3)) {
                        numberOfGoodAnswers++
                        scoreRecordTV.text=numberOfGoodAnswers.toString()
                    } else {
                        scoreRecordTV.text=numberOfGoodAnswers.toString()
                    }
                    if(currentQuizIndex==quizList.size-1){
                        var currentUserID= FirebaseAuth.getInstance().currentUser!!.uid
                        val ref =FirebaseDatabase.getInstance().getReference(finalSub+"C")

                        val quizMarkMap = HashMap<String,Any>()
                        quizMarkMap[currentUserID]=numberOfGoodAnswers

                        ref.child(currentUserID).setValue(quizMarkMap)


                        showCongratulation(numberOfGoodAnswers)
                    }else{
                        showQuestion(quizList[currentQuizIndex+1],currentQuizIndex+1)
                    }


                    currentQuizIndex++
                }

                stopBtn.setOnClickListener {
                    val sub = intent.getStringExtra("subject")
                    startActivity(Intent(applicationContext, quizMainActivity::class.java).putExtra("subject", sub))
                }

            }


        })




        //quizs.add(Quiz("Abstract class is__", "A - A class must contain all pure virtual functions", "B - A class must contain at least one pure virtual function", "C - A class may not contain pure virtual function.", 2))
        //quizs.add(Quiz("By default the members of the structure are", "A - private", "B - protected", "C - public", 3))
        //quizs.add(Quiz("The programs machine instructions are store in __ memory segment.", "A - Data", "B - Stack", "C - Code", 3))
        //quizs.add(Quiz("What is a generic class.", "A - Class template", "B - Function template", "C - Inherited class", 1))

        //Pour mélanger les questions
        //Collections.shuffle(quizs)


        //showQuestion(quizs[0])

        //answer1.setOnClickListener {
        //handleAnswer(1)
        //}
        //answer2.setOnClickListener {
        //handleAnswer(2)
        //}
        //answer3.setOnClickListener {
        //handleAnswer(3)
        //}
    }

    fun showCongratulation(correctAns:Int){
        var check=0
        val sub = intent.getStringExtra("subject")

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.show_quiz_mark, null)

        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("Congratulations!\nYou answered $correctAns questions correctly")


        val mAlertDialog = mBuilder.show()
        mDialogView.showOkBtn.setOnClickListener {
            startActivity(Intent(this, quizMainActivity::class.java).putExtra("subject", sub))
            mAlertDialog.dismiss()
            check=1

        }
        Timer("SettingUp", false).schedule(1500){
            if(check==0) {
                startActivity(
                    Intent(
                        applicationContext,
                        quizMainActivity::class.java
                    ).putExtra("subject", sub)
                )
            }
        }

    }

    fun showQuestion(quiz: Quiz, no:Int) {
        questionNo.text=(no+1).toString()
        txtQuestion.setText(quiz.question)
        answer1.setText(quiz.choice1)
        answer2.setText(quiz.choice2)
        answer3.setText(quiz.choice3)

    }

    fun handleAnswer(answerID: Int,quiz:Quiz) {

        if (quiz.isCorrect(answerID)) {
            numberOfGoodAnswers++
            Toast.makeText(this, "+1", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "+0", Toast.LENGTH_SHORT).show()
        }


        currentQuizIndex++




    }

}

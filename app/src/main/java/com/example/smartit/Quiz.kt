package com.example.smartit

class Quiz(var answer: Int, var question: String, var choice3: String, var choice1: String, var choice2: String) {
    constructor():this(0,"","","",""){

    }
    fun isCorrect(answerNumber: Int) : Boolean {
        if (answerNumber == answer)
            return true

        return false
    }

}
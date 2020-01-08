package com.example.smartit



class Post(val postID : String, val title : String, val content : String, val date : String, val userID : String, val postPhoto : String, val order : Int){

    constructor() : this("","","","","","",0){

    }

}
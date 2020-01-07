package com.example.smartit



class Post(val postID : String, val title : String, val content : String, val date : String, val username : String, val order : Int, val postPhoto : String){

    constructor() : this("","","","","",0,""){

    }

}
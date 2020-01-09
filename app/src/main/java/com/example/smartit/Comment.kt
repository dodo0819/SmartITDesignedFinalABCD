package com.example.smartit

class Comment(var commentID : String, var postID : String, val userID : String , val commentContent : String, val date : String){
    constructor() : this("","","","",""){

    }
}
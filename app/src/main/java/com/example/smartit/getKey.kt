package com.example.smartit

class getKey{

    companion object{
        var key:String = "abc"

        fun get():String{
            return getKey.key
        }


    }
}
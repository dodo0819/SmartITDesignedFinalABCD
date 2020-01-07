
package com.example.smartit

data class User(val id:String, val name:String = "", val password:String = ""){
    constructor() : this("","",""){

    }


}
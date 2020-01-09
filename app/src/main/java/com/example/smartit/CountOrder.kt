package com.example.smartit

class CountOrder{

    companion object{
        var number:Int = 0
        var total:Int = 0
        var commentCount : Int = 0

        var getUser : User = User()
        var getPost : Post = Post()

        fun get():Int{
            return CountOrder.number
        }

        fun get1():Int{
            return CountOrder.total
        }

        fun get2():Int{
            return CountOrder.commentCount
        }
    }
}
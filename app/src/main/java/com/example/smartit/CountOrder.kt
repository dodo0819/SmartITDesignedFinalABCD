package com.example.smartit

class CountOrder{

    companion object{
        var number:Int = 1000
        var total:Int = 0

        fun get():Int{
            return CountOrder.number
        }

        fun get1():Int{
            return CountOrder.total
        }
    }
}
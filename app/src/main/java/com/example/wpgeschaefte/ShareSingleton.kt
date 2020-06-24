package com.example.wpgeschaefte

object ShareSingleton{
     var shareList: ArrayList<Share> = arrayListOf<Share>()
     var selectedShare: Share? = null
     var currentIndex: Int = 0

     //API CALL
     var currentPrice = 0.0
     var validSymbol = false
}
package com.example.wpgeschaefte

/**
 *
 * Contains the share-list and the selected share in the detail screen.
 */
object ShareSingleton{
     var shareList: ArrayList<Share> = arrayListOf()
     var selectedShare: Share? = null
     var currentIndex: Int = 0

     /**
      *  These are used forAPI CALL
      */
     var currentPrice = 0.0
     var validSymbol = false
}
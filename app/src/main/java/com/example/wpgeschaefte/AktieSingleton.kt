package com.example.wpgeschaefte

object AktieSingleton{
     var aktkieListe: ArrayList<Aktie> = arrayListOf<Aktie>()
     var selectedAktie: Aktie? = null
     var currentIndex: Int = 0

     //API CALL
     var currentPrice = 0.0
     var validSymbol = false
}
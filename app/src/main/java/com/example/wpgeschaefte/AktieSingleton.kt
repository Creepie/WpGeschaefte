package com.example.wpgeschaefte

object AktieSingleton{
     var aktkieListe: ArrayList<Aktie> = arrayListOf<Aktie>()
     var selectedAktie: Aktie? = null
     var currentIndex: Int = 0
}
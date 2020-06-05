package com.example.wpgeschaefte

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_wp__detail.*

class Wp_Detail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wp__detail)
        tV_wpDetail_wpName.text = AktieSingleton.selectedAktie?.kauf?.name
        var aktie = AktieSingleton.selectedAktie
        var liste = AktieSingleton.atkieListe
    }
}

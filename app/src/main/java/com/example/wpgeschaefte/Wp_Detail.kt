package com.example.wpgeschaefte

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_wp__detail.*
import kotlin.math.roundToInt

class Wp_Detail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wp__detail)
        var aktie = AktieSingleton.selectedAktie
        var liste = AktieSingleton.aktkieListe



        tV_wpDetail_wpName.text = aktie?.kauf?.name
        tV_wpDetail_symbol.text = aktie?.kauf?.symbol
        tV_wpDetail_buyDate.text = aktie?.kauf?.kaufDatum
        tv_wpDetail_perShareCurrentPrice.text = "€ ${aktie?.currentPrice.toString()}"
        tV_wpDetail_buyPrice.text = "€ ${aktie?.kauf?.wert.toString()}"

        val currentPrice = aktie?.kauf?.anzahl?.times(aktie?.currentPrice)
            ?.minus(aktie?.kauf?.spesen)

        tv_wpDetail_currentPrice.text = "€ ${currentPrice}"
    }
}

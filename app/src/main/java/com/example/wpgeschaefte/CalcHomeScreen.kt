package com.example.wpgeschaefte

import androidx.appcompat.app.AppCompatActivity
import java.math.RoundingMode
import kotlin.math.roundToInt
class CalcHomeScreen {

    //check value of the whole portfolio
    fun checkTotalSum(): Double {
        var sum: Double = 0.0

        for (Aktie in AktieSingleton.aktieListe) {

            //count the total value from the current stock
            sum += (Aktie.currentPrice * Aktie.kauf.anzahl) - Aktie.kauf.spesen

            //if there are any divi -> add them to the sum
            if (Aktie.dividenden != null) {
                for (Dividende in Aktie.dividenden) {
                    sum += Dividende.ertrag
                }
            }

            //if there are any expanses -> calculate minus from the sum
            if (Aktie.spesen != null) {
                for (Spesen in Aktie.spesen) {
                    sum -= Spesen.betrag
                }
            }

            //if the stock is sold -> calculate expanses from sold minus from the sum
            if (Aktie.sold && Aktie.soldData != null) {
                sum -= Aktie.soldData!!.spesen
            }
        }
        sum=sum.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        return sum
    }

    fun getPurchaseValue(): Double{
        var value = 0.0

        for (Aktie in AktieSingleton.aktieListe){
            value += (Aktie.kauf.kaufpreis*Aktie.kauf.anzahl)-Aktie.kauf.spesen
        }
        value = value.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        return value
    }
}

//profit € and %

//divi total in €

//profit  in € and % p.a.
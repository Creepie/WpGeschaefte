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
                    sum += Dividende.gutschrift
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

    fun gettotalDivi(): Double {
        var total: Double = 0.0

        for (Aktie in AktieSingleton.aktieListe){
            if(Aktie.dividenden.size!=0){
                for(Divi in Aktie.dividenden){
                    total +=Divi.gutschrift
                }
            }
        }
        total = total.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()

          return total
    }

    fun getProfit():Double {
        var profit:Double = 0.0
        val currentValue = checkTotalSum()
        val purchaseVAlue = getPurchaseValue()

        val diff = currentValue - purchaseVAlue


        profit = diff.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()


        return profit
    }

    fun getProfitPercent():Double {
        var percent: Double = 0.0
        var diff: Double = getProfit()

        percent = (diff/getPurchaseValue())*100
        percent = percent.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()

        return percent
    }
}

//profit € and %

//divi total in €

//profit  in € and % p.a.
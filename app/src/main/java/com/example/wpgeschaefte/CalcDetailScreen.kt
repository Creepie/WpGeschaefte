package com.example.wpgeschaefte

import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import java.math.RoundingMode


class CalcDetailScreen {

    //count of all taxes(active, sold and divi) 25% taxes
//
    fun totalTaxes(): Double {
        var totalTax: Double = 0.0

        for (Aktie in AktieSingleton.selectedAktie?.dividenden!!){
            totalTax += Aktie.steuern
        }
        if(AktieSingleton.selectedAktie!!.soldData!=null){
            totalTax += AktieSingleton.selectedAktie!!.soldData?.steuern!!
        }
        totalTax = totalTax.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        return totalTax
    }

    // count of all taxes plus yearly expenses
    fun getTotalExpanses(): Double {
        var totalExpanses: Double = 0.0

        totalExpanses += AktieSingleton.selectedAktie?.kauf?.spesen!!
        if(AktieSingleton.selectedAktie!!.soldData!=null){
            totalExpanses += AktieSingleton.selectedAktie!!.soldData?.spesen!!
        }


        for (Spese in AktieSingleton.selectedAktie!!.spesen){
            totalExpanses += Spese.betrag
        }
        for (DiviSpese in AktieSingleton.selectedAktie!!.dividenden){
            totalExpanses += DiviSpese.spesen
        }

        totalExpanses = totalExpanses.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        return totalExpanses
    }

    fun getAverageDivi(): Double {
        var average: Double = 0.0

        for (Divi in AktieSingleton.selectedAktie?.dividenden!!){
            average += Divi.ertrag
        }
        average = average / AktieSingleton.selectedAktie!!.kauf.anzahl

        average = average.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        return average
    }

    fun getAverageDiviPercent(): Double {
        var averagePercent: Double = 0.0

        for (Divi in AktieSingleton.selectedAktie?.dividenden!!){

        }
        averagePercent = averagePercent.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        return averagePercent
    }

    fun getTotalCredit(): Double {
        var credit: Double = 0.0

        for (Divi in AktieSingleton.selectedAktie?.dividenden!!){
            credit += Divi.gutschrift
        }

        credit = credit.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        return credit
    }

    fun getProfit() : Double {
        var profit: Double = 0.0

        profit += (AktieSingleton.selectedAktie?.currentPrice!!*AktieSingleton.selectedAktie!!.kauf.anzahl)
                    - AktieSingleton.selectedAktie!!.kauf.spesen

        if (AktieSingleton.selectedAktie!!.dividenden!=null){
            for (Divi in AktieSingleton.selectedAktie!!.dividenden){
                profit += Divi.ertrag
            }
        }
        if (AktieSingleton.selectedAktie!!.spesen!=null){
            for (Spesen in AktieSingleton.selectedAktie!!.spesen){
                profit -= Spesen.betrag
            }
        }

        if (AktieSingleton.selectedAktie!!.sold){
            profit -= AktieSingleton.selectedAktie!!.soldData?.spesen!!
            profit -= AktieSingleton.selectedAktie!!.soldData?.steuern!!
        }

        profit = profit.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        return profit
    }

}
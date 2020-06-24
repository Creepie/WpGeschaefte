package com.example.wpgeschaefte


import android.os.Build
import androidx.annotation.RequiresApi
import java.math.RoundingMode
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.abs


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
        if (AktieSingleton.selectedAktie?.dividenden !=null) {
            for (Divi in AktieSingleton.selectedAktie?.dividenden!!) {
                average += Divi.gutschrift
            }

            average = average / AktieSingleton.selectedAktie!!.kauf.anzahl

            average = average.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        }
        return average
    }

    fun getAverageDiviPercent(): Double {
        var averagePercent: Double = 0.0
        if (AktieSingleton.selectedAktie!!.dividenden.size==0) {
            return averagePercent
        }
            for (Divi in AktieSingleton.selectedAktie?.dividenden!!) {
                var percent = Divi.gutschrift / AktieSingleton.selectedAktie!!.kauf.kaufWert
                averagePercent += percent
            }
            averagePercent = averagePercent / AktieSingleton.selectedAktie!!.dividenden.size
            averagePercent =
                averagePercent.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()

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
                profit += Divi.gutschrift
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

        val diff = profit-(AktieSingleton.selectedAktie!!.kauf.kaufWert + AktieSingleton.selectedAktie!!.kauf.spesen)
        profit = (diff / (AktieSingleton.selectedAktie!!.kauf.kaufWert + AktieSingleton.selectedAktie!!.kauf.spesen))*100
        profit = profit.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        return profit
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getHoldyears():Double {
        var years:Double
//"dd-MMM-yyyy"
        val formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH)
        val string = AktieSingleton.selectedAktie?.kauf?.kaufDatum
        val buyDate: LocalDate? = LocalDate.parse(string, formatter)

        if (AktieSingleton.selectedAktie?.sold!!){
            val string2 = AktieSingleton.selectedAktie!!.soldData?.datum
            val soldDate: LocalDate? = LocalDate.parse(string2, formatter)
            val period: Period = Period.between(buyDate, soldDate)
            val diff: Int = period.getDays() + (period.months*30) + (period.years*365)
            years=diff.toDouble()

        }
        else {
            val now = LocalDate.now()
            val period: Period = Period.between(buyDate, now)
            val diff: Int = period.getDays() + (period.months*30) + (period.years*365)
            years= diff.toDouble()

        }

        years = abs((years / 365))

        years = years.toString().toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
        return years
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getProfitpA() : Double {
        var profitPA: Double = 0.0
        if(getHoldyears()<=1){
            profitPA = getProfit()
            profitPA = profitPA.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
            return profitPA
        }else {
            profitPA = getProfit() / getHoldyears()
            profitPA = profitPA.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        }
        return profitPA
    }


}





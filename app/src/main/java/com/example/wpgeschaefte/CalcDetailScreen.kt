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
    fun getTotalTaxes(): Double {
        var totalTax: Double = 0.0

        for (Share in ShareSingleton.selectedShare?.dividends!!){
            totalTax += Share.taxes
        }
        if(ShareSingleton.selectedShare!!.soldData!=null){
            totalTax += ShareSingleton.selectedShare!!.soldData?.taxes!!
        }
        totalTax = totalTax.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        return totalTax
    }

    // count of all taxes plus yearly expenses
    fun getTotalExpanses(): Double {
        var totalExpenses: Double = 0.0

        totalExpenses += ShareSingleton.selectedShare?.buyData?.expenses!!
        if(ShareSingleton.selectedShare!!.soldData!=null){
            totalExpenses += ShareSingleton.selectedShare!!.soldData?.expenses!!
        }


        for (expense in ShareSingleton.selectedShare!!.expenses){
            totalExpenses += expense.amount
        }
        for (dividend_expense in ShareSingleton.selectedShare!!.dividends){
            totalExpenses += dividend_expense.expenses
        }

        totalExpenses = totalExpenses.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        return totalExpenses
    }

    fun getAverageDividends(): Double {
        var average: Double = 0.0
        if (ShareSingleton.selectedShare?.dividends!!.size !=0) {
            for (Divi in ShareSingleton.selectedShare?.dividends!!) {
                average += Divi.profit
            }

            average = average / ShareSingleton.selectedShare!!.dividends.size

            average = average.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        }
        return average
    }

    fun getAverageDividendsPercent(): Double {
        var averagePercent: Double = 0.0
        if (ShareSingleton.selectedShare!!.dividends.size==0) {
            return averagePercent
        }
            for (dividend in ShareSingleton.selectedShare?.dividends!!) {
                var percent = (dividend.credit / ShareSingleton.selectedShare!!.buyData.purchaseValue)*100
                averagePercent += percent
            }
            averagePercent = averagePercent / ShareSingleton.selectedShare!!.dividends.size
            averagePercent =
                averagePercent.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()

        return averagePercent
    }

    fun getTotalCredit(): Double {
        var credit: Double = 0.0

        for (dividend in ShareSingleton.selectedShare?.dividends!!){
            credit += dividend.credit
        }

        credit = credit.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        return credit
    }

    fun getProfit() : Double {
        var profit: Double = 0.0

        profit += ShareSingleton.selectedShare?.currentPrice!!*ShareSingleton.selectedShare!!.buyData.amount


        if (ShareSingleton.selectedShare!!.dividends!=null){
            for (dividend in ShareSingleton.selectedShare!!.dividends){
                profit += dividend.credit
            }
        }
        if (ShareSingleton.selectedShare!!.expenses!=null){
            for (expense in ShareSingleton.selectedShare!!.expenses){
                profit -= expense.amount
            }
        }

        if (ShareSingleton.selectedShare!!.sold){
            profit -= ShareSingleton.selectedShare!!.soldData?.expenses!!
            profit -= ShareSingleton.selectedShare!!.soldData?.taxes!!
        }

        val diff = profit-(ShareSingleton.selectedShare!!.buyData.purchaseValue + ShareSingleton.selectedShare!!.buyData.expenses)
        profit = (diff / (ShareSingleton.selectedShare!!.buyData.purchaseValue))*100
        profit = profit.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        return profit
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getHoldyears():Double {
        var years:Double
//"dd-MMM-yyyy"
        val formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH)
        val string = ShareSingleton.selectedShare?.buyData?.purchaseDate
        val buyDate: LocalDate? = LocalDate.parse(string, formatter)

        if (ShareSingleton.selectedShare?.sold!!){
            val string2 = ShareSingleton.selectedShare!!.soldData?.date
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





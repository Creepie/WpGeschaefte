package com.example.wpgeschaefte

import java.math.RoundingMode

class CalcHomeScreen {

    //check value of the whole portfolio
    fun checkTotalSum(): Double {
        var sum: Double = 0.0

        for (Aktie in ShareSingleton.shareList) {

            //count the total value from the current stock
            sum += (Aktie.currentPrice * Aktie.buyData.amount) - Aktie.buyData.expenses

            //if there are any divi -> add them to the sum
            if (Aktie.dividends != null) {
                for (Dividende in Aktie.dividends) {
                    sum += Dividende.credit
                }
            }

            //if there are any expanses -> calculate minus from the sum
            if (Aktie.expenses != null) {
                for (Spesen in Aktie.expenses) {
                    sum -= Spesen.amount
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

        for (Aktie in ShareSingleton.shareList){
            value += (Aktie.buyData.purchasePrice*Aktie.buyData.amount)-Aktie.buyData.expenses
        }
        value = value.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        return value
    }

    fun gettotalDivi(): Double {
        var total: Double = 0.0

        for (Aktie in ShareSingleton.shareList){
            if(Aktie.dividends.size!=0){
                for(Divi in Aktie.dividends){
                    total +=Divi.credit
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
        val purchaseValue = getPurchaseValue()

        if(purchaseValue!=0.0){
            percent = (diff/getPurchaseValue())*100
        }

        percent = percent.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()

        return percent
    }
}

//profit € and %

//divi total in €

//profit  in € and % p.a.
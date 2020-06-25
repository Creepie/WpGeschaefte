package com.example.wpgeschaefte

import java.math.RoundingMode

class CalcHomeScreen {

    //check value of the whole portfolio
    fun checkTotalSum(): Double {
        var sum: Double = 0.0

        for (Share in ShareSingleton.shareList) {

            //count the total value from the current stock
            sum += (Share.currentPrice * Share.buyData.amount) - Share.buyData.expenses

            //if there are any divi -> add them to the sum
            if (Share.dividends != null) {
                for (Dividende in Share.dividends) {
                    sum += Dividende.credit
                }
            }

            //if there are any expanses -> calculate minus from the sum
            if (Share.expenses != null) {
                for (Expense in Share.expenses) {
                    sum -= Expense.amount
                }
            }

            //if the stock is sold -> calculate expanses from sold minus from the sum
            if (Share.sold && Share.soldData != null) {
                sum -= Share.soldData!!.expenses
                sum -= Share.soldData!!.taxes
            }
        }
        sum=sum.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        return sum
    }

    fun getPurchaseValue(): Double{
        var value = 0.0

        for (Share in ShareSingleton.shareList){
            value += (Share.buyData.purchasePrice*Share.buyData.amount)
        }
        value = value.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        return value
    }

    fun gettotalDivi(): Double {
        var total: Double = 0.0

        for (Share in ShareSingleton.shareList){
            if(Share.dividends.size!=0){
                for(Divi in Share.dividends){
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
        val purchaseValue = getPurchaseValue()

        val diff = currentValue - purchaseValue


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

    fun getRecyclerItemValue(item: Share): Double {
        var sum: Double = 0.0

            //count the total value from the current stock
            sum += (item.currentPrice * item.buyData.amount) - item.buyData.expenses

            //if there are any divi -> add them to the sum
            if (item.dividends != null) {
                for (Dividende in item.dividends) {
                    sum += Dividende.credit
                }
            }

            //if there are any expanses -> calculate minus from the sum
            if (item.expenses != null) {
                for (Expense in item.expenses) {
                    sum -= Expense.amount
                }
            }

            //if the stock is sold -> calculate expanses from sold minus from the sum
            if (item.sold && item.soldData != null) {
                sum -= item.soldData!!.expenses
                sum -= item.soldData!!.taxes
            }

        sum=sum.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        return sum
    }
}

//profit € and %

//divi total in €

//profit  in € and % p.a.
package com.example.wpgeschaefte

import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi

class CalcDetailScreen {

    //count of all taxes(active, sold and divi) 25% taxes
//
    fun totalTaxes(): Double {
        var totalTax: Double = 0.0

        for (a in AktieSingleton.aktieListe) {
            totalTax += 0.25 * a.kauf.wert

        }
        return totalTax
    }

    // count of all taxes plus yearly expenses
    fun totalTaxesPlusYearlyExpenses(): Double {
        var totalTax: Double = 0.0

        for (a in AktieSingleton.aktieListe) {
            totalTax += 0.25 * a.kauf.wert
            totalTax += a.kauf.spesen
        }
        return totalTax
    }

// Holdyears one digit after comma

    @RequiresApi(Build.VERSION_CODES.N)
    fun getHoldyears(date: String): Long {
        return 0
    }
// show value (buy in price)

// average divi in €

// average divi in %

//total divi net

//divi % of current item

}
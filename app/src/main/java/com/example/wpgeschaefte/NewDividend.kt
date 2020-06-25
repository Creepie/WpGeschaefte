package com.example.wpgeschaefte

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.pm.ActivityInfo
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_neue_dividende.*
import java.math.RoundingMode
import java.util.*

class NewDividend : AppCompatActivity(), View.OnClickListener {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_neue_dividende)
        /**
         * forces activity to stay in portrait mode
         */
        requestedOrientation =  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        bT_newDivi_save.setOnClickListener(this)
        bT_newDivi_cancel.setOnClickListener(this)
        eT_newDivi_date.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.bT_newDivi_cancel -> {
                    finish()
            }
            R.id.bT_newDivi_save -> {
                if (!allFilled()) {
                    getToast()
                } else {val date = eT_newDivi_date.text.toString()
                    val profit = eT_newDivi_profit.text.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
                    val volume = eT_newDivi_volume.text.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
                    val taxes = eT_newDivi_taxes.text.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
                    val expenses = eT_newDivi_expenses.text.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
                    val credit = eT_newDivi_credit.text.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
                    val amount = eT_newDivi_amount.text.toString().toInt()

                    val i = intent
                    i.putExtra(
                        "neueDivi", Dividends(
                            date,
                            profit,
                            volume,
                            taxes,
                            expenses,
                            credit,
                            amount
                        )
                    )
                    setResult(Activity.RESULT_OK, i)
                    finish()
                }

            }
            R.id.eT_newDivi_date -> {
                //creates Calendar
                val myCalendar = Calendar.getInstance()

                //creates date Picker to set day, month and year in the edit Text
                val datePickerOnDataSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    myCalendar.set(Calendar.YEAR, year)
                    myCalendar.set(Calendar.MONTH, monthOfYear)
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateLabel(myCalendar)
                }
                //show calendar with current date
                val DatePickerDialog = DatePickerDialog(this, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH))

                DatePickerDialog.datePicker.maxDate = System.currentTimeMillis()

                DatePickerDialog.show()
            }
        }
    }

    /**
     * Shows toast if edit text input is invalid
     */
    fun getToast() {
        val toast = Toast.makeText(applicationContext, "Bitte alle Felder korrekt ausfüllen", Toast.LENGTH_LONG)
        toast.show()
    }

    /**
     * check if every spinner is != default position and edit Text is not empty
     */
    fun allFilled(): Boolean {
        return !(eT_newDivi_date.text.isNullOrEmpty() ||
                eT_newDivi_profit.text.isNullOrEmpty() ||
                eT_newDivi_volume.text.isNullOrEmpty() ||
                eT_newDivi_taxes.text.isNullOrEmpty() ||
                eT_newDivi_expenses.text.isNullOrEmpty() ||
                eT_newDivi_credit.text.isNullOrEmpty() ||
                eT_newDivi_amount.text.isNullOrEmpty()

                )
    }

    /**
     * Updates edit text with selected date
     */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateLabel(myCalendar: Calendar) {
        val myFormat: String = "dd-MMM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        eT_newDivi_date.setText(sdf.format(myCalendar.time))
    }
}

/**
 * Dividends data class which implements Parcelable in order to pass objects via Intent
 */
data class Dividends(val date:String?, val profit:Double, val volume:Double, val taxes:Double, val expenses:Double, val credit:Double, val amount: Int) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeDouble(profit)
        parcel.writeDouble(volume)
        parcel.writeDouble(taxes)
        parcel.writeDouble(expenses)
        parcel.writeDouble(credit)
        parcel.writeInt(amount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Dividends> {
        override fun createFromParcel(parcel: Parcel): Dividends {
            return Dividends(parcel)
        }

        override fun newArray(size: Int): Array<Dividends?> {
            return arrayOfNulls(size)
        }
    }

}

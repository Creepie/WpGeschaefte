package com.example.wpgeschaefte

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.pm.ActivityInfo
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_wp__verkauf.*
import java.util.*

class ShareSellActivity : AppCompatActivity(), View.OnClickListener {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wp__verkauf)
        /**
         *forces activity to stay in portrait mode
         */
        requestedOrientation =  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        bT_wpSell_cancel.setOnClickListener(this)
        bT_wpSell_save.setOnClickListener(this)
        eT_wpSell_date.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.bT_wpSell_cancel -> {
                finish()
            }
            R.id.bT_wpSell_save -> {
            if(!allFilled()){
                getToast()
            }else {
                val date = eT_wpSell_date.text.toString()
                val profit = eT_wpSell_earning.text.toString().toDouble()
                val volume = eT_wpSell_volume.text.toString().toDouble()
                val taxes = eT_wpSell_taxes.text.toString().toDouble()
                val expenses = eT_wpSell_expenses.text.toString().toDouble()
                val credit = eT_wpSell_credit.text.toString().toDouble()

                val i = intent
                i.putExtra("aktieSell", ShareSell(
                    date,
                    profit,
                    volume,
                    taxes,
                    expenses,
                    credit
                ))
                setResult(Activity.RESULT_OK, i)
                finish()
            }
            }
            R.id.eT_wpSell_date -> {
                //creates Calendar
                val myCalendar = Calendar.getInstance()

                //creates date Picker to set day, month and year in the edit Text
                val datePickerOnDataSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    myCalendar.set(Calendar.YEAR, year)
                    myCalendar.set(Calendar.MONTH, monthOfYear)
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateLabel(myCalendar, eT_wpSell_date)
                }
                //show calendar with current date
                val datePickerDialog = DatePickerDialog(this, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH))

                datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

                datePickerDialog.show()
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
        return !(eT_wpSell_date.text.isNullOrEmpty() ||
                eT_wpSell_earning.text.isNullOrEmpty() ||
                eT_wpSell_volume.text.isNullOrEmpty() ||
                eT_wpSell_taxes.text.isNullOrEmpty() ||
                eT_wpSell_expenses.text.isNullOrEmpty() ||
                eT_wpSell_credit.text.isNullOrEmpty()
                )
    }
    /**
     * Updates edit text with selected date
     */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateLabel(myCalendar: Calendar, dateEditText: EditText) {
        val myFormat: String = "dd-MMM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        eT_wpSell_date.setText(sdf.format(myCalendar.time))
    }
}

/**
 * ShareSell data class which implements Parcelable in order to pass objects via Intent
 */
data class ShareSell(val date:String?, val currentPrice:Double, val volume:Double, val taxes:Double, val expenses:Double, val credit:Double): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble()
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeDouble(currentPrice)
        parcel.writeDouble(volume)
        parcel.writeDouble(taxes)
        parcel.writeDouble(expenses)
        parcel.writeDouble(credit)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShareSell> {
        override fun createFromParcel(parcel: Parcel): ShareSell {
            return ShareSell(parcel)
        }

        override fun newArray(size: Int): Array<ShareSell?> {
            return arrayOfNulls(size)
        }
    }
}

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
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_wp__verkauf.*
import java.util.*

class Wp_Verkauf : AppCompatActivity(), View.OnClickListener {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wp__verkauf)
        //forces activity to stay in portrait mode
        requestedOrientation =  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        bT_wpSell_cancel.setOnClickListener(this)
        bT_wpSell_save.setOnClickListener(this)
        eT_wpSell_date.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(v: View?) {
        when(v?.id){

            R.id.bT_wpSell_cancel -> {
                Log.i("LOG", "bT_wpSell_cancel was clicked")
                finish()
            }
            R.id.bT_wpSell_save -> {
                Log.i("LOG", "bT_wpSell_save was clicked")

            if(!allFilled()){
                getToast()
            }else {
                val datum = eT_wpSell_date.text.toString()
                val ertrag = eT_wpSell_earning.text.toString().toDouble()
                val volumen = eT_wpSell_volume.text.toString().toDouble()
                val steuern = eT_wpSell_taxes.text.toString().toDouble()
                val spesen = eT_wpSell_expenses.text.toString().toDouble()
                val gutschrift = eT_wpSell_credit.text.toString().toDouble()

                val i = intent
                i.putExtra("aktieSell", AktieSell(
                    datum,
                    ertrag,
                    volumen,
                    steuern,
                    spesen,
                    gutschrift
                ))
                setResult(Activity.RESULT_OK, i)
                finish()
            }
            }
            R.id.eT_wpSell_date -> {
                //create Calendar
                val myCalendar = Calendar.getInstance()

                //create date Picker to set day, month and year in the edit Text
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

    //Toast Message
    fun getToast() {
        val toast = Toast.makeText(applicationContext, "Bitte alle Felder korrekt ausfüllen", Toast.LENGTH_LONG)
        toast.show()
    }

    //check if every spinner is != default position and edit Text is not empty
    fun allFilled(): Boolean {
        return !(eT_wpSell_date.text.isNullOrEmpty() ||
                eT_wpSell_earning.text.isNullOrEmpty() ||
                eT_wpSell_volume.text.isNullOrEmpty() ||
                eT_wpSell_taxes.text.isNullOrEmpty() ||
                eT_wpSell_expenses.text.isNullOrEmpty() ||
                eT_wpSell_credit.text.isNullOrEmpty()
                )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateLabel(myCalendar: Calendar, dateEditText: EditText) {
        val myFormat: String = "dd-MMM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        eT_wpSell_date.setText(sdf.format(myCalendar.time))
    }
}


data class AktieSell(val datum:String?, val kurs:Double, val volumen:Double, val steuern:Double, val spesen:Double, val gutschrift:Double): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(datum)
        parcel.writeDouble(kurs)
        parcel.writeDouble(volumen)
        parcel.writeDouble(steuern)
        parcel.writeDouble(spesen)
        parcel.writeDouble(gutschrift)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AktieSell> {
        override fun createFromParcel(parcel: Parcel): AktieSell {
            return AktieSell(parcel)
        }

        override fun newArray(size: Int): Array<AktieSell?> {
            return arrayOfNulls(size)
        }
    }
}

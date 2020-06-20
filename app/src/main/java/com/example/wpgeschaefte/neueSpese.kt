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
import kotlinx.android.synthetic.main.activity_neue_dividende.*
import kotlinx.android.synthetic.main.activity_neue_spese.*
import java.util.*

class neueSpese : AppCompatActivity(), View.OnClickListener {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_neue_spese)
        //forces activity to stay in portrait mode
        requestedOrientation =  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        //set click listeners
        bT_newSpese_save.setOnClickListener(this)
        bT_newSpese_cancel.setOnClickListener(this)
        eT_newSpese_date.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(v: View?) {
        when(v?.id){

            R.id.bT_newSpese_cancel -> {
                Log.i("LOG", "bT_newSpese_cancel was clicked")
                finish()
            }

            R.id.bT_newSpese_save -> {
                if (!allFilled()) {
                    getToast()
                } else {
                    val datum = eT_newSpese_date.text.toString()
                    val betrag = String.format("%.2f",eT_newSpese_betrag.text.toString()).toDouble()

                    val i = intent
                    i.putExtra("neueSpese", Spese(
                        datum,
                        betrag
                    ))
                    setResult(Activity.RESULT_OK, i)
                    finish()
                }
            }

            R.id.eT_newSpese_date -> {
                //create Calendar
                val myCalendar = Calendar.getInstance()

                //create date Picker to set day, month and year in the edit Text
                val datePickerOnDataSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    myCalendar.set(Calendar.YEAR, year)
                    myCalendar.set(Calendar.MONTH, monthOfYear)
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateLabel(myCalendar, eT_newSpese_date)
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

    //check if every spinner is != default position and edit Text is not empty
    fun allFilled(): Boolean {
        return !(eT_newSpese_date.text.isNullOrEmpty() ||
                eT_newSpese_betrag.text.isNullOrEmpty()
                )
    }

    //Toast Message
    fun getToast() {
        val toast = Toast.makeText(applicationContext, "Bitte alle Felder korrekt ausfüllen", Toast.LENGTH_LONG)
        toast.show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateLabel(myCalendar: Calendar, dateEditText: EditText) {
        val myFormat: String = "dd-MMM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        eT_newSpese_date.setText(sdf.format(myCalendar.time))
    }
}


data class Spese(val datum: String?, val betrag: Double):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(datum)
        parcel.writeDouble(betrag)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Spese> {
        override fun createFromParcel(parcel: Parcel): Spese {
            return Spese(parcel)
        }

        override fun newArray(size: Int): Array<Spese?> {
            return arrayOfNulls(size)
        }
    }
}

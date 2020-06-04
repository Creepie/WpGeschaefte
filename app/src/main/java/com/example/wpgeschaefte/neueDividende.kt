package com.example.wpgeschaefte

import android.app.Activity
import android.app.DatePickerDialog
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
import kotlinx.android.synthetic.main.activity_neues_wertpapier.*
import java.util.*

class neueDividende : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_neue_dividende)

        //set Click Listeners
        bT_newDivi_save.setOnClickListener(this)
        bT_newDivi_cancel.setOnClickListener(this)
        eT_newDivi_date.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(v: View?) {
        when(v?.id){

            R.id.bT_newDivi_cancel -> {
                    Log.i("LOG", "bT_newDivi_cancel was clicked")
                    finish()
            }
            R.id.bT_newDivi_save -> {
                Log.i("LOG", "bT_nWertpapier_speichern was clicked")

                if (!allFilled()) {
                    getToast()
                } else {val datum = eT_newDivi_date.text.toString()
                    val ertrag = eT_newDivi_ertrag.text.toString().toDouble()
                    val volumen = eT_newDivi_volume.text.toString().toDouble()
                    val steuern = eT_newDivi_taxes.text.toString().toDouble()
                    val spesen = eT_newDivi_expenses.text.toString().toDouble()
                    val gutschrift = eT_newDivi_credit.text.toString().toDouble()

                    val i = intent
                    i.putExtra(
                        "neueDivi", Dividende(
                            datum,
                            ertrag,
                            volumen,
                            steuern,
                            spesen,
                            gutschrift
                        )
                    )
                    setResult(Activity.RESULT_OK, i)
                    finish()
                }

            }
            R.id.eT_newDivi_date -> {
                //create Calendar
                val myCalendar = Calendar.getInstance()

                //create date Picker to set day, month and year in the edit Text
                val datePickerOnDataSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    myCalendar.set(Calendar.YEAR, year)
                    myCalendar.set(Calendar.MONTH, monthOfYear)
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateLabel(myCalendar, eT_neues_Datum)
                }
                //show calendar with current date
                DatePickerDialog(this, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show()
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
        return !(eT_newDivi_date.text.isNullOrEmpty() ||
                eT_newDivi_ertrag.text.isNullOrEmpty() ||
                eT_newDivi_volume.text.isNullOrEmpty() ||
                eT_newDivi_taxes.text.isNullOrEmpty() ||
                eT_newDivi_expenses.text.isNullOrEmpty() ||
                eT_newDivi_credit.text.isNullOrEmpty()

                )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateLabel(myCalendar: Calendar, dateEditText: EditText) {
        val myFormat: String = "dd-MMM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        dateEditText.setText(sdf.format(myCalendar.time))
    }
}
//data stuff
data class Dividende(val datum:String?, val ertrag:Double, val volumen:Double, val steuern:Double, val spesen:Double, val gutschrift:Double ):Parcelable {
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
        parcel.writeDouble(ertrag)
        parcel.writeDouble(volumen)
        parcel.writeDouble(steuern)
        parcel.writeDouble(spesen)
        parcel.writeDouble(gutschrift)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Dividende> {
        override fun createFromParcel(parcel: Parcel): Dividende {
            return Dividende(parcel)
        }

        override fun newArray(size: Int): Array<Dividende?> {
            return arrayOfNulls(size)
        }
    }
}

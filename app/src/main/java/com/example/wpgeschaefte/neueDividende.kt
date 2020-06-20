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
import kotlinx.android.synthetic.main.activity_neues_wertpapier.*
import java.math.RoundingMode
import java.util.*

class neueDividende : AppCompatActivity(), View.OnClickListener {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_neue_dividende)

        //forces activity to stay in portrait mode
        requestedOrientation =  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
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
                    val ertrag = eT_newDivi_ertrag.text.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
                    val volumen = eT_newDivi_volume.text.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
                    val steuern = eT_newDivi_taxes.text.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
                    val spesen = eT_newDivi_expenses.text.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
                    val gutschrift = eT_newDivi_credit.text.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
                    val stk = eT_newDivi_stk.text.toString().toInt()

                    val i = intent
                    i.putExtra(
                        "neueDivi", Dividende(
                            datum,
                            ertrag,
                            volumen,
                            steuern,
                            spesen,
                            gutschrift,
                            stk
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
                    updateLabel(myCalendar, eT_newDivi_date)
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
                eT_newDivi_credit.text.isNullOrEmpty() ||
                eT_newDivi_stk.text.isNullOrEmpty()

                )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateLabel(myCalendar: Calendar, dateEditText: EditText) {
        val myFormat: String = "dd-MMM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        eT_newDivi_date.setText(sdf.format(myCalendar.time))
    }
}
//data stuff
data class Dividende(val datum:String?, val ertrag:Double, val volumen:Double, val steuern:Double, val spesen:Double, val gutschrift:Double , val stk: Int) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(datum)
        parcel.writeDouble(ertrag)
        parcel.writeDouble(volumen)
        parcel.writeDouble(steuern)
        parcel.writeDouble(spesen)
        parcel.writeDouble(gutschrift)
        parcel.writeInt(stk)
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

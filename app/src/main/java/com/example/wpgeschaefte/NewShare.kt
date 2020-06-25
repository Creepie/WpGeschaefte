package com.example.wpgeschaefte

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.pm.ActivityInfo
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.text.InputFilter
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_neues_wertpapier.*
import java.math.RoundingMode
import java.util.*


class NewShare : AppCompatActivity(), View.OnClickListener , SymbolAvailable{
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * forces activity to stay in portrait mode
         */
        requestedOrientation =  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_neues_wertpapier)
        setSupportActionBar(toolbar_neues)

        eT_new_buyDate.setOnClickListener(this)
        eT_new_symbol.setFilters(eT_new_symbol.getFilters() + InputFilter.AllCaps())

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       menuInflater.inflate(R.menu.menu_neueswertpapier, menu)
       return true
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.cancel_item -> {
                finish()
            }
            R.id.saveAktie_item -> {
                if (allFilled()) {
                    API().getValues(eT_new_symbol.text.toString(),this)
                }
            }
            R.id.eT_new_buyDate -> {
                //creates Calendar
                val myCalendar = Calendar.getInstance()

                //creates date Picker to set day, month and year in the edit Text
                val datePickerOnDataSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    myCalendar.set(Calendar.YEAR, year)
                    myCalendar.set(Calendar.MONTH, monthOfYear)
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateLabel(myCalendar, eT_new_buyDate)
                }
                //shows calendar with current date
                val DatePickerDialog = DatePickerDialog(this, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH))

                DatePickerDialog.datePicker.maxDate = System.currentTimeMillis()

                DatePickerDialog.show()
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.eT_new_buyDate -> {
                //creates Calendar
                val myCalendar = Calendar.getInstance()
                //creates date Picker to set day, month and year in the edit Text
                val datePickerOnDataSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    myCalendar.set(Calendar.YEAR, year)
                    myCalendar.set(Calendar.MONTH, monthOfYear)
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateLabel(myCalendar, eT_new_buyDate)
                }
                    //shows calendar with current date
                   val DatePickerDialog = DatePickerDialog(this, datePickerOnDataSetListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))

                DatePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                DatePickerDialog.show()
                }
            }
        }

    fun getInvalidSymbolToast() {
        val toast = Toast.makeText(applicationContext, "Ungültiges Symbol!", Toast.LENGTH_LONG)
        toast.show()
    }

    /**
     * check if every spinner is != default position and edit Text is not empty
     */
    fun allFilled(): Boolean {
        return !(eT_new_symbol.text.isNullOrEmpty() ||
                eT_new_name.text.isNullOrEmpty() ||
                eT_new_purchaseValue.text.isNullOrEmpty() ||
                eT_new_amount.text.isNullOrEmpty() ||
                eT_new_buyDate.text.isNullOrEmpty() ||
                eT_new_expenses.text.isNullOrEmpty()

                )
    }

    /**
     * Updates edit text with selected date
     */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateLabel(myCalendar: Calendar, dateEditText: EditText) {
        val myFormat: String = "dd-MMM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        dateEditText.setText(sdf.format(myCalendar.time))
    }

    /**
     *Gets called if the user entered a correct symbol. Sends all the entered data via Intent to HomeScreen activity
     */
    override fun available() {
        val name = eT_new_name.text.toString()
        val symbol = eT_new_symbol.text.toString()
        val purchasePrice = eT_new_purchaseValue.text.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        val purchaseDate = eT_new_buyDate.text.toString()
        val expenses = eT_new_expenses.text.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        val amount = eT_new_amount.text.toString().toInt()
        val value = 0.0
        val purchaseValue = amount*purchasePrice
        val i = intent
        var newShare =  Stockitem(
            name,
            symbol,
            purchasePrice,
            purchaseDate,
            expenses,
            amount,
            value,
            purchaseValue
        )
        i.putExtra("neueAktie", newShare)
        setResult(Activity.RESULT_OK, i)
        finish()
    }

    /**
     * Gets called if entered symbol is wrong
     */
    override fun notAvailable() {
        getInvalidSymbolToast()
    }
}

/**
 * Stockitem data class which implements Parcelable in order to pass objects via Intent
 */
data class Stockitem(val name: String?, val symbol: String?, val purchasePrice: Double, val purchaseDate: String?, val expenses: Double, val amount: Int, var value: Double, var purchaseValue: Double) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readDouble()
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(symbol)
        parcel.writeDouble(purchasePrice)
        parcel.writeString(purchaseDate)
        parcel.writeDouble(expenses)
        parcel.writeInt(amount)
        parcel.writeDouble(value)
        parcel.writeDouble(purchaseValue)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Stockitem> {
        override fun createFromParcel(parcel: Parcel): Stockitem {
            return Stockitem(parcel)
        }

        override fun newArray(size: Int): Array<Stockitem?> {
            return arrayOfNulls(size)
        }
    }

}

/**
 * Is used as asynchronous Listener in order to update Views dynamically and always gets called if data is "available" or "not Available".
 * If data is "available" the listener gets called in the onRespone()-method of the Retrofit service.
 * Not Available gets called if the HTTP-Connection failes or the entered symbol is wrong.
 * It
 */
interface SymbolAvailable{
    fun available()
    fun notAvailable()
}

package com.example.wpgeschaefte

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_homescreen.*
import kotlinx.android.synthetic.main.activity_neues_wertpapier.*
import kotlinx.android.synthetic.main.activity_neues_wertpapier.view.*

class neuesWertpapier : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_neues_wertpapier)

        //add Listeners
        bT_nWertpapier_speichern.setOnClickListener(this)
        bT_nWertpapier_abbrechen.setOnClickListener(this)
}
override fun onCreateOptionsMenu(menu: Menu?): Boolean {
   menuInflater.inflate(R.menu.menu_neueswertpapier, menu)
   return true
}

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.bT_nWertpapier_abbrechen -> {
                Log.i("LOG", "bT_nWerpapier_abbrechen was clicked")
                finish()
            }
            R.id.bT_nWertpapier_speichern -> {
                Log.i("LOG", "bT_nWertpapier_speichern was clicked")

                if (!allFilled()) {
                    getToast()
                } else {val name = eT_neues_Name.text.toString()
                    val symbol = eT_neues_Symbol.text.toString()
                    val kaufPreis = eT_neues_Kaufpreis.text.toString().toDouble()
                    val kaufDatum = eT_neues_Datum.text.toString()
                    val spesen = eT_neues_Spesen.text.toString().toDouble()
                    val anzahl = eT_neues_Anzahl.text.toString().toInt()
                    val wert = kaufPreis * anzahl - spesen
                    val i = intent
                    i.putExtra(
                        "neueAktie", Aktie(
                            name,
                            symbol,
                            kaufPreis,
                            kaufDatum,
                            spesen,
                            anzahl,
                            wert
                        )
                    )
                    setResult(Activity.RESULT_OK, i)
                    finish()
                }
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
        return !(eT_neues_Symbol.text.isNullOrEmpty() ||
                eT_neues_Name.text.isNullOrEmpty() ||
                eT_neues_Kaufpreis.text.isNullOrEmpty() ||
                eT_neues_Anzahl.text.isNullOrEmpty() ||
                eT_neues_Datum.text.isNullOrEmpty() ||
                eT_neues_Spesen.text.isNullOrEmpty()

                )
    }
}




//---------------------------------------------------
//data stuff

data class Aktie(val name: String?, val symbol: String?, val kaufpreis: Double, val kaufDatum: String?, val spesen: Double, val anzahl: Int, val wert: Double) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(symbol)
        parcel.writeDouble(kaufpreis)
        parcel.writeString(kaufDatum)
        parcel.writeDouble(spesen)
        parcel.writeInt(anzahl)
        parcel.writeDouble(wert)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Aktie> {
        override fun createFromParcel(parcel: Parcel): Aktie {
            return Aktie(parcel)
        }

        override fun newArray(size: Int): Array<Aktie?> {
            return arrayOfNulls(size)
        }
    }

}

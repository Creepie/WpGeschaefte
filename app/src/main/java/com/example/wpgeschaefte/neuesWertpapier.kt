package com.example.wpgeschaefte

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.Menu
import kotlinx.android.synthetic.main.activity_homescreen.*
import kotlinx.android.synthetic.main.activity_neues_wertpapier.*
import kotlinx.android.synthetic.main.activity_neues_wertpapier.view.*

class neuesWertpapier : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_neues_wertpapier)

        bT_nWertpapier_speichern.setOnClickListener {
            val name = eT_neues_Name.text.toString()
            val symbol = eT_neues_Symbol.text.toString()
            val kaufPreis = eT_neues_Kaufpreis.text.toString().toDouble()
            val kaufDatum = eT_neues_Datum.text.toString()
            val spesen = eT_neues_Spesen.text.toString().toDouble()
            val i = intent
            i.putExtra(
                "neueAktie", Aktie(
                    name,
                    symbol,
                    kaufPreis,
                    kaufDatum,
                    spesen
                )
            )
            setResult(Activity.RESULT_OK, i)
            finish()
        }

}
override fun onCreateOptionsMenu(menu: Menu?): Boolean {
   menuInflater.inflate(R.menu.menu_neueswertpapier, menu)
   return true
}
}

//---------------------------------------------------
//data stuff

data class Aktie(val name: String?, val symbol: String?, val kaufpreis: Double, val kaufDatum: String?, val spesen: Double): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(symbol)
        parcel.writeDouble(kaufpreis)
        parcel.writeString(kaufDatum)
        parcel.writeDouble(spesen)
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

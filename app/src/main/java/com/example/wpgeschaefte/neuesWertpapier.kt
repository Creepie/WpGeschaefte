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

class neuesWertpapier : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_neues_wertpapier)

        bT_nWertpapier_speichern.setOnClickListener {
            val name = eT_neues_Name.text.toString()
            val symbol = eT_neues_Symbol.text.toString()
            val i = intent
            i.putExtra(
                "neueAktie", Aktie(
                    name,
                    symbol
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

data class Aktie(val name: String?, val symbol: String?): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(symbol)
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

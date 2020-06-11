package com.example.wpgeschaefte

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_homescreen.*
import kotlinx.android.synthetic.main.activity_wp__detail.*

class Wp_Detail : AppCompatActivity() {
    var aktie = AktieSingleton.selectedAktie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wp__detail)

        var liste = AktieSingleton.aktkieListe

        bT_wpDetail_neueDivi.setOnClickListener {
            startActivityForResult(Intent(this, neueDividende::class.java), 100)
            true
        }


        rV_wpDetail_divis.layoutManager = LinearLayoutManager(this)

        if (aktie != null) {
            rV_wpDetail_divis.adapter = aktie!!.dividenden?.let { MyDiviRecyclerAdapter(it, this) }
        };

        tV_wpDetail_wpName.text = aktie?.kauf?.name
        tV_wpDetail_symbol.text = aktie?.kauf?.symbol
        tV_wpDetail_buyDate.text = aktie?.kauf?.kaufDatum
        tv_wpDetail_perShareCurrentPrice.text = "€ ${aktie?.currentPrice}"
        tv_wpDetail_perShareBuyPrice.text = "€ ${aktie?.kauf?.kaufpreis}"
        tV_wpDetail_buyPrice.text = "€ ${aktie?.kauf?.wert}"

        val currentPrice = aktie?.kauf?.spesen?.let {
            aktie?.currentPrice?.let { it1 ->
                aktie?.kauf?.anzahl?.times(it1)
                    ?.minus(it)
            }
        }

        tv_wpDetail_currentPrice.text = "€ ${currentPrice}"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val dividende = data?.getParcelableExtra<Dividende>("neueDivi")
            if (dividende != null) {
                aktie?.dividenden?.add(dividende)
                Log.i("LOG", "neue divi hinzugefügt")
                rV_wpDetail_divis.adapter?.notifyDataSetChanged()
            }
        }

    }
}


//----------------------------------------------------------------
//Recycler Stuff
class MyDiviViewHolder(view: View) : RecyclerView.ViewHolder(view){

    val tV_gutschrift = view.findViewById<TextView>(R.id.divi_recycleritem_gutschrift)
    val tV_datum = view.findViewById<TextView>(R.id.divi_recycleritem_datum)
    init {

    }
}

class MyDiviRecyclerAdapter(val list: MutableList<Dividende>, val context: Context) : RecyclerView.Adapter<MyDiviViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyDiviViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.divi_recycleritem,
            parent,
            false
        )
        return MyDiviViewHolder(view)
    }


    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: MyDiviViewHolder, position: Int) {

        val item = list[position]
        holder.tV_gutschrift.text = item.gutschrift.toString()
        holder.tV_datum.text = item.datum

        //get a Toast message with the the country text > if you clicked on the item
        holder.itemView.setOnClickListener{
        }
    }

}

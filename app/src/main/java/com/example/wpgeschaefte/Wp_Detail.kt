package com.example.wpgeschaefte

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_homescreen.*
import kotlinx.android.synthetic.main.activity_wp__detail.*
import kotlin.math.roundToInt

class Wp_Detail : AppCompatActivity() {
    var aktie = AktieSingleton.selectedAktie

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wp__detail)
        //forces activity to stay in portrait mode
        requestedOrientation =  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        bT_wpDetail_neueDivi.setOnClickListener {
            if (aktie?.sold == false){
                startActivityForResult(Intent(this, neueDividende::class.java), 100)
                true
            } else {
                val toast = Toast.makeText(applicationContext, "Aktie bereits verkauft, divi hinzufügen nicht mehr möglich", Toast.LENGTH_LONG)
                toast.show()
            }
        }

        bT_wpDetail_sell.setOnClickListener {
            if (aktie?.sold == false){
                startActivityForResult(Intent(this,Wp_Verkauf::class.java), 200)
                true
            } else {
                val toast = Toast.makeText(applicationContext, "Aktie bereits verkauft, verkaufen nicht mehr möglich", Toast.LENGTH_LONG)
                toast.show()
            }
        }

        bT_wpDetail_spesenHinzufuegen.setOnClickListener {
            if (aktie?.sold == false){
                startActivityForResult(Intent(this, neueSpese::class.java), 300)
                true
            } else {
                val toast = Toast.makeText(applicationContext, "Aktie bereits verkauft, spese hinzufügen nicht mehr möglich", Toast.LENGTH_LONG)
                toast.show()
            }
        }

        bT_wpDetail_aktieLöschen.setOnClickListener {
            AktieSingleton.aktieListe.removeAt(AktieSingleton.currentIndex)
            finish()
        }

        rV_wpDetail_divis.layoutManager = LinearLayoutManager(this)

        if (aktie != null) {
            rV_wpDetail_divis.adapter = aktie!!.dividenden?.let { MyDiviRecyclerAdapter(it, this) }
        };

        //add data to textviews
        tV_wpDetail_wpName.text = aktie?.kauf?.name
        tV_wpDetail_symbol.text = aktie?.kauf?.symbol
        tV_wpDetail_buyDate.text = aktie?.kauf?.kaufDatum
        tv_wpDetail_perShareCurrentPrice.text = "€ ${aktie?.currentPrice}"
        tv_wpDetail_perShareBuyPrice.text = "€ ${aktie?.kauf?.kaufpreis}"
        tv_wpDetail_currentPrice.text = "€ ${aktie?.kauf?.wert}"
        tV_wpDetail_pieces.text = "${aktie?.kauf?.anzahl.toString()} Stk"
        tV_wpDetail_taxes.text = "${CalcDetailScreen().totalTaxes()} €"
        tV_wpDetail_expenses.text = "${CalcDetailScreen().getTotalExpanses()} €"
        tV_wpDetail_averageDivi.text = "€ ${CalcDetailScreen().getAverageDivi()}"
        tV_wpDetail_totalCredits.text = "€ ${CalcDetailScreen().getTotalCredit()}"
        tV_wpDetail_profit.text ="Gewinn: +100% ( p.A.: 40% )"
        tV_wpDetail_buyPrice.text = "€ ${aktie?.kauf?.kaufpreis?.times(aktie?.kauf?.anzahl!!)}"



        val currentPrice = aktie?.kauf?.spesen?.let {
            aktie?.kauf?.kaufpreis.let { it1 ->
                if (it1 != null) {
                    aktie?.kauf?.anzahl?.times(it1)
                        ?.minus(it)
                }
            }
        }


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
            recreate()
        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK){
            val soldAktie = data?.getParcelableExtra<AktieSell>("aktieSell")
            aktie?.soldData = soldAktie
            aktie?.sold = true
            aktie?.currentPrice = aktie?.soldData?.kurs!!
            recreate()
            Log.i("LOG", "aktie wurde verkauft")
            recreate()
        }
        else if (requestCode == 300 && resultCode == Activity.RESULT_OK){
            val spese = data?.getParcelableExtra<Spese>("neueSpese")
            if (spese != null) {
                aktie?.spesen?.add(spese)

            }
            recreate()
        }
    }
}


//----------------------------------------------------------------
//Recycler Stuff
class MyDiviViewHolder(view: View) : RecyclerView.ViewHolder(view){

    val tV_stk = view.findViewById<TextView>(R.id.divi_recycleritem_stk)
    val tV_gutschrift = view.findViewById<TextView>(R.id.divi_recycleritem_gutschrift)
    val tV_datum = view.findViewById<TextView>(R.id.divi_recycleritem_datum)
    val tV_percent = view.findViewById<TextView>(R.id.divi_recycleritem_percent)
    val tV_perShare = view.findViewById<TextView>(R.id.divi_recycleritem_perShare)

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
        holder.tV_gutschrift.text = "Gutschrift: € ${item.gutschrift}"
        holder.tV_datum.text = "Datum: ${item.datum}"
        holder.tV_stk.text = "STK: ${item.stk}"

        val percent = AktieSingleton.selectedAktie?.kauf?.kaufWert?.let { item.gutschrift?.div(it)?.times(100) }
        holder.tV_percent.text = "% (netto): ${percent?.roundToInt()}"
        holder.tV_perShare.text = "Ertrag: € ${item.ertrag}"


        //get a Toast message with the the country text > if you clicked on the item
        holder.itemView.setOnClickListener{
        }
    }

}

package com.example.wpgeschaefte

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_wp__detail.*
import java.math.RoundingMode

class Wp_Detail : AppCompatActivity() {
    var share = ShareSingleton.selectedShare

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wp__detail)
        //forces activity to stay in portrait mode
        requestedOrientation =  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        bT_wpDetail_neueDivi.setOnClickListener {
            if (share?.sold == false){
                startActivityForResult(Intent(this, neueDividende::class.java), 100)
                true
            } else {
                val toast = Toast.makeText(applicationContext, "Aktie bereits verkauft, Dividende hinzufügen nicht mehr möglich", Toast.LENGTH_LONG)
                toast.show()
            }
        }

        bT_wpDetail_sell.setOnClickListener {
            if (share?.sold == false){
                startActivityForResult(Intent(this,Wp_Verkauf::class.java), 200)
                true
            } else {
                val toast = Toast.makeText(applicationContext, "Aktie ist bereits verkauft", Toast.LENGTH_LONG)
                toast.show()
            }
        }

        bT_wpDetail_addExpense.setOnClickListener {
            if (share?.sold == false){
                startActivityForResult(Intent(this, neueSpese::class.java), 300)
                true
            } else {
                val toast = Toast.makeText(applicationContext, "Aktie bereits verkauft, Spese hinzufügen nicht mehr möglich", Toast.LENGTH_LONG)
                toast.show()
            }
        }

        bT_wpDetail_aktieLöschen.setOnClickListener {
            ShareSingleton.shareList.removeAt(ShareSingleton.currentIndex)
            finish()
        }

        rV_wpDetail_divis.layoutManager = LinearLayoutManager(this)

        if (share != null) {
            rV_wpDetail_divis.adapter = share!!.dividends?.let { MyDiviRecyclerAdapter(it, this) }
        };

        //add data to textviews
        tV_wpDetail_wpName.text = share?.buyData?.name
        tV_wpDetail_symbol.text = share?.buyData?.symbol
        tV_wpDetail_buyDate.text = share?.buyData?.purchaseDate
        tv_wpDetail_perShareCurrentPrice.text = "€ ${share?.currentPrice.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()}"
        tv_wpDetail_perShareBuyPrice.text = "€ ${share?.buyData?.purchasePrice.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()}"
        tv_wpDetail_currentPrice.text = "€ ${share?.buyData?.value.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()}"
        tV_wpDetail_pieces.text = "${share?.buyData?.amount.toString()} Stk"
        tV_wpDetail_taxes.text = "Steuern: ${CalcDetailScreen().getTotalTaxes()} €"
        tV_wpDetail_expenses.text = "Spesen: ${CalcDetailScreen().getTotalExpanses()} €"
        tV_wpDetail_averageDivi.text = "€ ${CalcDetailScreen().getAverageDividends()}"
        tV_wpDetail_totalCredits.text = "€ ${CalcDetailScreen().getTotalCredit()}"
        tV_wpDetail_profit.text ="Gewinn: ${CalcDetailScreen().getProfit()}% ( p.A.: ${CalcDetailScreen().getProfitpA()}% )"
        tV_wpDetail_buyPrice.text = "€ ${share?.buyData?.purchasePrice?.times(share?.buyData?.amount!!)}"
        tv_wpDetail_holdingTime.text ="Jahre ${CalcDetailScreen().getHoldyears()}"
        tV_wpDetail_averagePercent.text = "${CalcDetailScreen().getAverageDividendsPercent()} %"



        val currentPrice = share?.buyData?.expenses?.let {
            share?.buyData?.purchasePrice.let { it1 ->
                if (it1 != null) {
                    share?.buyData?.amount?.times(it1)
                        ?.minus(it)
                }
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val dividend = data?.getParcelableExtra<Dividends>("neueDivi")
            if (dividend != null) {
                share?.dividends?.add(dividend)
                Log.i("LOG", "neue Dividende hinzugefügt")
                rV_wpDetail_divis.adapter?.notifyDataSetChanged()
            }
            recreate()
        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK){
            val soldAktie = data?.getParcelableExtra<ShareSell>("aktieSell")
            share?.soldData = soldAktie
            share?.sold = true
            share?.currentPrice = share?.soldData?.currentPrice!!
            recreate()
            Log.i("LOG", "Aktie wurde verkauft")
            recreate()
        }
        else if (requestCode == 300 && resultCode == Activity.RESULT_OK){
            val expense = data?.getParcelableExtra<Expense>("neueSpese")
            if (expense != null) {
                share?.expenses?.add(expense)

            }
            recreate()
        }
        //save in Json
        HomeScreen.createJSONFromStocks("myStocks.json", this)
    }
}


//----------------------------------------------------------------
//Recycler Stuff
class MyDiviViewHolder(view: View) : RecyclerView.ViewHolder(view){

    val tV_amount = view.findViewById<TextView>(R.id.divi_recycleritem_amount)
    val tV_credit = view.findViewById<TextView>(R.id.divi_recycleritem_credit)
    val tV_date = view.findViewById<TextView>(R.id.divi_recycleritem_date)
    val tV_percent = view.findViewById<TextView>(R.id.divi_recycleritem_percent)
    val tV_perShare = view.findViewById<TextView>(R.id.divi_recycleritem_perShare)

    init {

    }
}

class MyDiviRecyclerAdapter(val list: MutableList<Dividends>, val context: Context) : RecyclerView.Adapter<MyDiviViewHolder>() {

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
        holder.tV_credit.text = "Gutschrift: € ${item.credit.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()}"
        holder.tV_date.text = "Datum: ${item.date}"
        holder.tV_amount.text = "STK: ${item.amount}"

        val percent = ShareSingleton.selectedShare?.buyData?.purchaseValue?.let { item.credit?.div(it)?.times(100) }
        holder.tV_percent.text = "% (netto): ${percent?.toString()?.toBigDecimal()
            ?.setScale(2, RoundingMode.UP)?.toDouble()}"
        holder.tV_perShare.text = "Ertrag: € ${item.profit.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()}"


    }

}

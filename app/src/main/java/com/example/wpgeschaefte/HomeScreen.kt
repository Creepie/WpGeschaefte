package com.example.wpgeschaefte

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_homescreen.*
import java.io.Serializable
import kotlin.math.roundToInt

class HomeScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homescreen)
        setSupportActionBar(toolbar_homescreen)

        sP_homescreen.adapter = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.sP_list_homescreen)
        )

        rV_aktien.layoutManager = LinearLayoutManager(this)
        rV_aktien.adapter = MyRecyclerAdapter(AktieSingleton.aktkieListe, this);
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_homescreen, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.addAktie_item ->{
                startActivityForResult(Intent(this, neuesWertpapier::class.java),999)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //check the Result of the activity (neuer Fehler)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 999 && resultCode == Activity.RESULT_OK){
            val Aktie = data?.getParcelableExtra<Aktiepos>("neueAktie")
            if (Aktie != null) {
                val neueAktie = Aktie(Aktie, arrayListOf<Dividende>(),Aktie.kaufpreis, false)
                AktieSingleton.aktkieListe.add(neueAktie)
                Log.i("LOG", "neue Aktie hinzugefügt")
                //calc data
                calcAktie()
                //notify recycler adapter
                rV_aktien.adapter?.notifyDataSetChanged()
                val sum = AktieSingleton.aktkieListe.sumBy { x -> x.kauf.wert.roundToInt() }
                tV_gesamt.text = "Derzeitiger Wert deines Portfilios: € ${sum.toString()}"
            }
        }
    }

    fun calcAktie(){
        val currentAktie = AktieSingleton.aktkieListe[AktieSingleton.aktkieListe.size-1]
        currentAktie.currentPrice = AktieSingleton.currentPrice
        currentAktie.kauf.wert = (currentAktie.currentPrice * currentAktie.kauf.anzahl) - currentAktie.kauf.spesen
    }

    override fun onResume() {
        super.onResume()
        Log.i("LOG", "onResume")
        rV_aktien.adapter?.notifyDataSetChanged()
        val sum = AktieSingleton.aktkieListe.sumBy { x -> x.kauf.wert.roundToInt() }
        tV_gesamt.text = "Derzeitiger Wert deines Portfilios: € ${sum.toString()}"
    }
}



//----------------------------------------------------------------
//Recycler Stuff
class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){

    val tV_name = view.findViewById<TextView>(R.id.home_recycleritem_Name)
    val tv_kaufDatum = view.findViewById<TextView>(R.id.home_recycleritem_Kaufdatum)
    val tv_kaufPreis = view.findViewById<TextView>(R.id.home_recycleritem_Kaufkurs)
    val tv_wert = view.findViewById<TextView>(R.id.home_recycleritem_WertAktuell)
    val tv_currentPrice = view.findViewById<TextView>(R.id.home_recycleritem_KursAktuell)
    init {

    }
}

class MyRecyclerAdapter(val list: MutableList<Aktie>, val context: Context) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.home_recycleritem,
            parent,
            false
        )
        return MyViewHolder(view)
    }


    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val item = list[position]
        holder.tV_name.text = "${item.kauf.name} (${item.kauf.symbol})"
        holder.tv_kaufDatum.text = item.kauf.kaufDatum
        holder.tv_kaufPreis.text = "Preis: € ${item.kauf.kaufpreis.toString()}"
        holder.tv_wert.text = "Wert: € ${item.kauf.wert}"
        holder.tv_currentPrice.text = "Aktuell: € ${item.currentPrice.toString()}"

        //get a Toast message with the the country text > if you clicked on the item
        holder.itemView.setOnClickListener{
            Toast.makeText(
                it.context,"${item.kauf.name} , ${item.kauf.symbol}", Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(context, Wp_Detail::class.java)
            AktieSingleton.selectedAktie = item
            AktieSingleton.currentIndex = position
            context.startActivity(intent)
        }
    }
}

//data stuff
data class Aktie (val kauf: Aktiepos, val dividenden: MutableList<Dividende>, var currentPrice: Double, var sold: Boolean): Serializable


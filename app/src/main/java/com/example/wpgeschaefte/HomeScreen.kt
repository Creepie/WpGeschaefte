package com.example.wpgeschaefte

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_homescreen.*
import java.io.*
import java.lang.reflect.Type
import kotlin.math.roundToInt


class HomeScreen : AppCompatActivity() {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homescreen)
        setSupportActionBar(toolbar_homescreen)
        //forces activity to stay in portrait mode
        requestedOrientation =  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        rV_aktien.layoutManager = LinearLayoutManager(this)


        Log.i("LOG", "onCreate")
        //returns '[]' if the file contains no JSON-Objects
        //returns "" (empty string) if file  has not been created yet -> gets returned by FileNotFoundException
        val jsonString = readStocksFromJSON ( "myStocks.json")
        var gson = Gson()
        val listType: Type = object : TypeToken<ArrayList<Aktie?>?>() {}.type

        if(jsonString != ""){
            val aktie: List<Aktie> = gson.fromJson(jsonString, listType)
            AktieSingleton.aktieListe = aktie as ArrayList<Aktie>;
            rV_aktien.adapter = MyRecyclerAdapter(AktieSingleton.aktieListe, this);
            rV_aktien.adapter?.notifyDataSetChanged()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_homescreen, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.addAktie_item ->{
                startActivityForResult(Intent(this, neuesWertpapier::class.java),999)
                true}
             R.id.refresh_item -> {Toast.makeText(this, "Daten wurde aktualisiert!", Toast.LENGTH_SHORT).show()
                API.getValuesOnRefresh(rV_aktien.adapter as MyRecyclerAdapter)
                true}
            else -> super.onOptionsItemSelected(item)
        }
    }

    //check the Result of the activity (neuer Fehler)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 999 && resultCode == Activity.RESULT_OK){
            val Aktie = data?.getParcelableExtra<Aktiepos>("neueAktie")
            if (Aktie != null) {
                val neueAktie = Aktie(Aktie, arrayListOf<Dividende>(),arrayListOf<Spese>(), Aktie.kaufpreis, false, null)
                AktieSingleton.aktieListe.add(neueAktie)
                Log.i("LOG", "neue Aktie hinzugefügt")
                createJSONFromStocks("myStocks.json")
                //calc data
                calcAktie()
                //notify recycler adapter
                rV_aktien.adapter?.notifyDataSetChanged()
                val sum = CalcHomeScreen().checkTotalSum().toString()
                tV_gesamt.text = "Derzeitiger Wert deines Portfilios: € ${sum}"
            }
        }
    }

    private fun readStocksFromJSON(fileName: String): String? {
        return try {
            val fis = openFileInput(fileName)
            val isr = InputStreamReader(fis)
            val bufferedReader = BufferedReader(isr)
            val sb = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                sb.append(line)
            }
             sb.toString()
        } catch (fileNotFound: FileNotFoundException) {
            ""
        } catch (ioException: IOException) {
            null
        }
    }
     private fun createJSONFromStocks(fileName: String): Boolean {
         var g = Gson()
         var jsonString = g.toJson(AktieSingleton.aktieListe)
         var file = File(filesDir,fileName)
         var fos = file.outputStream()

        return try {
            if (jsonString != null) {
                fos.write(jsonString.toByteArray())
            }
            fos.close()
            true
        } catch (fileNotFound: FileNotFoundException) {
            false
        } catch (ioException: IOException) {
            false
        }
    }

    fun calcAktie(){
        val currentAktie = AktieSingleton.aktieListe[AktieSingleton.aktieListe.size-1]
        currentAktie.currentPrice = AktieSingleton.currentPrice
        currentAktie.kauf.wert = (currentAktie.currentPrice * currentAktie.kauf.anzahl) - currentAktie.kauf.spesen
    }

    override fun onResume() {
        super.onResume()
        Log.i("LOG", "onResume")
        rV_aktien.adapter?.notifyDataSetChanged()
        val purchaseValue = CalcHomeScreen().getPurchaseValue().toString()
        val sum = CalcHomeScreen().checkTotalSum().toString()
        tV_gesamt.text = "Derzeitiger Wert deines Portfolios: € ${sum}"
        tV_purchaseValue.text = "Kaufwert deines Portfolios: € ${purchaseValue}"
        createJSONFromStocks("myStocks.json")
    }
}



//----------------------------------------------------------------
//Recycler Stuff
class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){

    val tV_name = view.findViewById<TextView>(R.id.home_recycleritem_Name)
    val tv_bestand = view.findViewById<TextView>(R.id.home_recycleritem_bestand)
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
        var soldtext = "ja"

        if (item.sold){
            soldtext = "nein"
        }
        holder.tV_name.text = "${item.kauf.name} (${item.kauf.symbol})"
        holder.tv_bestand.text = "Bestand: ${soldtext}"
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
data class Aktie (val kauf: Aktiepos, val dividenden: MutableList<Dividende>, var spesen: MutableList<Spese>, var currentPrice: Double, var sold: Boolean, var soldData: AktieSell?): Serializable


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
import java.math.RoundingMode


class HomeScreen : AppCompatActivity() {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homescreen)
        setSupportActionBar(toolbar_homescreen)
        //forces activity to stay in portrait mode
        requestedOrientation =  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        rV_share.layoutManager = LinearLayoutManager(this)


        Log.i("LOG", "onCreate")
        //returns '[]' if the file contains no JSON-Objects
        //returns "" (empty string) if file  has not been created yet -> gets returned by FileNotFoundException
        val jsonString = readStocksFromJSON ( "myStocks.json", this)
        var gson = Gson()
        val listType: Type = object : TypeToken<ArrayList<Share?>?>() {}.type

        if(jsonString != ""){
            val share: List<Share> = gson.fromJson(jsonString, listType)
            ShareSingleton.shareList = share as ArrayList<Share>;
            rV_share.adapter = MyRecyclerAdapter(ShareSingleton.shareList, this);
            rV_share.adapter?.notifyDataSetChanged()
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
                API.getValuesOnRefresh(rV_share.adapter as MyRecyclerAdapter, this)
                true}
            else -> super.onOptionsItemSelected(item)
        }
    }

    //check the Result of the activity (neuer Fehler)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 999 && resultCode == Activity.RESULT_OK){

            val share = data?.getParcelableExtra<Stockitem>("neueAktie")
            if (ShareSingleton.validSymbol && share != null) {
                val newShare = Share(share, arrayListOf<Dividends>(),arrayListOf<Expense>(), share.purchasePrice, false, null)
                ShareSingleton.shareList.add(newShare)
                Log.i("LOG", "neue Aktie hinzugefügt")
                createJSONFromStocks("myStocks.json", this)
                //calc data
                calcShare()
                //notify recycler adapter
                rV_share.adapter?.notifyDataSetChanged()
                val sum = CalcHomeScreen().checkTotalSum().toString()
                tV_total.text = "Derzeitiger Wert deines Portfilios: € ${sum}"
            }
            recreate()
        }
    }


    fun calcShare(){
        val currentShare = ShareSingleton.shareList[ShareSingleton.shareList.size-1]
        currentShare.currentPrice = ShareSingleton.currentPrice
        currentShare.buyData.value = currentShare.currentPrice * currentShare.buyData.amount
    }

    override fun onResume() {
        super.onResume()
        Log.i("LOG", "onResume")
        rV_share.adapter?.notifyDataSetChanged()
        val purchaseValue = CalcHomeScreen().getPurchaseValue().toString()
        val sum = CalcHomeScreen().checkTotalSum().toString()
        tV_total.text = "Derzeitiger Wert deines Portfolios: € ${sum}"
        tV_purchaseValue.text = "Kaufwert deines Portfolios: € ${purchaseValue}"
        tV_totalProfit.text = "Gewinn/Verlust: € ${CalcHomeScreen().getProfit()} / ${CalcHomeScreen().getProfitPercent()}%"
        tV_totalDivi.text = "Dividenden gesamt: € ${CalcHomeScreen().gettotalDivi()}"
        createJSONFromStocks("myStocks.json", this)
    }

    companion object{

         fun readStocksFromJSON(fileName: String, context:Context): String? {
            return try {
                val fis = context.openFileInput(fileName)
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

         fun createJSONFromStocks(fileName: String, context:Context): Boolean {
            var g = Gson()
            var jsonString = g.toJson(ShareSingleton.shareList)
            var file = File(context.filesDir,fileName)
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
    }
}



//----------------------------------------------------------------
//Recycler Stuff
class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){

    val tV_name = view.findViewById<TextView>(R.id.home_recycleritem_name)
    val tv_stock = view.findViewById<TextView>(R.id.home_recycleritem_stock)
    val tv_purchasePrice = view.findViewById<TextView>(R.id.home_recycleritem_purchasePrice)
    val tv_value = view.findViewById<TextView>(R.id.home_recycleritem_currentValue)
    val tv_currentPrice = view.findViewById<TextView>(R.id.home_recycleritem_currentPrice)
    init {

    }
}

class MyRecyclerAdapter(val list: MutableList<Share>, val context: Context) : RecyclerView.Adapter<MyViewHolder>() {

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
        holder.tV_name.text = "${item.buyData.name} (${item.buyData.symbol})"
        holder.tv_stock.text = "Bestand: ${soldtext}"
        holder.tv_purchasePrice.text = "Preis: € ${item.buyData.purchasePrice.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()}"
        holder.tv_value.text = "Wert: € ${item.buyData.value.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()}"
        holder.tv_currentPrice.text = "Aktuell: € ${item.currentPrice.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()}"

        holder.itemView.setOnClickListener{
            val intent = Intent(context, Wp_Detail::class.java)
            ShareSingleton.selectedShare = item
            ShareSingleton.currentIndex = position
            context.startActivity(intent)
        }

        }
    }

//data stuff
data class Share (val buyData: Stockitem, val dividends: MutableList<Dividends>, var expenses: MutableList<Expense>, var currentPrice: Double, var sold: Boolean, var soldData: ShareSell?): Serializable


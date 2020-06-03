package com.example.wpgeschaefte

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_homescreen.*

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

        val list = arrayListOf<Aktie>()
        list.add(Aktie("OMV", "austria"))
        list.add(Aktie("Voest", "Italien"))


        rV_aktien.layoutManager = LinearLayoutManager(this)
        rV_aktien.adapter = MyRecyclerAdapter(list)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_homescreen, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.addAktie_item ->{
                startActivity(Intent(this, neuesWertpapier::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

//----------------------------------------------------------------
//Recycler Stuff
class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){

    val tV_name = view.findViewById<TextView>(R.id.home_recycleritem_Name)

    init {

    }
}

class MyRecyclerAdapter(val list: MutableList<Aktie>) : RecyclerView.Adapter<MyViewHolder>() {

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
        holder.tV_name.text = item.name

        //get a Toast message with the the country text > if you clicked on the item
        holder.itemView.setOnClickListener{
            Toast.makeText(
                it.context,"${item.name} , ${item.symbol}", Toast.LENGTH_SHORT
            ).show()
        }
    }

}

//---------------------------------------------------
//data stuff

data class Aktie(val name: String, val symbol: String)
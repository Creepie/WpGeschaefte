package com.example.wpgeschaefte

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import kotlinx.android.synthetic.main.activity_homescreen.*
import kotlinx.android.synthetic.main.activity_neues_wertpapier.*

class neuesWertpapier : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_neues_wertpapier)

/*   setSupportActionBar(toolbar_neues_wertpapier)

   bT_home.setOnClickListener{
       intent =  Intent(this, HomeScreen::class.java)
       startActivity(this.intent)
   }

   bT_cancel.setOnClickListener{
       intent = Intent(this,HomeScreen::class.java)
       startActivity(intent)
   }
*/
}
override fun onCreateOptionsMenu(menu: Menu?): Boolean {
   menuInflater.inflate(R.menu.menu_neueswertpapier, menu)
   return true
}
}

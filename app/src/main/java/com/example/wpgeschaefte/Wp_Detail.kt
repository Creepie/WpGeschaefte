package com.example.wpgeschaefte

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_wp__detail.*

class Wp_Detail : AppCompatActivity() {
    lateinit var list:MutableList<Aktiepos>
    lateinit var selectedItem:Aktiepos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //list = intent.getParcelableArrayListExtra<Aktiepos>("list").toTypedArray().toMutableList()
       // selectedItem = intent.getBundleExtra("selectedItem").getSerializable("selectedItem")
       // selectedItem = intent.getBundleExtra("selectedItem").getSerializable("selectedItem")
        setContentView(R.layout.activity_wp__detail)

        tV_wpDetail_wpName.text = selectedItem.name

    }
}

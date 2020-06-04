package com.example.wpgeschaefte

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Wp_Detail : AppCompatActivity() {
    lateinit var list:MutableList<Aktie>
    lateinit var selectedItem:Aktie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        list = intent.getParcelableArrayListExtra<Aktie>("list").toTypedArray().toMutableList()
        selectedItem = intent.getParcelableExtra("selectedItem")
        setContentView(R.layout.activity_wp__detail)
    }
}

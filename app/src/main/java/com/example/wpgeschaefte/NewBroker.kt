package com.example.wpgeschaefte

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class NewBroker : AppCompatActivity() {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        //forces activity to stay in portrait mode
        requestedOrientation =  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_neuer_broker)
    }
}

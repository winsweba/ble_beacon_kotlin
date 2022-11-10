package com.example.belkot

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast

class AboutPhoneActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_phone2)

        val textViewSupported: TextView = findViewById<TextView>(R.id.isSupported)

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {

                    Toast.makeText(applicationContext, "ble_not_supported,", Toast.LENGTH_LONG).show()
            textViewSupported.text = getString(R.string.ble_not_supported)
                    finish();
                }else {
                        Toast.makeText(applicationContext, "supported," ,Toast.LENGTH_SHORT).show();
            textViewSupported.text = getString(R.string.supported)
                }
    }
}
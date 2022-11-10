package com.example.belkot

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconTransmitter
import java.util.*


const val  PERMISSION_REQUEST_FINE_LOCATION  = 1
 const val  PERMISSION_REQUEST_BACKGROUND_LOCATION = 2

class MainActivity : AppCompatActivity()  {
//class MainActivity : Activity()  {
    lateinit var toggle : ActionBarDrawerToggle

    lateinit var drawerLayout: DrawerLayout

    lateinit var bluetoothAdapter: BluetoothAdapter

//    lateinit var

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)




        drawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            it.isChecked = true
            when(it.itemId){
                R.id.nav_home -> {
                    Toast.makeText(this, "Hone", Toast.LENGTH_SHORT).show()
//                    replaceFragment(HomeFragment(), it.title.toString())
                    val n = Intent(this@MainActivity, MainActivity::class.java)
                    startActivity(n)

                }
                R.id.nav_phone -> {
//                    Toast.makeText(this, "HoneAAAAA", Toast.LENGTH_SHORT).show()
                    val n = Intent(this@MainActivity, AboutPhoneActivity2::class.java)
                    startActivity(n)
                }

            }
            true
        }






        val bluetoothManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getSystemService(
                BluetoothManager::class.java
            )
        } else {
            TODO("VERSION.SDK_INT < M")
        }
//         bluetoothAdapter = bluetoothManager.adapter
        bluetoothAdapter = bluetoothManager.getAdapter()




        if (ContextCompat.checkSelfPermission(this@MainActivity,
                android.Manifest.permission.BLUETOOTH_ADVERTISE) !== PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                    android.Manifest.permission.BLUETOOTH_ADVERTISE)){

                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(android.Manifest.permission.BLUETOOTH_ADVERTISE), 1)
            }
            else{
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(android.Manifest.permission.BLUETOOTH_ADVERTISE), 1)
            }


        }





        var mBeaconTransmitter: BeaconTransmitter? = null

        mBeaconTransmitter = BeaconTransmitter(
            this,
            BeaconParser().setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25")
        )
        // Transmit a beacon with Identifiers 2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6 1 2
        // Transmit a beacon with Identifiers 2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6 1 2
        val beacon = Beacon.Builder()
            .setId1("2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6")
            .setId2("1")
            .setId3("2")
            .setManufacturer(0x0000) // Choose a number of 0x00ff or less as some devices cannot detect beacons with a manufacturer code > 0x00ff
            .setTxPower(-59)
            .setDataFields(Arrays.asList(*arrayOf(0L)))
            .build()
//        AdvertiseCallback

        mBeaconTransmitter.startAdvertising(beacon, object : AdvertiseCallback() {
            override fun onStartFailure(errorCode: Int) {
                Log.e("TAG------", "Advertisement start failed with code: $errorCode")
            }

            override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
                Log.i("TAG++++++++", "Advertisement start succeeded.********* ${settingsInEffect}")
            }
        })



    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,grantResults: IntArray) {

        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(applicationContext,
                "Yes", Toast.LENGTH_SHORT).show()

            ActivityCompat.requestPermissions(this@MainActivity,
                arrayOf(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION), 1)


        }
        val negativeButtonClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(applicationContext,
                "No", Toast.LENGTH_SHORT).show()
        }
        val neutralButtonClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(applicationContext,
                "Maybe", Toast.LENGTH_SHORT).show()
        }

        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@MainActivity,
                            android.Manifest.permission.ACCESS_FINE_LOCATION) ===
                                PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()

                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

         when (item.itemId){
            R.id.menu_scan -> {
                Toast.makeText(applicationContext, "click on setting", Toast.LENGTH_LONG).show()

                blePermission()


            }
             R.id.menu_stop ->{
                 Toast.makeText(applicationContext, "click on share", Toast.LENGTH_LONG).show()
                 return true
             }
         }

        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun locationPermission(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (bluetoothAdapter == null){
                Toast.makeText(this, "Device doesn't support bluetooth", Toast.LENGTH_SHORT).show()
            }


            if (ContextCompat.checkSelfPermission(this@MainActivity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(this@MainActivity,
                    android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) !== PackageManager.PERMISSION_GRANTED

            )
            {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                        android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)

                ) {

                    ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
                    ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION), 1)


                } else {
                    ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
                    ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION), 1)

                }
            }


        }

    }

    private fun blePermission(){



        if (!bluetoothAdapter.isEnabled) {
            val enableBluetooth = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this@MainActivity,
                        Manifest.permission.BLUETOOTH_CONNECT
                    )
                ) {
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                        1
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                        1
                    )
//                            startActivityForResult(enableBluetooth, 1)
                    Toast.makeText(this, "enableBluetooth", Toast.LENGTH_SHORT).show()
                    println("enableBluetooth++++++++++++++++++++++++++++")
                }
                Toast.makeText(this, "enableBluetooth", Toast.LENGTH_SHORT).show();
                System.out.println("enableBluetooth++++++++++++++++++++++++++++");
//                        startActivityForResult(enableBluetooth, 1)

            }
            startActivityForResult(enableBluetooth, 1)
            Toast.makeText(this, "enableBluetooth", Toast.LENGTH_SHORT).show()
            println("Per Given++++++++++++++++++++++++++++")
//            startActivity(enableBluetooth);
        }

    }





    private fun replaceFragment(fragment: Fragment, title: String ){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
        setTitle(title)


    }


}

//fun permissionsRequest (context: Context) {
//    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//
//        if (context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
//        == PackageManager.PERMISSION_GRANTED){
//            if(ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)){
//               var  builder =  AlertDialog.Builder(context)
//                builder.setTitle("This app needs background location access");
//                builder.setMessage("Please grant location access so this app can detect beacons in the background.");
//                builder.setPositiveButton("OK", null);
//                builder.setOnDismissListener(DialogInterface.OnDismissListener {
//
//                })
//            }
//        }
//    }
//}



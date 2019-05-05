package cz.uhk.umte.myDeviceInfo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_battery.*
import android.os.BatteryManager

class BatteryActivity : AppCompatActivity() {

    var receiver:PowerReceiver = PowerReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battery)
        title = "Baterie"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        receiver = PowerReceiver()

        // register BroadcastReceiver
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
           ifilter.addAction(Intent.ACTION_POWER_CONNECTED)
           ifilter.addAction(Intent.ACTION_POWER_DISCONNECTED)
           ifilter.addAction(Intent.ACTION_BATTERY_LOW)
           ifilter.addAction(Intent.ACTION_BATTERY_OKAY)
           applicationContext.registerReceiver(receiver, ifilter)
        }

        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)?: 0
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, 0)?: 0
        var temp = level.div(scale).times(100).toString() + " %"
        levelTextView.text = temp

        val chargingStatus: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val chargePlug: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) ?: -1
        stateTextView.text = chargePlugToString(chargingStatus, chargePlug)

        val temperature: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0)?:0
        temp = temperature.toFloat().div(10).toString() + " °C"
        tempTextView.text = temp

        val voltage: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)?: 0
        temp = voltage.toFloat().div(1000).toString() + " V"
        voltageTextView.text = temp
        technologyTextView.text = batteryStatus?.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)

        val health = batteryStatus?.getIntExtra(BatteryManager.EXTRA_HEALTH, 0)?: 0
        techStateTextView.text = batteryHealthToString(health)
    }

    override fun onDestroy() {
        super.onDestroy()
        applicationContext.unregisterReceiver(receiver)
    }

    // return text representation of Android charging status and type of source
    private fun chargePlugToString(chargingStatus:Int, chargePlug: Int): String {
        val chargeType:String
        when (chargePlug) {
            BatteryManager.BATTERY_PLUGGED_USB -> chargeType = "USB"
            BatteryManager.BATTERY_PLUGGED_AC -> chargeType = "AC"
            BatteryManager.BATTERY_PLUGGED_WIRELESS -> chargeType = "Wireless"
            else -> chargeType = "Neznámý"
        }

        val temp: String
        when (chargingStatus) {
            BatteryManager.BATTERY_STATUS_CHARGING -> temp = "Nabíjí se ($chargeType)"
            BatteryManager.BATTERY_STATUS_DISCHARGING -> temp = "Vybíjí se ($chargeType)"
            BatteryManager.BATTERY_STATUS_FULL -> temp = "Plně nabitá ($chargeType)"
            BatteryManager.BATTERY_STATUS_UNKNOWN -> temp = "Neznámý"
            BatteryManager.BATTERY_STATUS_NOT_CHARGING -> temp = "Nenabíjí se"
            else -> temp = "Neznámý"
        }
        return temp
    }

    // return text representation of Android battery health status
    private fun batteryHealthToString(batteryHealth: Int) : String {
        val temp: String
        when(batteryHealth){
            BatteryManager.BATTERY_HEALTH_COLD -> { temp = "Podchlazení" }
            BatteryManager.BATTERY_HEALTH_DEAD -> { temp = "Mrtvá" }
            BatteryManager.BATTERY_HEALTH_GOOD -> { temp = "Dobrý" }
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> { temp = "Přehřátí" }
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> { temp = "Vysoké napětí" }
            BatteryManager.BATTERY_HEALTH_UNKNOWN -> { temp = "Neznámý" }
            BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> { temp = "Nespecifikované selhání" }
            else -> {temp = "Neznámý" }
        }
        return temp
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

   inner class PowerReceiver: BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            when(intent.action) {
                // power disconected/conected
               Intent.ACTION_POWER_CONNECTED, Intent.ACTION_POWER_DISCONNECTED  -> {
                    val  mIntent: Intent ?= context.applicationContext.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
                    val chargingStatus: Int = mIntent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1)?: -1
                    val chargePlug: Int = mIntent?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)?: -1
                    stateTextView.text = chargePlugToString(chargingStatus, chargePlug)
               }
               // battery changed
               Intent.ACTION_BATTERY_CHANGED -> {
                    val mIntent: Intent ?= context.applicationContext.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
                    val level = mIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)?: 0
                    val scale = mIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, 0)?: 0
                    val temp = level.div(scale).times(100).toString() + " %"
                    levelTextView.text = temp
               }
            }
        }
    }

}
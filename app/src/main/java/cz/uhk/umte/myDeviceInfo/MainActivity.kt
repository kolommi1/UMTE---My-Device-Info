package cz.uhk.umte.myDeviceInfo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        systemButton.setOnClickListener {
            val intent = Intent(this, SystemActivity::class.java)
            startActivity(intent)
        }
        batteryButton.setOnClickListener {
            val intent = Intent(this, BatteryActivity::class.java)
            startActivity(intent)
        }
        sensorButton.setOnClickListener {
            val intent = Intent(this, SensorActivity::class.java)
            startActivity(intent)
        }
        appButton.setOnClickListener {
            val intent = Intent(this, AppActivity::class.java)
            startActivity(intent)
        }
        memoryButton.setOnClickListener {
            val intent = Intent(this, MemoryActivity::class.java)
            startActivity(intent)
        }
        cpuButton.setOnClickListener {
            val intent = Intent(this, CPUActivity::class.java)
            startActivity(intent)
        }
    }
}

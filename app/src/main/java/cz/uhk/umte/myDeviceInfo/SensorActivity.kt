package cz.uhk.umte.myDeviceInfo

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_sensor.*

class SensorActivity : AppCompatActivity(), SensorEventListener {

    private var sensorName = "Sensor"
    private var sensorList: List<Sensor> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor)
        title = "Senzory"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL)
        val sensorNameList: MutableList<String> = mutableListOf()
        for(sensor in sensorList){
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(sensor.type), SensorManager.SENSOR_DELAY_NORMAL)
            sensorNameList.add(sensor.name)
        }

        val sensorAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, android.R.id.text1, sensorNameList)
        spinner.adapter = sensorAdapter

        spinner.onItemSelected<String>{
            sensorName=it
            xTextView.text = "0.0" // X
            yTextView.text = "0.0" // Y
            zTextView.text = "0.0" // Z

            for(sensor in sensorList){
                if(sensor.name == it){
                    powerTextView.text = "${sensor.power} mA"
                    vendorTextView.text = sensor.vendor
                    versionTextView.text = sensor.version.toString()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(e: SensorEvent?) {
        e?.let{
            if(it.sensor.name == sensorName){
                xTextView.text = "X:   ${it.values[0]}" // X
                yTextView.text = "Y:   ${it.values[1]}" // Y
                zTextView.text = "Z:   ${it.values[2]}" // Z
            }
        }
    }
}

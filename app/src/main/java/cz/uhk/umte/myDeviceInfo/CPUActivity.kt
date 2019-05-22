package cz.uhk.umte.myDeviceInfo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.os.Build
import android.util.Log
import kotlinx.android.synthetic.main.activity_cpu.*
import java.io.*
import android.widget.SimpleAdapter

class CPUActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cpu)
        title = "CPU"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val cpuInfo = getCPUdata()
        listView.adapter  = SimpleAdapter (this, cpuInfo,  R.layout.row_cpu_info,
        arrayOf("label", "value"), intArrayOf(R.id.labelTextView, R.id.valueTextView))
    }

    private fun getCoreFrequency(coreNumber: Int): String {
        var frequency = "Zastaveno"
        val filePath = "/sys/devices/system/cpu/cpu$coreNumber/cpufreq/scaling_cur_freq"

        try {
            val reader = RandomAccessFile(filePath, "r")
            val value = reader.readLine().toLong() / 1000
            reader.close()
            frequency = "$value MHz"

        } catch (e: Exception) {
            Log.e("CPUactivity","Couldn't read core frequency")
        }
        return frequency
    }

    private fun getCPUdata() : ArrayList<HashMap<String, String>>{
        val list = ArrayList<HashMap<String, String>>()

        val cpuCores = Runtime.getRuntime().availableProcessors()
        var map = HashMap<String, String>()
        map["label"] = "Jádra: "
        map["value"] = cpuCores.toString()
        list.add(map)

        for (i in 0 until cpuCores){
            map = HashMap()
            map["label"] = "Jádro $i: "
            map["value"] = getCoreFrequency(i)
            list.add(map)
        }
        map = HashMap()
        map["label"] = "ABI: "
        map["value"] = Build.SUPPORTED_ABIS[0]
        list.add(map)

        val file = File("/proc/cpuinfo")

        if (file.exists()) {
            try {
                val br = BufferedReader(FileReader(file))
                var aLine = br.readLine()
                while (aLine != null) {
                    aLine = br.readLine()
                    if(aLine != null){
                        val values: List<String> = aLine.split(":")
                        if(values.size == 2){
                            // trim white spaces
                            values.map { it.trim() }

                            var temp = values[0]
                            if(values[0].startsWith("CPU ")) {
                               temp = temp.substring("CPU ".length)
                            }
                            map = HashMap()
                            map["label"] = temp.capitalize() + ": "
                            map["value"] = values[1]
                            list.add(map)
                        }
                    }
                }
                br.close()

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return list
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}

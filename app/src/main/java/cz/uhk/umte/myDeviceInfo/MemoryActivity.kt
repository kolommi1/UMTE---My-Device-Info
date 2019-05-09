package cz.uhk.umte.myDeviceInfo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.app.ActivityManager
import android.content.Context
import android.os.Environment
import kotlinx.android.synthetic.main.activity_memory.*
import android.os.StatFs
import android.support.v4.content.ContextCompat
import android.view.View

class MemoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory)
        title = "Paměti"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var temp: String
        // Internal storage (Data partition)
        var stat = StatFs(Environment.getDataDirectory().path)//getRootDirectory(System partition)
        var availableMemory = stat.availableBytes / 1048576L / 1024.0 // to MB, to GB
        var totalMemory = stat.totalBytes / 1048576L / 1024.0
        var usedMemory = totalMemory - availableMemory
        temp = "použito: ${usedMemory.format(2)} GB z ${totalMemory.format(2)} GB "
        internalMemoryTextView.text = temp
        internalMemoryProgressBar.progress = usedMemory.div(totalMemory).times(100.0).toInt()
        temp = "volná: ${availableMemory.format(2)} GB "
        freeInternalMemoryTextView.text = temp

        temp = ""
        var tempFree = ""
        var progres = 1
        var count = 0
        // SD card
        val externalStorageFiles = ContextCompat.getExternalFilesDirs(this, null)
        for (file in externalStorageFiles) {
            if (file.exists()) {
                // is removable
                if (Environment.isExternalStorageRemovable(file)) {
                    count +=1
                    stat = StatFs(file.path)
                    totalMemory = stat.totalBytes / 1048576L / 1024.0
                    availableMemory = stat.availableBytes / 1048576L / 1024.0
                    usedMemory = totalMemory - availableMemory
                    progres = usedMemory.div(totalMemory).times(100).toInt()
                    temp += "použito: ${usedMemory.format(2)} GB z ${totalMemory.format(2)} GB "
                    tempFree += "volná: ${availableMemory.format(2)} GB "
                }
            }
        }
        //No SD card
        if(count < 1){
            sdCardLabel.visibility = View.INVISIBLE
            sdCardImageView.visibility = View.INVISIBLE
            sdCardTextView.visibility = View.INVISIBLE
            sdCardProgressBar.visibility = View.INVISIBLE
            freeSdCardTextView.visibility = View.INVISIBLE
        }
        else {
            sdCardTextView.text = temp
            sdCardProgressBar.progress = progres
            freeSdCardTextView.text = tempFree
        }

        // RAM
        val mi = ActivityManager.MemoryInfo()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(mi)
        availableMemory = mi.availMem / 1048576L * 1.0
        totalMemory = mi.totalMem /  1048576L * 1.0
        usedMemory = totalMemory - availableMemory
        temp = "použito: ${usedMemory.format(0)} MB z ${totalMemory.format(0)} MB "
        ramMemoryTextView.text = temp
        ramMemoryProgressBar.progress = usedMemory.div(totalMemory).times(100).toInt()
        temp = "volná: ${availableMemory.format(0)} MB"
        freeRAMMemoryTextView.text = temp

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
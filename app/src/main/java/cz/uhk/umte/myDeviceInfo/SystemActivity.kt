package cz.uhk.umte.myDeviceInfo

import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_system.*

class SystemActivity : AppCompatActivity() {

    private object Constants {
        const val PERMISSION_REQUEST_READ_PHONE_STATE:Int = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_system)
        title = "Systém"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val text = Build.VERSION.RELEASE + " ("+ getCodeName() +")"
        osVersionTextView.text = text
        apiLevelTextView.text = Build.VERSION.SDK_INT.toString()
        brandTextView.text = Build.BRAND
        modelTextView.text = Build.MODEL
        vendorTextView.text = Build.MANUFACTURER
        boardTextView.text = Build.BOARD
        deviceTextView.text = Build.DEVICE
        buildIDTextView.text = Build.ID

        // older than API 26
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            serialNumberTextView.text = Build.SERIAL
        }
        else{
            // user canceled permission - ask for permission
            if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions( this,  arrayOf(android.Manifest.permission.READ_PHONE_STATE) ,
                    Constants.PERMISSION_REQUEST_READ_PHONE_STATE )
            }
            // permissions are granted
            else{
                serialNumberTextView.text = Build.getSerial()
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getCodeName() : String {
        var codeName = "Neznámý"
        // access version code using SDK version
        Build.VERSION_CODES::class.java.fields.filter {
            it.getInt(Build.VERSION_CODES::class) == Build.VERSION.SDK_INT
        }.forEach { codeName = it.name }
        // prepare for display
        if(codeName.startsWith("M")) {codeName = "Marshmallow"}
        if(codeName.startsWith("N")) {codeName = "Nougat"}
        if(codeName.startsWith("O")) {codeName = "Oreo"}
        if(codeName.startsWith("P")) {codeName = "Pie"}

        return codeName
    }

    @SuppressWarnings("MissingPermission")
    @TargetApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {

            Constants.PERMISSION_REQUEST_READ_PHONE_STATE -> {
                // permission granted
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    serialNumberTextView.text = Build.getSerial()
                }
                return
            }
        }
    }

}

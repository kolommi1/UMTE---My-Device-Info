package cz.uhk.umte.myDeviceInfo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

class MemoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory)
        title = "Paměti"
        supportActionBar?.setIcon(android.R.drawable.sym_def_app_icon)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
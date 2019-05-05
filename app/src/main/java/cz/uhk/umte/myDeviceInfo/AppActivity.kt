package cz.uhk.umte.myDeviceInfo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
        title = "Aplikace"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AppListFragment())
                .commitAllowingStateLoss()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}

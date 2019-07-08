package net.treelzebub.offlineindicator

import android.Manifest
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import net.treelzebub.netdetect._final.ConnectivityMonitor
import net.treelzebub.offlineindicator.debug.LogAdapter


class MainActivity : AppCompatActivity() {

    private val adapter by lazy { LogAdapter(this, 10) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        handlePermissions()
        logSetup()

        ConnectivityMonitor.observe(this) {
            info ->
            adapter.appendLog(info.toString())
        }

        fab.setOnClickListener {}
    }

    private fun handlePermissions() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {}
                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {}
                override fun onPermissionDenied(response: PermissionDeniedResponse?) {}
            })
            .check()
    }

    private fun logSetup() {
        recycler.layoutManager = LinearLayoutManager(this).also {
            it.stackFromEnd = true
        }
        recycler.adapter = this.adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}

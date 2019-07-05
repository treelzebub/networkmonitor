package net.treelzebub.offlineindicator

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import net.treelzebub.netdetect.ConnectionAwareViewModel
import net.treelzebub.netdetect.ConnectivityReceiver
import net.treelzebub.netdetect.cell.CellQuality
import net.treelzebub.offlineindicator.debug.LogAdapter


class MainActivity : AppCompatActivity() {

    private val ConnectionAwareViewModel.Quality.color: Int
        get() = when (this) {
            ConnectionAwareViewModel.Quality.None -> R.color.red
            ConnectionAwareViewModel.Quality.Poor -> R.color.yellow
            ConnectionAwareViewModel.Quality.Good -> R.color.green
            else -> throw IllegalArgumentException()
        }

    private val CellQuality.Quality.color: Int
        get() = when (this) {
            CellQuality.Quality.None -> R.color.red
            CellQuality.Quality.Poor -> R.color.yellow
            CellQuality.Quality.Moderate -> R.color.orange
            CellQuality.Quality.Good -> R.color.green_light
            CellQuality.Quality.Great -> R.color.green
        }

    private val adapter by lazy { LogAdapter(this, 10) }

    private val receiver by lazy { ConnectivityReceiver() }
    private val observer = Observer<String> { adapter.appendLog(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        logSetup()

        val connectionAwareViewModel = ConnectionAwareViewModel(this)
        connectionAwareViewModel.cellQuality.observe(this, observer)
//        connectionAwareViewModel.cellQuality.observe(this) {
//            val color = CellQuality.Quality.fromString(it).color
//            color(color)
//        }

        connectionAwareViewModel.signalStrength.observe(this, observer)
        connectionAwareViewModel.signalStrength.observe(this) {
            level.text = "Strength: $it"
        }

        connectionAwareViewModel.isNetworkConnected.observe(this, observer)
        connectionAwareViewModel.isNetworkConnected.observe(this) {
            can_connect.text = it
        }

        registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    private fun logSetup() {
        recycler.layoutManager = LinearLayoutManager(this).also {
            it.stackFromEnd = true
        }
        recycler.adapter = this.adapter

        receiver.status.observe(this, observer)
    }

    private fun color(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(this, colorRes)
        circle.setBackgroundColor(color)
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

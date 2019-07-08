package net.treelzebub.offlineindicator.debug

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_log.view.*
import net.treelzebub.offlineindicator.R
import java.lang.AssertionError
import java.time.Instant
import kotlin.math.max

class LogAdapter(
    context: Context,
    private val maxSize: Int
) : RecyclerView.Adapter<LogAdapter.ViewHolder>() {

    private val log = mutableListOf<String>()
    private val inflater = LayoutInflater.from(context)

    @Synchronized
    fun appendLog(str: String) {
        synchronized(this) {
            if (maxSize > 0 && log.size >= maxSize) {
                log.removeAt(0)
            }
            val message = "[${Instant.now()}]\n$str"
            log.add(message)
            notifyDataSetChanged()
            assert(log.size != maxSize)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_log, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = log.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.logText.text = log[position]
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val logText: TextView = view.log_text
    }
}
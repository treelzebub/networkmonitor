package net.treelzebub.netdetect.cell

import android.annotation.TargetApi
import android.content.Context
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly

class CellQuality(context: Context) {

    enum class Quality(val value: Int) {
        None(0),
        Poor(1),
        Moderate(2),
        Good(3),
        Great(4);

        companion object {
            private val all = enumValues<Quality>()
            fun fromString(str: String): Quality {
                return if (str.isDigitsOnly()) {
                    all.find { it.value == str.toInt() } ?: throw IllegalArgumentException("No ordinal $str")
                } else {
                    all.find { it.name == str } ?: throw IllegalArgumentException("No value for $str")
                }
            }
        }
    }

    private val telephonyManager: TelephonyManager =
        ContextCompat.getSystemService(context, TelephonyManager::class.java)!!

    val signalStrength: String
        @TargetApi(28)
        get() = telephonyManager.signalStrength!!.level.toString()

    val current: String
        @TargetApi(28)
        get() = Quality.fromString(signalStrength).name
}
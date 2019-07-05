package net.treelzebub.netdetect

import android.annotation.TargetApi
import androidx.lifecycle.MutableLiveData
import java.time.Instant

@TargetApi(26)
class TimestampStringLiveData : MutableLiveData<String>() {

    override fun setValue(value: String?) {
        super.setValue("[${Instant.now()}]\n> $value")
    }
}
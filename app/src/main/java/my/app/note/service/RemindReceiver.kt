package my.app.note.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Created by CCP on 2017.12.10 0010.
 *
 */
class RemindReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val currTime = System.currentTimeMillis()
    }
}
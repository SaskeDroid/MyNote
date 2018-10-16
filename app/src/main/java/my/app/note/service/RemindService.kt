package my.app.note.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.os.PowerManager

/**
 * Created by CCP on 2017.12.10 0010.
 *
 */
class RemindService : Service() {

    var receiver: RemindReceiver? = null
//    var wakeLock: PowerManager.WakeLock? = null

    override fun onCreate() {
        super.onCreate()
        receiver = RemindReceiver()
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_TIME_TICK) // 系统每隔一分钟会发出一次该广播
        registerReceiver(receiver, filter)

//        val powerManager: PowerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
//        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this::class.java.canonicalName)
//        if (!wakeLock!!.isHeld) {
//            wakeLock!!.acquire()
//        }
    }

    override fun onBind(intent: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)

//        if (wakeLock != null && wakeLock!!.isHeld) {
//            wakeLock!!.setReferenceCounted(false)
//            wakeLock!!.release()
//            wakeLock = null
//        }
    }
}
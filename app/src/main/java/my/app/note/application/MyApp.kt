package my.app.note.application

import android.app.Application
import android.content.Context

/**
 * Created by CCP on 2017.11.21 0021.
 *
 */
class MyApp : Application() {

    companion object {
        var mInstance: MyApp? = null

        fun getContext(): Context {
            return mInstance!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }
}
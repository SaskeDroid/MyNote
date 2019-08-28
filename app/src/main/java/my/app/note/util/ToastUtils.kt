package my.app.note.util

import android.content.Context
import android.widget.Toast

/**
 * Created by CCP on 2017.11.3.
 *
 */

object ToastUtils {

    private var mToast: Toast? = null
    private var lastMsg: String = ""
    private var firstTime: Long = 0L
    private var secondTime: Long = 0L

    /**
     * JvmOverloads实现Java调用中的方法重载，下面函数表示：
     * showToast(context, R.string.msg)
     * showToast(context, R.string.msg, Toast.LENGTH_LONG)
     */
    @JvmOverloads
    fun showToast(context: Context, msg: Int, len: Int = Toast.LENGTH_SHORT) = showMessage(context, context.getString(msg), len)

    @JvmOverloads
    fun showToast(context: Context, msg: String, len: Int = Toast.LENGTH_SHORT) = showMessage(context, msg, len)

    private fun showMessage(context: Context, msg: String, len: Int) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, len)
            mToast!!.show()
            firstTime = System.currentTimeMillis()
        } else {
            secondTime = System.currentTimeMillis()
            if (msg == lastMsg) { // Kotlin字符串内容比较可以用“==”
                if (secondTime - firstTime > len) {
                    mToast!!.show()
                }
            } else {
                lastMsg = msg
                mToast!!.setText(msg)
                mToast!!.show()
            }
        }
        firstTime = secondTime
    }
}

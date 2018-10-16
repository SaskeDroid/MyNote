package my.app.note.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.app.Activity


/**
 * Created by CCP on 2017.12.29 0029.
 *
 */
object KeyboardUtils {

    // 打开软键盘
    fun openKeyboard(context: Context, editText: EditText) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    // 关闭软键盘
    fun closeKeyboard(context: Context, editText: EditText) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    // 关闭所有软键盘
    fun closeAllKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
    }

    // 判断软键盘是否打开
    fun isSoftInputShow(activity: Activity): Boolean {
        val view = activity.window.peekDecorView()
        if (view != null) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            return imm.isActive && activity.window.currentFocus != null
        }
        return false
    }
}
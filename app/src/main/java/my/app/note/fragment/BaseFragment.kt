package my.app.note.fragment

import android.app.Fragment
import android.text.TextUtils
import android.widget.TextView
import my.app.note.R
import my.app.note.constant.Constants
import my.app.note.database.NoteBean
import my.app.note.util.DateUtils
import my.app.note.util.DialogUtils
import my.app.note.util.FileUtils
import my.app.note.util.ToastUtils

/**
 * Created by CCP on 2018.1.25 0025.
 *
 */
open class BaseFragment: Fragment() {

    fun showToast(msg: Int) = showToast(getString(msg))

    fun showToast(msg: String) = ToastUtils.showToast(activity, msg)

    @JvmOverloads
    fun showNoteDetail(noteBean: NoteBean, newText: String = "") {
        val content = if (TextUtils.isEmpty(newText)) {
            noteBean.noteContent
        } else {
            newText
        }
        showNoteDetail(noteBean.noteCreateTime, noteBean.noteUpdateTime, content)
    }

    fun showNoteDetail(t1: String, t2: String, newText: String) {
        val layout = activity.layoutInflater.inflate(R.layout.dialog_detail, null, false)
        val tvCreateTime = layout.findViewById<TextView>(R.id.tvCreateTime)
        val tvUpdateTime = layout.findViewById<TextView>(R.id.tvUpdateTime)
        val tvWordCount = layout.findViewById<TextView>(R.id.tvWordCount)
        tvCreateTime.text = DateUtils.getDateString(t1, Constants.FORMAT_YMDHMSE)
        tvUpdateTime.text = DateUtils.getDateString(t2, Constants.FORMAT_YMDHMSE)
        tvWordCount.text = FileUtils.getWordCount(newText)
        DialogUtils.getCustomDialog(activity, getString(R.string.detail), layout).show()
    }
}
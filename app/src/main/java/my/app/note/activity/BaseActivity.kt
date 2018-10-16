package my.app.note.activity

import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.app_bar.*
import me.imid.swipebacklayout.lib.app.SwipeBackActivity
import my.app.note.R
import my.app.note.constant.Constants
import my.app.note.database.NoteBean

import my.app.note.util.DateUtils
import my.app.note.util.DialogUtils
import my.app.note.util.FileUtils
import my.app.note.util.ToastUtils

/**
 * Created by CCP on 2017.11.6.
 *
 * SwipeBackActivity extends AppCompatActivity
 */

open class BaseActivity : SwipeBackActivity() {

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    @JvmOverloads
    fun setToolbarTitle(title: Int, showHomeAsUp: Boolean = true) = setToolbarTitle(getString(title), showHomeAsUp)

    @JvmOverloads
    fun setToolbarTitle(title: String, showHomeAsUp: Boolean = true) {
        toolbar.title = title
        setSupportActionBar(toolbar) // 用Toolbar取代原来的ActionBar
        supportActionBar!!.setDisplayHomeAsUpEnabled(showHomeAsUp) // 显示返回按钮
    }

    fun showToast(msg: Int) = showToast(getString(msg))

    fun showToast(msg: String) = ToastUtils.showToast(this, msg)

    fun setViewsClickListener(listener: View.OnClickListener, vararg views: View) {
        for (it in views) {
            it.setOnClickListener(listener)
        }
    }

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
        val layout = layoutInflater.inflate(R.layout.dialog_detail, null, false)
        val tvCreateTime = layout.findViewById<TextView>(R.id.tvCreateTime) // 原写法：layout.findViewById(R.id.tvCreateTime) as TextView 报错
        val tvUpdateTime = layout.findViewById<TextView>(R.id.tvUpdateTime)
        val tvWordCount = layout.findViewById<TextView>(R.id.tvWordCount)
        tvCreateTime.text = DateUtils.getDateString(t1, Constants.FORMAT_YMDHMSE)
        tvUpdateTime.text = DateUtils.getDateString(t2, Constants.FORMAT_YMDHMSE)
        tvWordCount.text = FileUtils.getWordCount(newText)
        DialogUtils.getCustomDialog(this, getString(R.string.detail), layout).show()
    }
}

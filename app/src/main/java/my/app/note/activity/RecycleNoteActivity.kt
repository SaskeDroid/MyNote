package my.app.note.activity

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import kotlinx.android.synthetic.main.activity_note.*
import my.app.note.R
import my.app.note.constant.Constants
import my.app.note.database.NoteBean
import my.app.note.util.DateUtils

/**
 * Created by CCP on 2017.12.9 0009.
 *
 */
class RecycleNoteActivity : BaseActivity() {

    private var noteBean: NoteBean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        setToolbarTitle(R.string.view_note)

        richEditor.visibility = View.GONE
        tvContent.visibility = View.VISIBLE
        tvContent.movementMethod = ScrollingMovementMethod.getInstance() // 可滚动

        noteBean = intent.getParcelableExtra(Constants.INTENT_NOTE_BEAN)
        tvContent.text = noteBean?.noteContent ?: "N/A" // 如果?:左侧的表达式不为空，则返回表达式的值，否则返回右侧的值"N/A"
        tvDate.text = DateUtils.getDateString(noteBean!!.noteUpdateTime, Constants.FORMAT_YMDHME)
    }
}
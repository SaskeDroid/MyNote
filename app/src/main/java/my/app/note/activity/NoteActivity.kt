package my.app.note.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import kotlinx.android.synthetic.main.activity_note.*
import my.app.note.R
import my.app.note.constant.Constants
import my.app.note.database.NoteBean
import my.app.note.util.*
import java.util.*
import android.graphics.BitmapFactory
import android.util.Log
import com.jph.takephoto.app.TakePhoto
import com.jph.takephoto.app.TakePhotoImpl
import com.jph.takephoto.model.InvokeParam
import com.jph.takephoto.model.TContextWrap
import com.jph.takephoto.model.TResult
import com.jph.takephoto.permission.InvokeListener
import com.jph.takephoto.permission.PermissionManager
import com.jph.takephoto.permission.TakePhotoInvocationHandler
import my.app.note.config.TakePhotoConfig

/**
 * Created by CCP on 2017.11.6.
 *
 */
class NoteActivity : BaseDBActivity(), View.OnTouchListener, TakePhoto.TakeResultListener, InvokeListener {

    private var noteId: Int = -1
    private var noteBean: NoteBean? = null
    private var oldText: String? = ""
    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var mHour: Int = 0
    private var mMinute: Int = 0
    private var createTimestamp: String = ""
    private var remindTimestamp: String = ""
    private var label: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        getTakePhoto().onCreate(savedInstanceState)

        noteId = intent.getIntExtra(Constants.INTENT_NOTE_ID, -1)
        noteBean = noteDBManager!!.select(noteId)
        if (noteId == -1) create() else edit()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        getTakePhoto().onSaveInstanceState(outState)
    }

    // 新建笔记
    private fun create() {
        setToolbarTitle(R.string.new_note)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        createTimestamp = DateUtils.getCurrentTimestamp()
        tvDate.text = String.format(getString(R.string.created_on_), DateUtils.getDateString(createTimestamp, Constants.FORMAT_YMDHM))
    }

    // 编辑笔记
    private fun edit() {
        setToolbarTitle(R.string.edit_note)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        oldText = noteBean!!.noteContent
        etContent.isCursorVisible = false
        etContent.setOnTouchListener(this)
        etContent.richText = oldText
        tvDate.text = String.format(getString(R.string.updated_on_), DateUtils.getDateString(noteBean!!.noteUpdateTime, Constants.FORMAT_YMDHM))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // 通过反射设置menu显示icon
//    override fun onMenuOpened(featureId: Int, menu: Menu?): Boolean {
//        if (menu != null) {
//            if (menu.javaClass.simpleName.equals("MenuBuilder", true)) {
//                val method = menu.javaClass.getDeclaredMethod("setOptionalIconsVisible", Boolean::class.java) as Method
//                method.isAccessible = true
//                method.invoke(menu, true)
//            }
//        }
//        return super.onMenuOpened(featureId, menu)
//    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        noteBean = noteDBManager!!.select(noteId)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                KeyboardUtils.closeKeyboard(this, etContent)
                save()
            }
            R.id.menuCamera -> {
                takePhoto() // TakePhoto自带权限管理
            }
            R.id.menuRemind -> {
                if (noteId == -1) {
                    showRemindSettingDialog()
                } else {
                    if (TextUtils.isEmpty(noteBean!!.noteRemindTime)) {
                        showRemindSettingDialog()
                    } else {
                        val remindTime = DateUtils.getDateString(noteBean!!.noteRemindTime, Constants.FORMAT_YMDHM)
                        DialogUtils.getMessageDialog(this, "", String.format(getString(R.string.remind_clear_tips), remindTime),
                                getString(R.string.remind_clear), getString(R.string.remind_reset), DialogInterface.OnClickListener { _, which ->
                            if (which == DialogInterface.BUTTON_NEGATIVE) {
                                setRemindTime("")
                            } else if (which == DialogInterface.BUTTON_POSITIVE) {
                                showRemindSettingDialog()
                            }
                        }).show()
                    }
                }
            }
            R.id.menuTags -> {
                val array = SharedUtils.getNoteTags(this).toTypedArray() // Set → Array
                DialogUtils.getSingleChoiceDialog(this, getString(R.string.set_label), CollectionUtils.mergeArray(arrayOf(getString(R.string.all_notes)), array), DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            if (noteId >= 0) {
                                noteDBManager!!.setLabel(noteId, label)
                            }
                        }
                        0 -> label = ""
                        else -> label = array[which - 1]
                    }
                }).show()
            }
            R.id.menuDetail -> {
                val content = etContent.text.toString().trim()
                if (noteId == -1) {
                    showNoteDetail(createTimestamp, createTimestamp, content)
                } else {
                    showNoteDetail(noteBean!!, content)
                }
            }
            R.id.menuDelete -> {
                if (noteId == -1) {
                    finish()
                } else {
                    DialogUtils.getMessageDialog(this, "", getString(R.string.whether_to_delete_this_note), DialogInterface.OnClickListener { _, _ ->
                        finish()
                        noteDBManager!!.recycle(noteId)
                        showToast(R.string.note_has_been_deleted)
                    }).show()
                }
            }
            R.id.menuUndo -> {
                etContent.setText(oldText)
                etContent.setSelection(etContent.text.length)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                etContent.isCursorVisible = true
                etContent.setSelection(etContent.text.length)
            }
            MotionEvent.ACTION_UP -> {
                v!!.performClick()
            }
        }
        return false
    }

    override fun onBackPressed() {
        save()
        super.onBackPressed()
    }

    // 保存笔记与否
    private fun save() {
        val newText = etContent.text.toString().trim()
        if (!TextUtils.isEmpty(newText)) {
            if (noteId >= 0) {
                // 编辑
                if (newText != oldText!!.trim()) {
                    val note = NoteBean()
                    note.noteUpdateTime = DateUtils.getCurrentTimestamp()
                    note.noteContent = newText
                    noteDBManager!!.update(noteId, note)
                }
            } else {
                // 新建
                val note = NoteBean()
                note.noteCreateTime = DateUtils.getCurrentTimestamp()
                note.noteUpdateTime = DateUtils.getCurrentTimestamp()
                note.noteContent = newText
                note.noteRemindTime = remindTimestamp
                note.noteTags = label
                noteDBManager!!.insert(note)
                if (!TextUtils.isEmpty(remindTimestamp)) {
                    val intent = Intent(Constants.ACTION_LIST_UPDATE)
                    intent.putExtra(Constants.INTENT_NOTE_ID, noteId)
                    sendBroadcast(intent)
                }
            }
        }
    }

    // 调用相册
    private fun takePhoto() {
        DialogUtils.getSelectDialog(this, "", arrayOf(getString(R.string.photo), getString(R.string.gallery)), DialogInterface.OnClickListener { _, which ->
            if (which == 0) {
                TakePhotoConfig.openCamera(this, getTakePhoto())
            } else if (which == 1) {
                TakePhotoConfig.opeGallery(this, getTakePhoto())
            }
        }).show()
    }

    override fun takeSuccess(result: TResult?) {
        val path = result?.image?.originalPath
        Log.i("CCP", "Take photo success: " + path)
        val bmp = BitmapFactory.decodeFile(path)
        if (bmp != null) {
            etContent.addImage(bmp, path)
        }
    }

    override fun takeCancel() {
        Log.i("CCP", "Take photo cancel")
    }

    override fun takeFail(result: TResult?, msg: String?) {
        showToast("$msg")
    }

    // 设置提醒时间对话框
    private fun showRemindSettingDialog() {
        val calendar: Calendar = Calendar.getInstance()
        DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            mYear = year
            mMonth = month + 1
            mDay = dayOfMonth
            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                mHour = hourOfDay
                mMinute = minute
                val remindTime = "$mYear-$mMonth-$mDay $mHour:$mMinute"
                val currTime = DateUtils.getCurrentDate(Constants.FORMAT_YMDHM)
                if (DateUtils.compareTimeString(currTime, remindTime, Constants.FORMAT_YMDHM) == 1) {
                    setRemindTime(DateUtils.getTimestampString(remindTime, Constants.FORMAT_YMDHM))
                } else {
                    showToast(R.string.remind_set_tips)
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    // 设置提醒时间
    private fun setRemindTime(timestamp: String) {
        if (noteId == -1) {
            remindTimestamp = timestamp
        } else {
            noteDBManager!!.setRemind(noteId, timestamp)
            val intent = Intent(Constants.ACTION_LIST_UPDATE)
            intent.putExtra(Constants.INTENT_NOTE_ID, noteId)
            sendBroadcast(intent)
            if (!TextUtils.isEmpty(timestamp)) {
                showToast(String.format(getString(R.string.remind_set_success), "$mYear-$mMonth-$mDay $mHour:$mMinute"))
            } else {
                showToast(R.string.remind_clear_success)
            }
        }
    }
}
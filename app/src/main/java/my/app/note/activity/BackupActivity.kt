package my.app.note.activity

import android.Manifest
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_backup.*
import my.app.note.R
import my.app.note.util.DialogUtils
import my.app.note.util.FileUtils
import my.app.note.util.PermissionUtils
import java.lang.StringBuilder

/**
 * Created by CCP on 2018.4.26 0026.
 *
 */
class BackupActivity : BaseDBActivity(), View.OnClickListener {

    private val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val REQUEST_EXPORT_PERMISSION: Int = 101
    private val REQUEST_IMPORT_PERMISSION: Int = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backup)
        setToolbarTitle(R.string.backup)
        setViewsClickListener(this, tvExport, tvImport)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvExport -> {
                if (PermissionUtils.requestPermissions(this, REQUEST_EXPORT_PERMISSION, permissions)) {
                    export()
                }
            }
            R.id.tvImport -> {
                if (PermissionUtils.requestPermissions(this, REQUEST_EXPORT_PERMISSION, permissions)) {
                    import()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults, object : PermissionUtils.OnRequestPermissionsResultCallback {
            override fun onPermissionsGranted(requestCode: Int, perms: List<String>, isAllGranted: Boolean) {
                when (requestCode) {
                    REQUEST_EXPORT_PERMISSION -> export()
                    REQUEST_IMPORT_PERMISSION -> import()
                }
            }

            override fun onPermissionsDenied(requestCode: Int, perms: List<String>, isAllDenied: Boolean) {
                showToast(R.string.request_permission_failed)
            }
        })
    }

    // 导出
    private fun export() {
        FileUtils.saveFileToStorage(this, noteDBManager!!.selectNoteList())
        showToast(R.string.export_success)
    }

    // 导入
    private fun import() {
        val list = FileUtils.getFileFromStorage(this)
        val sb = StringBuilder()
        if (list.size > 0 && !list.isEmpty()) {
            for (bean in list) {
                sb.append("ID-->${bean.noteId}").append("\n")
                sb.append("CREATE_TIME-->${bean.noteCreateTime}").append("\n")
                sb.append("UPDATE_TIME-->${bean.noteUpdateTime}").append("\n")
                sb.append("CONTENT-->${bean.noteContent}").append("\n")
            }
            DialogUtils.getMessageDialog(this, "Test", sb.toString()).show()
        } else {
            showToast(R.string.import_no_data)
        }
    }
}
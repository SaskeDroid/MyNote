package my.app.note.activity

import android.content.Intent
import android.os.Bundle
import com.jph.takephoto.app.TakePhoto
import com.jph.takephoto.app.TakePhotoImpl
import com.jph.takephoto.model.InvokeParam
import com.jph.takephoto.model.TContextWrap
import com.jph.takephoto.model.TResult
import com.jph.takephoto.permission.InvokeListener
import com.jph.takephoto.permission.PermissionManager
import com.jph.takephoto.permission.TakePhotoInvocationHandler
import my.app.note.database.NoteDBManager

/**
 * Created by CCP on 2018.3.29 0029.
 *
 */
open class BaseDBActivity : BaseActivity(), InvokeListener, TakePhoto.TakeResultListener {

    var noteDBManager: NoteDBManager? = null

    private var takePhoto: TakePhoto? = null
    private var invokeParam: InvokeParam? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteDBManager = NoteDBManager.getInstance(this)
    }

    fun getTakePhoto(): TakePhoto {
        if (takePhoto == null) {
            takePhoto = TakePhotoInvocationHandler.of(this).bind(TakePhotoImpl(this, this)) as TakePhoto
        }
        return takePhoto!!
    }

    override fun invoke(invokeParam: InvokeParam?): PermissionManager.TPermissionType {
        val type: PermissionManager.TPermissionType = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam!!.method)
        if(PermissionManager.TPermissionType.WAIT == type) {
            this.invokeParam = invokeParam
        }
        return type
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        getTakePhoto().onActivityResult(requestCode, resultCode, data)
    }

    // 权限申请结果
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // TakePhoto's permission manager
        val type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this)
    }

    override fun takeSuccess(result: TResult?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun takeCancel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun takeFail(result: TResult?, msg: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
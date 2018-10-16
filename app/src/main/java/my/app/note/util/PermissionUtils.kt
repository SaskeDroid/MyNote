package my.app.note.util

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast

import java.util.ArrayList

import my.app.note.R

/**
 * Created by CCP on 2018.4.19 0019.
 *
 */

object PermissionUtils {

    // 申请权限，使用系统onRequestPermissionsResult方法处理回调结果，返回是否获取到权限
    fun requestPermissions(activity: Activity, requestCode: Int, permissions: Array<String>): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        if (!hasPermissions(activity, permissions)) {
            if (shouldShowRequestPermissions(activity, permissions)) {
                Toast.makeText(activity, activity.getString(R.string.request_permission_never_ask), Toast.LENGTH_SHORT).show()
            } else {
                val deniedPermissions = getDeniedPermissions(activity, permissions)
                if (deniedPermissions != null && deniedPermissions.isNotEmpty()) {
                    ActivityCompat.requestPermissions(activity, deniedPermissions.toTypedArray(), requestCode)
                }
            }
            return false
        }
        return true
    }

    // 是否已经拥有权限
    private fun hasPermissions(activity: Activity, permissions: Array<String>): Boolean {
//        for (permission in permissions) {
//            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
//                return false
//            }
//        }
//        return true ↓↓↓
        return permissions.none { ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED }
    }

    // 用户拒绝了再次申请权限的请求（不再询问）
    private fun shouldShowRequestPermissions(activity: Activity, permissions: Array<String>): Boolean {
        val deniedPermissions = getDeniedPermissions(activity, permissions)
//        if (deniedPermissions != null) {
//            for (permission in deniedPermissions) {
//                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
//                    return true
//                }
//            }
//        } ↓↓↓
        deniedPermissions?.filterNot { ActivityCompat.shouldShowRequestPermissionRationale(activity, it) }?.forEach { return true }
        return false
    }

    // 获取未获得的权限列表
    private fun getDeniedPermissions(activity: Activity, permissions: Array<String>): List<String>? {
//        for (permission in permissions) {
//            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
//                deniedPermissions.add(permission)
//            }
//        } ↓↓↓
        val deniedPermissions = permissions.filter { ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED }
        return if (!deniedPermissions.isEmpty())
            deniedPermissions
        else null
    }

    // 申请权限返回方法
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray, callBack: OnRequestPermissionsResultCallback) {
        val granted = ArrayList<String>()
        val denied = ArrayList<String>()
        for (i in permissions.indices) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(permissions[i])
            } else {
                denied.add(permissions[i])
            }
        }

        if (!granted.isEmpty() && denied.isEmpty()) {
            callBack.onPermissionsGranted(requestCode, granted, denied.isEmpty())
        }
        if (!denied.isEmpty()) {
            callBack.onPermissionsDenied(requestCode, denied, granted.isEmpty())
        }
    }

    // 接口
    interface OnRequestPermissionsResultCallback {
        // 所有权限允许
        fun onPermissionsGranted(requestCode: Int, perms: List<String>, isAllGranted: Boolean)

        // 一项或多项权限拒绝
        fun onPermissionsDenied(requestCode: Int, perms: List<String>, isAllDenied: Boolean)
    }
}

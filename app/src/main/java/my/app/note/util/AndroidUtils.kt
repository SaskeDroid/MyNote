package my.app.note.util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.Intent
import android.net.Uri
import android.provider.Settings


/**
 * Created by CCP on 2017.10.30.
 *
 */

object AndroidUtils {

    private fun getPackageInfo(context: Context): PackageInfo? {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return packageInfo
    }

    fun getVersionName(context: Context): String {
        val packageInfo: PackageInfo? = getPackageInfo(context)
        if (packageInfo != null)
            return packageInfo.versionName
        return "N/A"
    }

    fun getVersionCode(context: Context): Int {
        val packageInfo: PackageInfo? = getPackageInfo(context)
        if (packageInfo != null)
            return packageInfo.versionCode
        return -1
    }

    // 打开应用信息
    @JvmOverloads
    fun openAppDetails(context: Context, packageName: String = context.packageName) {
        // 方法一
        val uri = Uri.parse("package:$packageName")
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)
        // 方法二
//        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//        intent.data = Uri.fromParts("package", packageName, null)
        context.startActivity(intent)
    }

    // 打开应用商店
    @JvmOverloads
    fun openAppMarket(context: Context, packageName: String = context.packageName) {
        val uri = Uri.parse("market://details?id=$packageName")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    }
}

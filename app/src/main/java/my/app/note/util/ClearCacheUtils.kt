package my.app.note.util

import java.io.File
import java.math.BigDecimal

import android.content.Context
import android.os.Environment

/**
 * Created by CCP on 2018.4.11 0011.
 *
 * Context.getCacheDir() -> /data/data/包名/cache
 * Context.getFilesDir() -> /data/data/包名/files
 * File("/data/data/" + context.getPackageName() + "/databases") -> /data/data/包名/databases
 * File("/data/data/" + context.getPackageName() + "/shared_prefs") -> /data/data/包名/shared_prefs
 * Context.getExternalCacheDir() -> SDCard/Android/data/包名/cache
 * Context.getExternalFilesDir() -> SDCard/Android/data/包名/files
 */
object ClearCacheUtils {

    @Throws(Exception::class)
    fun getCacheSize2Double(context: Context): Double {
        var cacheSize = getFolderSize(context.cacheDir)
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            cacheSize += getFolderSize(context.externalCacheDir)
        }
        return cacheSize.toDouble()
    }

    fun getCacheSize2String(context: Context): String {
        return getFormatSize(getCacheSize2Double(context))
    }

    @Throws(Exception::class)
    private fun getFolderSize(file: File?): Long {
        var size: Long = 0
        try {
            val fileList = file!!.listFiles()
            for (fl in fileList) {
                // 如果下面还有文件
                size += if (fl.isDirectory) {
                    getFolderSize(fl)
                } else {
                    fl.length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return size
    }

    // 格式化单位
    private fun getFormatSize(size: Double): String {
        val kiloByte = size / 1024
        if (kiloByte < 1) {
            // return size + "Byte";
            return "0K"
        }

        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 = BigDecimal(java.lang.Double.toString(kiloByte))
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB"
        }

        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(java.lang.Double.toString(megaByte))
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
        }

        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(java.lang.Double.toString(gigaByte))
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
        }

        val result4 = BigDecimal(teraBytes)
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }

    // 清除缓存
    fun clearAllCache(context: Context) {
        deleteDir(context.cacheDir)
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            deleteDir(context.externalCacheDir)
        }
    }

    // 删除目录
    private fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
//            for (i in 0 until children.size) {
//                val success = deleteDir(File(dir, children[i]))
//                if (!success) {
//                    return false
//                }
//            }
//            return dir.delete() ↓↓↓
            return if (children
                    .map { deleteDir(File(dir, it)) }
                    .none { it }) false else dir.delete()
        } else if (dir == null || dir.isDirectory) {
            return true
        }
        return dir.delete()
    }
}

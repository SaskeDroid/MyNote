package my.app.note.util

import android.content.Context
import android.os.Environment
import com.google.gson.Gson
import my.app.note.database.NoteBean
import java.io.*
import com.google.gson.reflect.TypeToken


/**
 * Created by CCP on 2017.11.8.
 *
 */
object FileUtils {

    // 从Assets文件夹读取文件
    private fun readFileFromAssets(context: Context, path: String): String {
        var fileString = ""
        try {
            val input: InputStream = context.resources.assets.open(path)
            val length: Int = input.available()
            val buffer = ByteArray(length) // byte[]
            input.read(buffer)
            fileString = String(buffer)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return fileString
    }

    // 获取日志信息
    fun getLogMessage(context: Context): String = readFileFromAssets(context, "log.txt")

    // 字符串字数统计
    fun getWordCount(string: String): String {
        // 判断中文
        val regex1 = "[\u4e00-\u9fa5]"
        var count1 = 0
        // 判断字母
        val regex2 = "[a-zA-Z]"
        var count2 = 0
        // 判断数字
        val regex3 = "[0-9]"
        var count3 = 0
        // 判断标点符号
        val regex4 = "[\\p{P}+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]"
        var count4 = 0
        // 判断空格、回车、换行、制表符号
        val regex5 = "[\u0008-\u000d| ]"
        var count5 = 0
        // 把每个char字符都存入String数组中
        val charArray = string.toCharArray()
        val strArray = arrayOfNulls<String>(charArray.size) // String[] strArray = new String[charArray.length];
        for (i in charArray.indices) {
            strArray[i] = charArray[i].toString()
            if (strArray[i]!!.matches(regex1.toRegex())) count1++
            if (strArray[i]!!.matches(regex2.toRegex())) count2++
            if (strArray[i]!!.matches(regex3.toRegex())) count3++
            if (strArray[i]!!.matches(regex4.toRegex())) count4++
            if (strArray[i]!!.matches(regex5.toRegex())) count5++
        }
        return "汉字:$count1 字母:$count2 数字:$count3 标点:$count4 特殊:$count5 字符:${string.length}"
    }

    // 保存数据到本地
    fun saveFileToStorage(context: Context, list: MutableList<NoteBean>) {
        val gson = Gson()
        val data = gson.toJson(list)
        val fos: FileOutputStream
        val oos: ObjectOutputStream
        try {
            val file = createBackupFile(context)
            fos = FileOutputStream(file.toString())
            oos = ObjectOutputStream(fos)
            oos.writeObject(data)

            fos.close()
            oos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 从本地读取数据
    fun getFileFromStorage(context: Context): MutableList<NoteBean> {
        val gson = Gson()
        var list = ArrayList<NoteBean>()
        val fis: FileInputStream
        val ois: ObjectInputStream
        try {
            val file = createBackupFile(context)
            fis = FileInputStream(file.toString())
            ois = ObjectInputStream(fis)
            val data = ois.readObject() as String
            list = gson.fromJson(data, object : TypeToken<List<NoteBean>>() {}.type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    // 在SD卡创建名为 "my_app_note.backup" 的备份文件
    private fun createBackupFile(context: Context): File {
        val files = File(Environment.getExternalStorageDirectory().absolutePath + "/${context.packageName}/")
        if (!files.exists()) {
            files.mkdirs()
        }
        val file = File(files, "note.bak")
        try {
            file.createNewFile()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file
    }
}
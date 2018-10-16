package my.app.note.util

import android.content.Context
import my.app.note.R
import my.app.note.constant.Constants
import java.text.SimpleDateFormat
import java.util.*
import java.text.ParseException

/**
 * Created by CCP on 2017.11.7.
 *
 */
object DateUtils {

    // 获取当前时间戳
    fun getCurrentTimestamp(): String = System.currentTimeMillis().toString()

    // 获取当前时间
    fun getCurrentDate(format: String): String = SimpleDateFormat(format, Locale.getDefault()).format(Date())

    // 获取当前日期星期
    @JvmOverloads
    fun getCurrentWeek(context: Context, timeStamp: String = getCurrentTimestamp()): String {
        val week: Array<String> = context.resources.getStringArray(R.array.week) // arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = Date(timeStamp.toLong())
        return week[calendar.get(Calendar.DAY_OF_WEEK) - 1]
    }

    // 时间戳转时间字符串
    fun getDateString(timeStamp: String, format: String): String {
        val date = Date(timeStamp.toLong())
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.format(date)
    }

    // 时间字符串转时间戳
    fun getTimestampString(dateString: String, format: String): String {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        var date = Date()
        try {
            date = sdf.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date.time.toString()
    }

    // 比较两个时间戳
    fun compareTimestamp(t1: String, t2: String): Int {
        val date1 = Date(t1.toLong())
        val date2 = Date(t2.toLong())
        return when {
            date2.time > date1.time -> 1
            date2.time < date1.time -> -1
            else -> 0
        }
    }

    // 比较两个时间字符串
    @JvmOverloads
    fun compareTimeString(s1: String, s2: String, format: String = Constants.FORMAT_YMDHMS): Int {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        val date1 = sdf.parse(s1)
        val date2 = sdf.parse(s2)
        return when {
            date2.time > date1.time -> 1
            date2.time < date1.time -> -1
            else -> 0
        }
    }
}
package my.app.note.util

import android.content.Context
import android.content.SharedPreferences


/**
 * Created by CCP on 2017.12.1 0001.
 *
 */
object SharedUtils {

    private val SHARED_NAME: String = "SHARED_MY_APP_NOTE"
    private val KEY_NOTES_TAGS: String = "KEY_NOTES_TAGS"
    private val KEY_PASSWORD: String = "KEY_PASSWORD"
    private val KEY_LIKE_COUNT: String = "KEY_LIKE_COUNT"

    private fun getSharedPreferences(context: Context): SharedPreferences = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)

    fun getBoolean(context: Context, key: String): Boolean = getSharedPreferences(context).getBoolean(key, false)

    @Synchronized
    fun saveBoolean(context: Context, key: String, value: Boolean) {
        val shared = getSharedPreferences(context)
        val editor = shared.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getFloat(context: Context, key: String): Float = getSharedPreferences(context).getFloat(key, -1F)

    @Synchronized
    fun saveFloat(context: Context, key: String, value: Float) {
        val shared = getSharedPreferences(context)
        val editor = shared.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    fun getInt(context: Context, key: String): Int = getSharedPreferences(context).getInt(key, -1)

    @Synchronized
    fun saveInt(context: Context, key: String, value: Int) {
        val shared = getSharedPreferences(context)
        val editor = shared.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getLong(context: Context, key: String): Long = getSharedPreferences(context).getLong(key, -1L)

    @Synchronized
    fun saveLong(context: Context, key: String, value: Long?) {
        val share = getSharedPreferences(context)
        val editor = share.edit()
        editor.putLong(key, value!!)
        editor.apply()
    }

    fun getString(context: Context, key: String): String = getSharedPreferences(context).getString(key, "")

    @Synchronized
    fun saveString(context: Context, key: String, value: String) {
        val share = getSharedPreferences(context)
        val editor = share.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getStringSet(context: Context, key: String): MutableSet<String> = getSharedPreferences(context).getStringSet(key, HashSet<String>())

    @Synchronized
    fun saveStringSet(context: Context, key: String, value: Set<String>) {
        val share = getSharedPreferences(context)
        val editor = share.edit()
        editor.putStringSet(key, value)
        editor.apply()
    }

    // 保存标签
    fun saveNoteTags(context: Context, tags: Set<String>) = saveStringSet(context, KEY_NOTES_TAGS, tags)
    fun getNoteTags(context: Context): MutableSet<String> = getStringSet(context, KEY_NOTES_TAGS)
}
package my.app.note.constant

/**
 * Created by CCP on 2017.11.7.
 *
 */
object Constants {
    // Intent跳转的Key常量
    const val INTENT_NOTE_ID: String = "INTENT_NOTE_ID"
    const val INTENT_NOTE_BEAN: String = "INTENT_NOTE_BEAN"

    // 时间日期格式
    const val FORMAT_YMD: String = "yyyy-MM-dd"
    const val FORMAT_YMDHM: String = "yyyy-MM-dd HH:mm"
    const val FORMAT_YMDHME: String = "yyyy-MM-dd HH:mm EEEE"
    const val FORMAT_YMDHMS: String = "yyyy-MM-dd HH:mm:ss"
    const val FORMAT_YMDHMSE: String = "yyyy-MM-dd HH:mm:ss EEEE"
    const val FORMAT_MDHM: String = "MM-dd HH:mm"
    const val FORMAT_MDE: String = "MM月dd日 EEEE"

    // Intent action
    const val ACTION_LIST_ITEM_CLICK = "my.app.note.action.LIST_ITEM_CLICK"
    const val ACTION_LIST_UPDATE = "my.app.note.action.LIST_UPDATE"

    const val KEY_LIKE_COUNT = "KEY_LIKE_COUNT"

    // SharedUtils
    const val SHARED_NAME = "SHARED_MY_APP_NOTE"
    const val KEY_NOTES_TAGS = "KEY_NOTES_TAGS"
}
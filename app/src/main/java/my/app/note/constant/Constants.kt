package my.app.note.constant

/**
 * Created by CCP on 2017.11.7.
 *
 */
object Constants {
    // Intent跳转的Key常量
    val INTENT_NOTE_ID: String = "INTENT_NOTE_ID"
    val INTENT_NOTE_BEAN: String = "INTENT_NOTE_BEAN"

    // 时间日期格式
    val FORMAT_YMD: String = "yyyy-MM-dd"
    val FORMAT_YMDHM: String = "yyyy-MM-dd HH:mm"
    val FORMAT_YMDHME: String = "yyyy-MM-dd HH:mm EEEE"
    val FORMAT_YMDHMS: String = "yyyy-MM-dd HH:mm:ss"
    val FORMAT_YMDHMSE: String = "yyyy-MM-dd HH:mm:ss EEEE"
    val FORMAT_MDHM: String = "MM-dd HH:mm"
    val FORMAT_MDE: String = "MM月dd日 EEEE"

    // Intent action
    val ACTION_LIST_ITEM_CLICK = "my.app.note.action.LIST_ITEM_CLICK"
    val ACTION_LIST_UPDATE = "my.app.note.action.LIST_UPDATE"

    //
    val KEY_LIKE_COUNT = "KEY_LIKE_COUNT"
}
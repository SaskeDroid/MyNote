package my.app.note.bean

import my.app.note.util.PinyinUtils

/**
 * Created by CCP on 2018.4.12 0012.
 *
 */

class DemoItem(var name: String?) {
    val pinyin: String = PinyinUtils.getInstance().convertWords(name)
}
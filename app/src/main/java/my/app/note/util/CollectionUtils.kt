package my.app.note.util

import my.app.note.database.NoteBean
import java.util.*

/**
 * Created by CCP on 2017.12.1 0001.
 *
 */
object CollectionUtils {

    // 按创建日期排序
    fun sortByCreateTime(list: MutableList<NoteBean>): MutableList<NoteBean> {
        Collections.reverse(list)
        return list
    }

    // 按更新日期排序
    fun sortByUpdateTime(list: MutableList<NoteBean>): MutableList<NoteBean> {
        Collections.sort(list, Comparator { o1, o2 ->
            return@Comparator DateUtils.compareTimestamp(o1.noteUpdateTime, o2.noteUpdateTime)
        })
        return list
    }

    // 合并数组
    fun mergeArray(arrayA: Array<String>, arrayB: Array<String>): Array<String?> {
        val arrayC = arrayOfNulls<String>(arrayA.size + arrayB.size)
        System.arraycopy(arrayA, 0, arrayC, 0, arrayA.size)
        System.arraycopy(arrayB, 0, arrayC, arrayA.size, arrayB.size)
        return arrayC
    }
}
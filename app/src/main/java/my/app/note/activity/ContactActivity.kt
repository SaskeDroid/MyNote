package my.app.note.activity

import android.content.Context
import android.os.Bundle
import android.util.Xml
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_contact.*
import my.app.note.R
import my.app.note.adapter.ContactAdapter
import my.app.note.view.SideBar
import my.app.note.bean.ContactItem
import org.xmlpull.v1.XmlPullParser
import java.util.*


/**
 * Created by CCP on 2018.4.12 0012.
 *
 */
class ContactActivity : BaseActivity(), SideBar.OnTouchLetterChangedListener, AdapterView.OnItemClickListener {

    private var list: ArrayList<ContactItem> = ArrayList()
    private var adapter: ContactAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        setToolbarTitle(R.string.widget_contact)

        sideBar.setTextView(tvDialog)
        sideBar.setOnTouchLetterChangedListener(this)

        initData()
    }

    private fun initData() {
        try {
            // 读取assets目录
            val am = createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY).assets
            val fls = am.open("contact.xml")

            // 获得PULL解析器对象
            val parser = Xml.newPullParser()
            // 指定解析的文件和编码格式
            parser.setInput(fls, "utf-8")
            // 获得事件类型
            var eventType = parser.eventType

            var id: String? = null
            var name: String? = null

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = parser.name // 获得当前节点的名称
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (tagName == "widget_contact") {
                            // 根节点
                        } else if (tagName == "item") {
                            id = parser.getAttributeValue(null, "id")
                        } else if (tagName == "name") {
                            name = parser.nextText()
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (tagName == "item") {
                            list.add(ContactItem(name))
                        }
                    }
                }
                eventType = parser.next() // 获得下一个事件类型
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 根据拼音对集合排序
        // Collections.sort(list) { lhs, rhs ->
        //     lhs.pinyin.compareTo(rhs.pinyin)
        // } ↓↓↓
        list.sortWith(Comparator { lhs, rhs ->
            lhs.pinyin.compareTo(rhs.pinyin)
        })

        adapter = ContactAdapter(this, list)
        listView.adapter = adapter
        listView.onItemClickListener = this
    }

    override fun onTouchingLetterChanged(currLetter: String) {
        for (i in 0 until list.size) {
            val firstWord = list[i].pinyin.substring(0, 1)
            // 当前手指按下的字母与列表中首字母相同的项找出来
            if (currLetter == firstWord.toUpperCase()) {
                listView.setSelection(i)
                return
            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (list[position].name) {
            "Native + H5 test" -> {
//                startActivity(Intent(this, HybridActivity::class.java))
            }
        }
    }
}
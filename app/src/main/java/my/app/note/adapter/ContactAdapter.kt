package my.app.note.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import my.app.note.R
import my.app.note.bean.ContactItem

/**
 * Created by CCP on 2018.4.12 0012.
 *
 */
class ContactAdapter(var context: Context, var list: List<ContactItem>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: Holder
        val view: View
        if (convertView == null) {
            holder = Holder()
            view = LayoutInflater.from(context).inflate(R.layout.item_demo_list, parent, false)
            holder.tvHeader = view.findViewById(R.id.tvHeader)
            holder.tvName = view.findViewById(R.id.tvName)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as Holder
        }

        val firstWord = list[position].pinyin.substring(0, 1)
        holder.tvHeader.text = firstWord
        holder.tvName.text = list[position].name

        // 相同字母开头的合并在一起
        if (position == 0) {
            // 第一个显示
            holder.tvHeader.visibility = View.VISIBLE
        } else {
            // 后一个与前一个对比，首字母相同则隐藏
            val headerWord = list[position - 1].pinyin.substring(0, 1)
            if (firstWord == headerWord) {
                holder.tvHeader.visibility = View.GONE
            } else {
                holder.tvHeader.visibility = View.VISIBLE
            }
        }

        return view
    }

    override fun getItem(position: Int): Any = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = list.size

    class Holder {
        lateinit var tvHeader: TextView
        lateinit var tvName: TextView
    }
}
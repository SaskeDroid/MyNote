package my.app.note.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import my.app.note.R
import my.app.note.bean.GridItem


/**
 * Created by CCP on 2018.1.22 0022.
 *
 */
class GridViewAdapter(private val context: Context, private val list: List<GridItem>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: Holder
        val view: View
        if (convertView == null) {
            holder = Holder()
            view = LayoutInflater.from(context).inflate(R.layout.item_grid_view, parent, false)
            holder.textView = view.findViewById(R.id.textView)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as Holder
        }

        val drawable = context.resources.getDrawable(list[position].resId)
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight) // 必须设置图片大小，否则不显示
        holder.textView.setCompoundDrawables(null, drawable, null, null)
        holder.textView.text = list[position].text

        return view
    }

    override fun getItem(position: Int): Any = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = list.size

    class Holder {
        lateinit var textView: TextView
    }
}
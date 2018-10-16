package my.app.note.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import my.app.note.R

/**
 * Created by CCP on 2018.1.17 0017.
 *
 */
@Deprecated("Never used")
class LabelListAdapter(private val context: Context, private val list: List<String>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: Holder
        val view: View
        if (convertView == null) {
            holder = Holder()
            view = LayoutInflater.from(context).inflate(R.layout.item_label_list, parent, false)
            holder.textView = view.findViewById(R.id.textView)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as Holder
        }

        holder.textView.text = list[position]

        return view
    }

    override fun getItem(position: Int): Any = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = list.size

    class Holder {
        lateinit var textView: TextView
    }
}
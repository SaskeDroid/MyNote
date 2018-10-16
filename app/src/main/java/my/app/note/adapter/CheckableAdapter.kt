package my.app.note.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckedTextView
import my.app.note.R

/**
 * Created by CCP on 2018.1.5 0005.
 *
 */
@Deprecated("Never used")
class CheckableAdapter(private val context: Context, private val list: List<String>) : BaseAdapter() {

    private var checkedCallback: CheckedCallback? = null

    override fun getCount(): Int = list.size

    override fun getItem(position: Int): Any = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: Holder
        val view: View
        if (convertView == null) {
            holder = Holder()
            view = LayoutInflater.from(context).inflate(R.layout.item_checkable, parent, false)
            holder.checkedTextView = view.findViewById(R.id.checkedTextView)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as Holder
        }

        holder.checkedTextView.text = list[position]
        holder.checkedTextView.setOnClickListener {
            holder.checkedTextView.toggle()
            checkedCallback!!.onItemClickListener(position, holder.checkedTextView.isChecked)
        }
        holder.checkedTextView.setOnLongClickListener {
            checkedCallback!!.onItemLongClickListener(position)
            return@setOnLongClickListener true
        }

        return view
    }

    override fun hasStableIds(): Boolean = true

    fun setCallback(checkedCallback: CheckedCallback) {
        this.checkedCallback = checkedCallback
    }

    class Holder {
        lateinit var checkedTextView: CheckedTextView
    }

    interface CheckedCallback {
        fun onItemClickListener(position: Int, checked: Boolean)
        fun onItemLongClickListener(position: Int)
    }
}

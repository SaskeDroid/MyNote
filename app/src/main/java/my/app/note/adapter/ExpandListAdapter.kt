package my.app.note.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView

import my.app.note.R

/**
 * Created by SNCC on 2017.4.19 0019.
 *
 */
@Deprecated("This adapter is replaced by simpler listView adapter")
class ExpandListAdapter(private val mContext: Context, private var listTitle: List<String>, private var listDesc: List<List<String>>) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int = listTitle.size

    override fun getChildrenCount(groupPosition: Int): Int = listDesc[groupPosition].size

    override fun getGroup(groupPosition: Int): Any = listTitle[groupPosition]

    override fun getChild(groupPosition: Int, childPosition: Int): Any = listDesc[groupPosition][childPosition]

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun hasStableIds(): Boolean = true

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        val holder: ViewHolder
        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_expand_item, parent, false)
            holder = ViewHolder()
            holder.tvTitle = view.findViewById(R.id.tvTitle)
            holder.tvItem = view.findViewById(R.id.tvItem)
            view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            view = convertView
        }
        holder.tvTitle!!.text = listTitle[groupPosition]
        holder.tvItem!!.visibility = View.GONE
        if (isExpanded) {
            holder.tvTitle!!.setCompoundDrawablesRelativeWithIntrinsicBounds(parent.resources.getDrawable(R.drawable.ic_menu_archive), null,
                    parent.resources.getDrawable(R.drawable.ic_arrow_down, null), null)
        } else {
            holder.tvTitle!!.setCompoundDrawablesRelativeWithIntrinsicBounds(parent.resources.getDrawable(R.drawable.ic_menu_archive), null,
                    parent.resources.getDrawable(R.drawable.ic_arrow_right, null), null)
        }
        return view
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        val holder: ViewHolder
        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_expand_item,  parent, false)
            holder = ViewHolder()
            holder.tvTitle = view.findViewById(R.id.tvTitle)
            holder.tvItem = view.findViewById(R.id.tvItem)
            view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            view = convertView
        }
        holder.tvTitle!!.visibility = View.GONE
        holder.tvItem!!.text = listDesc[groupPosition][childPosition]
        return view
    }

    internal inner class ViewHolder {
        var tvTitle: TextView? = null
        var tvItem: TextView? = null
    }
}

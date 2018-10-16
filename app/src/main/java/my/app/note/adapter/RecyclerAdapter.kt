package my.app.note.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import my.app.note.R
import my.app.note.constant.Constants
import my.app.note.database.NoteBean
import my.app.note.util.DateUtils

/**
 * Created by CCP on 2017.11.9 0009.
 *
 */

class RecyclerAdapter(private val context: Context, private val list: MutableList<NoteBean>) : RecyclerView.Adapter<RecyclerAdapter.Holder>() {

    private var recyclerViewCallback: RecyclerViewCallback? = null
    private var showRevert: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tvDate.text = DateUtils.getDateString(list[position].noteUpdateTime, Constants.FORMAT_YMDHME)

        if (!TextUtils.isEmpty(list[position].noteRemindTime)) {
            holder.tvDate.setCompoundDrawablesWithIntrinsicBounds(null, null, context.resources.getDrawable(R.drawable.ic_clock_alarm), null)
        } else {
            holder.tvDate.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }

        holder.tvContent.text = list[position].noteContent.split("\n")[0] // 取第一行
        holder.itemView.tag = position
        holder.tvRevert.visibility = if (showRevert) View.VISIBLE else View.GONE

        holder.tvDelete.setOnClickListener {
            recyclerViewCallback!!.onClickListener(holder.tvDelete, position)
        }
        holder.tvRevert.setOnClickListener {
            recyclerViewCallback!!.onClickListener(holder.tvRevert, position)
        }
        holder.lyItem.setOnClickListener {
            recyclerViewCallback!!.onItemClickListener(position)
        }
        holder.lyItem.setOnLongClickListener {
            recyclerViewCallback!!.onItemLongClickListener(position)
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int = list.size

    // 更新数据
    fun refreshData(list: MutableList<NoteBean>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    // 设置右边按钮是否显示
    fun showRevert(showRevert: Boolean) {
        this.showRevert = showRevert
    }

    fun setCallback(callback: RecyclerViewCallback) {
        recyclerViewCallback = callback
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDelete: TextView = itemView.findViewById(R.id.tvDelete)
        val tvRevert: TextView = itemView.findViewById(R.id.tvRevert)
        val lyItem: LinearLayout = itemView.findViewById(R.id.lyItem)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvContent: TextView = itemView.findViewById(R.id.tvContent)
    }

    interface RecyclerViewCallback {
        fun onItemClickListener(position: Int)
        fun onItemLongClickListener(position: Int)
        fun onClickListener(view: View, position: Int)
    }
}

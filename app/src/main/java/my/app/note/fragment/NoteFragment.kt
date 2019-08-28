package my.app.note.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.util.Log
import android.view.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.fragment_note.*
import my.app.note.R
import my.app.note.activity.NoteActivity
import my.app.note.adapter.RecyclerAdapter
import my.app.note.constant.Constants
import my.app.note.database.NoteBean
import my.app.note.util.CollectionUtils
import android.support.v7.app.AppCompatActivity
import my.app.note.activity.RecycleListActivity

/**
 * Created by CCP on 2018.1.25 0025.
 *
 */
class NoteFragment : BaseDBFragment(), RecyclerAdapter.RecyclerViewCallback, SearchView.OnQueryTextListener {

    private var rycList: MutableList<NoteBean> = ArrayList()
    private var rycAdapter: RecyclerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_note, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.title = getString(R.string.all_notes)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        rycAdapter = RecyclerAdapter(activity, CollectionUtils.sortByUpdateTime(rycList))
        rycAdapter!!.setCallback(this)
        recyclerView.layoutManager = LinearLayoutManager(activity) // 必须
        recyclerView.adapter = rycAdapter

        fab.setOnClickListener { goToNoteActivity(-1) }
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_options, menu)
        val menuSearch = menu!!.findItem(R.id.menuSearch)
        val searchView = MenuItemCompat.getActionView(menuSearch) as SearchView
        searchView.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menuRecycle -> {
                startActivity(Intent(activity, RecycleListActivity::class.java))
            }
        }
        return false
    }

    override fun onItemClickListener(position: Int) = goToNoteActivity(rycList[position].noteId)

    override fun onItemLongClickListener(position: Int) = showNoteDetail(rycList[position])

    override fun onClickListener(view: View, position: Int) {
        if (view.id == R.id.tvDelete) {
            val noteId = rycList[position].noteId
            noteDBManager!!.recycle(noteId)
            rycList.removeAt(position)
            rycAdapter!!.notifyItemRemoved(position)
            rycAdapter!!.notifyItemRangeChanged(position, rycList.size - position)
            Snackbar.make(view, getString(R.string.note_has_been_deleted), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo)) {
                        noteDBManager!!.revert(noteId)
                        refreshData()
                    }
                    .setActionTextColor(Color.GREEN)
                    .show()
        }
    }

    // 跳转到笔记页面，id：-1新建，>0编辑
    private fun goToNoteActivity(id: Int) {
        Log.i("CCP", "笔记ID-->$id")
        val intent = Intent(activity, NoteActivity::class.java)
        intent.putExtra(Constants.INTENT_NOTE_ID, id)
        startActivity(intent)
    }

    // 更新笔记列表
    private fun refreshData() {
        val newList: MutableList<NoteBean> = noteDBManager!!.selectNoteList()
        rycAdapter!!.refreshData(CollectionUtils.sortByUpdateTime(newList))
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val list: MutableList<NoteBean> = if (!TextUtils.isEmpty(newText)) {
            search(newText!!)
        } else {
            noteDBManager!!.selectNoteList()
        }
        rycAdapter!!.refreshData(list)
        return false
    }

    // 搜索功能
    private fun search(keywords: String): MutableList<NoteBean> {
        val oldList = noteDBManager!!.selectNoteList()
        val newList: MutableList<NoteBean> = ArrayList()
        oldList.filterTo(newList) { it.noteContent.contains(keywords, true) }
        return newList
    }
}
package my.app.note.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import kotlinx.android.synthetic.main.fragment_note.*

import my.app.note.R
import my.app.note.adapter.RecyclerAdapter
import my.app.note.constant.Constants
import my.app.note.database.NoteBean
import my.app.note.util.DialogUtils
import java.util.*

class RecycleListActivity : BaseDBActivity(), RecyclerAdapter.RecyclerViewCallback {

    private var rycList: MutableList<NoteBean> = ArrayList()
    private var rycAdapter: RecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_note)
        setToolbarTitle(R.string.recycle)
        fab.visibility = View.GONE
        initData()
    }

    private fun initData() {
        rycList = noteDBManager!!.selectRecycleList()
        rycAdapter = RecyclerAdapter(this, rycList)
        rycAdapter?.setCallback(this)
        rycAdapter?.showRevert(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = rycAdapter
    }

    override fun onItemClickListener(position: Int) = goToNoteActivity(rycList[position])

    override fun onItemLongClickListener(position: Int) {}

    override fun onClickListener(view: View, position: Int) {
        when (view.id) {
            R.id.tvDelete -> {
                DialogUtils.getMessageDialog(this, getString(R.string.tips), getString(R.string.whether_to_remove_this_note),
                        DialogInterface.OnClickListener { _, _ ->
                            noteDBManager!!.delete(rycList[position].noteId)
                            refreshData()
                        }).show()
            }
            R.id.tvRevert -> {
                noteDBManager!!.revert(rycList[position].noteId)
                refreshData()
                Snackbar.make(view, getString(R.string.note_has_been_restored), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    // 跳转到笔记页面
    private fun goToNoteActivity(noteBean: NoteBean) {
        val intent = Intent(this, RecycleNoteActivity::class.java)
        intent.putExtra(Constants.INTENT_NOTE_BEAN, noteBean)
        startActivity(intent)
    }

    // 更新笔记列表
    private fun refreshData() {
        val newList: MutableList<NoteBean> = noteDBManager!!.selectRecycleList()
        rycAdapter?.refreshData(newList)
    }
}

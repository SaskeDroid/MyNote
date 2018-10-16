package my.app.note.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import my.app.note.R
import my.app.note.constant.Constants
import my.app.note.database.NoteBean
import my.app.note.database.NoteDBManager
import my.app.note.util.DateUtils

/**
 * Created by CCP on 2017.12.11 0011.
 *
 */
class NoteRemoteViewService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return NoteViewFactory(applicationContext, intent)
    }

    class NoteViewFactory(var context: Context, var intent: Intent?) : RemoteViewsService.RemoteViewsFactory {

        var list: MutableList<NoteBean> = ArrayList()
        var noteDBManager: NoteDBManager? = null

        override fun onCreate() {
            noteDBManager = NoteDBManager.getInstance(context)
            list = noteDBManager!!.selectRemindList()
        }

        override fun getLoadingView(): RemoteViews? = null

        override fun getItemId(position: Int): Long = position.toLong()

        override fun onDataSetChanged() {
            list = noteDBManager!!.selectRemindList()
        }

        override fun hasStableIds(): Boolean = true

        override fun getViewAt(position: Int): RemoteViews {
            val remoteView = RemoteViews(context.packageName, R.layout.item_widget)
            remoteView.setTextViewText(R.id.appwidget_content, list[position].noteContent.replace("\n", " "))
            remoteView.setTextViewText(R.id.appwidget_date, DateUtils.getDateString(list[position].noteRemindTime, Constants.FORMAT_MDHM))

            // ListView onItemClick, send to NoteWidgetProvider onReceive
            val intent = Intent(Constants.ACTION_LIST_ITEM_CLICK)
            intent.putExtra(Constants.INTENT_NOTE_ID, list[position].noteId)
            remoteView.setOnClickFillInIntent(R.id.appwidget_content, intent)

            return remoteView
        }

        override fun getCount(): Int = list.size

        override fun getViewTypeCount(): Int = 1

        override fun onDestroy() = list.clear()
    }
}
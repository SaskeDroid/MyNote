package my.app.note.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews

import my.app.note.R
import my.app.note.activity.HomeActivity
import my.app.note.constant.Constants
import my.app.note.util.DateUtils

/**
 * Implementation of App Widget functionality.
 */
class NoteWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val componentName = ComponentName(context!!, NoteWidgetProvider::class.java)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
        when (intent!!.action) {
            Constants.ACTION_LIST_ITEM_CLICK -> {
                Log.i("CCP", "Click Note id is No.${intent.getIntExtra(Constants.INTENT_NOTE_ID, -1)}")
//                for (appWidgetId in appWidgetIds) {
//                    updateAppWidget(context, appWidgetManager, appWidgetId)
//                }
            }
            Constants.ACTION_LIST_UPDATE -> {
                Log.i("CCP", "Update Note id is No.${intent.getIntExtra(Constants.INTENT_NOTE_ID, -1)}")
                // 此方法会调用NoteViewFactory的onDataSetChanged()
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_list)
            }
        }
    }

    companion object {

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            // 创建RemoteView
            val remoteView = RemoteViews(context.packageName, R.layout.note_widget)
            remoteView.setTextViewText(R.id.appwidget_text, DateUtils.getCurrentDate(Constants.FORMAT_MDE))

            // 设置点击日期的PendingIntent
            val paIntent = PendingIntent.getActivity(context, 0, Intent(context, HomeActivity::class.java), 0)
            remoteView.setOnClickPendingIntent(R.id.appwidget_text, paIntent)

            // 设置RemoteViewsService
            val rIntent = Intent(context, NoteRemoteViewService::class.java)
            rIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            remoteView.setRemoteAdapter(R.id.appwidget_list, rIntent)
            remoteView.setEmptyView(R.id.appwidget_list, android.R.id.empty)

            // 设置响应ListView的PendingIntent
            val aIntent = Intent(context, NoteWidgetProvider::class.java)
            aIntent.action = Constants.ACTION_LIST_ITEM_CLICK
            val pbIntent = PendingIntent.getBroadcast(context, 1, aIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            remoteView.setPendingIntentTemplate(R.id.appwidget_list, pbIntent)

            // 更新Widget
            appWidgetManager.updateAppWidget(appWidgetId, remoteView)
        }
    }
}


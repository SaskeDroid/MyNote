package my.app.note.fragment

import android.content.Intent
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceScreen
import android.view.View
import android.widget.ListView
import my.app.note.R
import my.app.note.activity.AboutActivity
import my.app.note.activity.BackupActivity
import my.app.note.util.ClearCacheUtils

/**
 * Created by CCP on 2018.1.25 0025.
 *
 */
class SettingFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference_setting)
    }

    override fun onResume() {
        super.onResume()
        getCacheSize()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listView = view!!.findViewById<View>(android.R.id.list) as ListView
        listView.divider = null // 移除ListView的分割线
    }

    override fun onPreferenceTreeClick(preferenceScreen: PreferenceScreen?, preference: Preference?): Boolean {
        when (preference!!.key) {
            getString(R.string.backup) -> {
                startActivity(Intent(activity, BackupActivity::class.java))
            }
            getString(R.string.clear_cache) -> {
                if (ClearCacheUtils.getCacheSize2Double(activity) > 1024) {
                    ClearCacheUtils.clearAllCache(activity)
                    getCacheSize()
                }
            }
            getString(R.string.about) -> {
                startActivity(Intent(activity, AboutActivity::class.java))
            }
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference)
    }

    private fun getCacheSize() {
        findPreference(getString(R.string.clear_cache)).summary = String.format(getString(R.string.clear_cache_summary), ClearCacheUtils.getCacheSize2String(activity))
    }
}
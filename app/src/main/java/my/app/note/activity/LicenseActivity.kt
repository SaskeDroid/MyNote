package my.app.note.activity

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceScreen
import android.text.TextUtils
import my.app.note.R
import my.app.note.util.DialogUtils

/**
 * Created by CCP on 2018.1.10 0010.
 *
 */
class LicenseActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference)
        setToolbarTitle(R.string.special_thanks)

        fragmentManager.beginTransaction().replace(R.id.lyContent, LicenseFragment()).commit()
    }

    class LicenseFragment : PreferenceFragment() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.preference_license)
        }

//        override fun onPreferenceTreeClick(preferenceScreen: PreferenceScreen?, preference: Preference?): Boolean {
//            if (!TextUtils.isEmpty(preference!!.key)) {
//                DialogUtils.getMessageDialog(activity, getString(R.string.visit_website), preference.key.toString(), DialogInterface.OnClickListener { _, which ->
//                    if (which == DialogInterface.BUTTON_POSITIVE) {
//                        val uri = Uri.parse(preference.key)
//                        val intent = Intent(Intent.ACTION_VIEW, uri)
//                        startActivity(intent)
//                    }
//                }).show()
//            }
//            return super.onPreferenceTreeClick(preferenceScreen, preference)
//        }
    }
}
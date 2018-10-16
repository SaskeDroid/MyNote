package my.app.note.activity

import android.app.Fragment
import android.app.FragmentTransaction
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*
import my.app.note.R
import my.app.note.fragment.NoteFragment
import my.app.note.fragment.SettingFragment
import my.app.note.fragment.WidgetFragment

class HomeActivity : BaseActivity() {

    private var currentFragment: Fragment? = null
    private var noteFragment: NoteFragment = NoteFragment()
    private var widgetFragment: WidgetFragment = WidgetFragment()
    private var settingFragment: SettingFragment = SettingFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSwipeBackEnable(false)
        switchFragment(noteFragment)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_note -> {
                switchFragment(noteFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_widget -> {
                switchFragment(widgetFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_setting -> {
                switchFragment(settingFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun switchFragment(fragment: Fragment) {
        if (currentFragment == fragment) {
            return
        }
        val transaction = fragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        if (!fragment.isAdded) {
            if (currentFragment != null) {
                transaction.hide(currentFragment)
            }
            transaction.add(R.id.content, fragment, fragment::class.java.name)
        } else {
            transaction.hide(currentFragment).show(fragment)
        }
        currentFragment = fragment
        transaction.commit()
    }
}

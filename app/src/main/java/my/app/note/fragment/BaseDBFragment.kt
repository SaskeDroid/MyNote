package my.app.note.fragment

import android.os.Bundle
import my.app.note.database.NoteDBManager

/**
 * Created by CCP on 2018.3.29 0029.
 *
 */
open class BaseDBFragment : BaseFragment() {

    var noteDBManager: NoteDBManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteDBManager = NoteDBManager.getInstance(activity)
    }
}
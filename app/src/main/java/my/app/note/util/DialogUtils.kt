package my.app.note.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.SpannableString
import android.text.TextUtils
import android.view.View

/**
 * Created by CCP on 2017.11.7.
 *
 */
object DialogUtils {

    private fun getDialog(context: Context): AlertDialog.Builder = AlertDialog.Builder(context)

    fun getMessageDialog(context: Context, title: String?, message: String): AlertDialog.Builder {
        val builder = getDialog(context)
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title)
        }
        builder.setMessage(SpannableString(message))
        builder.setPositiveButton(android.R.string.ok, null)
        return builder
    }

    fun getMessageDialog(context: Context, title: String?, message: String, listener: DialogInterface.OnClickListener): AlertDialog.Builder {
        val builder = getDialog(context)
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title)
        }
        builder.setMessage(SpannableString(message))
        builder.setPositiveButton(android.R.string.ok, listener)
        builder.setNegativeButton(android.R.string.cancel, null)
        return builder
    }

    fun getMessageDialog(context: Context, title: String?, message: String, negative: String, positive: String, listener: DialogInterface.OnClickListener): AlertDialog.Builder {
        val builder = getDialog(context)
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title)
        }
        builder.setMessage(SpannableString(message))
        builder.setPositiveButton(positive, listener)
        builder.setNegativeButton(negative, listener)
        return builder
    }

    fun getCustomDialog(context: Context, title: String?, view: View): AlertDialog.Builder {
        val builder = getDialog(context)
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title)
        }
        builder.setView(view)
        builder.setPositiveButton(android.R.string.ok, null)
        return builder
    }

    fun getCustomDialog(context: Context, title: String?, view: View, listener: DialogInterface.OnClickListener): AlertDialog.Builder {
        val builder = getDialog(context)
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title)
        }
        builder.setView(view)
        builder.setPositiveButton(android.R.string.ok, listener)
        return builder
    }

    @JvmOverloads
    fun getSingleChoiceDialog(context: Context, title: String?, items: Array<String?>, listener: DialogInterface.OnClickListener, checkedItem: Int = 0): AlertDialog.Builder {
        val builder = getDialog(context)
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title)
        }
        builder.setSingleChoiceItems(items, checkedItem, listener)
        builder.setPositiveButton(android.R.string.ok, listener)
        return builder
    }

    fun getSelectDialog(context: Context, title: String?, items: Array<String?>, listener: DialogInterface.OnClickListener): AlertDialog.Builder {
        val builder = getDialog(context)
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title)
        }
        builder.setItems(items, listener)
        return builder
    }
}
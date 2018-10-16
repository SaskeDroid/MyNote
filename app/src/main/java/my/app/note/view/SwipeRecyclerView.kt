package my.app.note.view

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Added by CCP on 2017.11.27.
 */

class SwipeRecyclerView : RecyclerView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev!!.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                val openItem = findOpenItem()
                if (openItem != null && openItem != getTouchItem(ev.x.toInt(), ev.y.toInt())) {
                    val swipeItemLayout = findSwipeItemLayout(openItem)
                    if (swipeItemLayout != null) {
                        swipeItemLayout.close()
                        return super.dispatchTouchEvent(ev)
                    }
                }
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                return false // 禁止多点触控
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    // 获取打开的Item
    private fun findOpenItem(): View? {
        // 0 until N 区间是 [0, N)， 0..N 区间是 [0, N]
        for (i in 0 until childCount) {
            val swipeItemLayout = findSwipeItemLayout(getChildAt(i))
            if (swipeItemLayout != null && swipeItemLayout.isOpen) {
                return getChildAt(i)
            }
        }
        return null
    }

    // 获取按下位置的Item
    private fun getTouchItem(x: Int, y: Int): View? {
        val frame = Rect()
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility == View.VISIBLE) {
                child.getHitRect(frame)
                if (frame.contains(x, y)) {
                    return child
                }
            }
        }
        return null
    }

    // 获取SwipeItemLayout
    private fun findSwipeItemLayout(view: View): SwipeItemLayout? {
        if (view is SwipeItemLayout) {
            return view
        } else if (view is ViewGroup) {
            (0 until childCount)
                    .mapNotNull { findSwipeItemLayout(view.getChildAt(it)) }
                    .forEach { return it }
        }
        return null
    }
}
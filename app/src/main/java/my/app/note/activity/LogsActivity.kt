package my.app.note.activity

import android.graphics.Color
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_logs.*
import my.app.note.R
import my.app.note.bean.LikeCount
import my.app.note.cache.ACache
import my.app.note.constant.Constants
import my.app.note.util.DateUtils
import my.app.note.util.FileUtils
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by CCP on 2017.11.29 0029.
 *
 */
class LogsActivity : BaseActivity() {

    private val random: Random = Random()
    private var mACache: ACache? = null
    private var count = 0
    private var currDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logs)
        setToolbarTitle(R.string.update_log)
        window.statusBarColor = Color.TRANSPARENT

        mACache = ACache.get(this)
        currDate = DateUtils.getCurrentDate(Constants.FORMAT_YMD)

        tvMessage.movementMethod = ScrollingMovementMethod.getInstance() // 可滚动
        tvMessage.setLineSpacing(1.0F, 1.2F)
        tvMessage.text = FileUtils.getLogMessage(this)
//        tvMessage.setOnLongClickListener {
//            mACache?.clear()
//            showToast("清除数据")
//            return@setOnLongClickListener true
//        }

        fab.setOnClickListener { _ ->
            // 随机颜色 [0, 256)
            val color =  Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256))
            heartLayout.addHeart(color, R.drawable.ic_like, R.drawable.ic_like)
            count++
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        saveLikeCount()
    }

    private fun getLikeCount() {
        Log.i("CCP", "点赞数-->$currDate : $count")
    }

    // To be fix bugs
    private fun saveLikeCount() {
        if (count > 0) {
            val gson = Gson()
            val cacheData = mACache?.getAsString(Constants.KEY_LIKE_COUNT)
            var oldList = ArrayList<LikeCount>()
            if (cacheData != null) {
                Log.i("CCP", "缓存的数据-->$cacheData")
                oldList = gson.fromJson(cacheData, object : TypeToken<List<LikeCount>>() {}.type)
            }
            val likeCount = LikeCount(currDate, count)
            oldList.add(likeCount)
            val newList = ArrayList<LikeCount>()
            for (bean in oldList) {
                if (bean.timestamp == currDate) {
                    newList.add(LikeCount(currDate, bean.count + count))
                } else {
                    newList.add(bean)
                }
            }
            val saveData = gson.toJson(newList)
            Log.i("CCP", "保存的数据-->$saveData")
            mACache?.put(Constants.KEY_LIKE_COUNT, saveData)
        }
    }
}
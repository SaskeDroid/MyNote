package my.app.note.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import com.youth.banner.BannerConfig
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.fragment_widget.*
import my.app.note.R
import my.app.note.activity.CompassActivity
import my.app.note.activity.ContactActivity
import my.app.note.activity.demo.HybridActivity
import my.app.note.adapter.GridViewAdapter
import my.app.note.bean.GridItem
import my.app.note.util.AndroidUtils


/**
 * Created by CCP on 2018.1.25 0025.
 *
 */
class WidgetFragment : BaseFragment(), AdapterView.OnItemClickListener {

    private var adapter: GridViewAdapter? = null
    private var list: List<GridItem> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_widget, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        list = getGridViewData(R.array.ary_widget_icons, R.array.ary_widget_titles)
        adapter = GridViewAdapter(activity, list)
        gridView.adapter = adapter
        gridView.onItemClickListener = this

        initBanner()
    }

    //初始化GridView数据
    private fun getGridViewData(icons: Int, titles: Int): List<GridItem> {
        val list = ArrayList<GridItem>()
        val aryTitles = resources.getStringArray(titles)
        val aryIcons = resources.obtainTypedArray(icons)
        // for (i in aryTitles.indices) {
        //   val item = GridItem(i, aryIcons.getResourceId(i, 0), aryTitles[i])
        //   list.add(item)
        // } ↓↓↓
        aryTitles.indices.mapTo(list) { GridItem(it, aryIcons.getResourceId(it, 0), aryTitles[it]) }
        aryIcons.recycle()
        return list
    }

    // 广告位 ←_←
    private fun initBanner() {
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR) // 显示圆形指示器
        banner.setIndicatorGravity(BannerConfig.CENTER) // 指示器居中
        banner.setDelayTime(4500) // 间隔时间
        val images = ArrayList<Int>()
        images.add(R.drawable.banner1)
        images.add(R.drawable.banner2)
        images.add(R.drawable.banner3)
        banner.setImages(images) // 设置轮播图片
        banner.setImageLoader(object : ImageLoader() {
            override fun displayImage(context: Context, path: Any, imageView: ImageView) {
                imageView.setImageResource(path as Int)
            }
        })
        banner.setOnBannerListener { position ->
            when (position) {
                0 -> AndroidUtils.openAppMarket(activity, "com.smartisan.notes")    // 锤子便签
                1 -> AndroidUtils.openAppMarket(activity, "org.dayup.gnotes")       // 随笔记
                2 -> AndroidUtils.openAppMarket(activity, "cn.ticktick.task")       // 滴答清单
            }
        }
        banner.start()
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (list[position].text) {
            getString(R.string.widget_compass) -> {
                startActivity(Intent(activity, CompassActivity::class.java))
            }
            getString(R.string.widget_web) -> {
                startActivity(Intent(activity, HybridActivity::class.java))
            }
            getString(R.string.widget_contact) -> {
                startActivity(Intent(activity, ContactActivity::class.java))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        banner.startAutoPlay()
    }

    override fun onStop() {
        super.onStop()
        banner.stopAutoPlay()
    }
}
package my.app.note.activity.demo

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.JavascriptInterface
import kotlinx.android.synthetic.main.activity_hybrid.*
import my.app.note.R
import android.webkit.WebSettings
import android.webkit.WebViewClient
import my.app.note.activity.BaseActivity


/**
 * Created by CCP on 2018.4.4 0004.
 *
 */
class HybridActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hybrid)
        setToolbarTitle(R.string.web_test)
        initWebView()

        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
    }

    private fun initWebView() {
        webView.webViewClient = WebViewClient() // 避免跳转到系统浏览器
        val settings = webView.settings
        settings.javaScriptEnabled = true // 开启JS支持
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN // 设置WebView单列显示，不允许横向移动
        webView.loadUrl("file:///android_asset/hybrid.html")
        webView.addJavascriptInterface(this, "native") // 该方法用于让h5调用android方法
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn1 -> {
                webView.loadUrl("javascript:message()") // 参数 "javascript:" + js中的函数名
            }
            R.id.btn2 -> {
                val name = "from activity"
                webView.loadUrl("javascript:message2('$name')") // 有参函数参数要加单引号
            }
        }
    }

    @JavascriptInterface
    fun setMessage() {
        runOnUiThread { content.text = "调用了Native方法" }
    }

    @JavascriptInterface
    fun setMessage(name: String) {
        runOnUiThread { content.text = "调用了Native方法：$name" }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
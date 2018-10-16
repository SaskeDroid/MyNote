package my.app.note.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_about.*
import my.app.note.BuildConfig
import my.app.note.R
import my.app.note.util.AndroidUtils
import my.app.note.util.DialogUtils
import java.util.*
import tyrantgit.explosionfield.ExplosionField


class AboutActivity : BaseActivity() {

    private var lastTime: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setToolbarTitle(R.string.about)

        tvVersion.text = StringBuilder().append("V ${AndroidUtils.getVersionName(this)}")
                .append(" (${AndroidUtils.getVersionCode(this)}.${BuildConfig.BUILD_TIME.replace("-", "").substring(2)}) ")
        tvCopyright.text = String.format(getString(R.string.copyright), Calendar.getInstance().get(Calendar.YEAR), BuildConfig.BUILD_HOST)

        // 爆炸效果
        val explosionField = ExplosionField.attach2Window(this)
        ivLogo.setOnLongClickListener { v ->
            explosionField?.explode(v)
            ivLogo.isEnabled = false
            return@setOnLongClickListener true
        }
    }

    // 更新日志
    fun showUpdateLog(view: View) {
        if (System.currentTimeMillis() - lastTime > 500L) {
            lastTime = System.currentTimeMillis()
            return
        }
        startActivity(Intent(this, LogsActivity::class.java))
    }

    // 使用帮助
    fun showUseHelp(view: View) {
        DialogUtils.getMessageDialog(this, "(ง •_•)ง", getString(R.string.help_tips)).show()
    }

    // 开源许可
    fun showSpecialThanks(view: View) {
        startActivity(Intent(this, LicenseActivity::class.java))
    }
}

package my.app.note.config

import android.content.Context
import android.net.Uri
import com.jph.takephoto.model.CropOptions
import com.jph.takephoto.app.TakePhoto
import com.jph.takephoto.compress.CompressConfig
import com.jph.takephoto.model.TakePhotoOptions
import java.io.File

/**
 * Created by CCP on 2018.4.20 0020.
 *
 */
object TakePhotoConfig {

    // 打开相机
    fun openCamera(context: Context, takePhoto: TakePhoto) {
        init(takePhoto)
        takePhoto.onPickFromCaptureWithCrop(getOutputUri(context), getCropConfigs())
    }

    // 打开相册
    fun opeGallery(context: Context, takePhoto: TakePhoto) {
        init(takePhoto)
        takePhoto.onPickFromGalleryWithCrop(getOutputUri(context), getCropConfigs())
    }

    private fun init(takePhoto: TakePhoto) {
        val builder = TakePhotoOptions.Builder()
        builder.setWithOwnGallery(false) // 是否使用TakePhoto自带的相册界面
        builder.setCorrectImage(true)
        takePhoto.setTakePhotoOptions(builder.create())

        // 压缩配置
        val config = CompressConfig.Builder()
                .setMaxSize(1024 * 100)
                .setMaxPixel(1080)
                .enableReserveRaw(true)
                .create()
        takePhoto.onEnableCompress(config, false)
    }

    // 获取保存后的文件Uri
    private fun getOutputUri(context: Context): Uri {
        val file = File(context.externalCacheDir, "p" + System.currentTimeMillis() + ".jpg")
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        return Uri.fromFile(file)
    }

    // 裁剪配置
    private fun getCropConfigs(): CropOptions {
        val cropBuilder = CropOptions.Builder()
        cropBuilder.setWithOwnCrop(false) // 是否使用TakePhoto自带的裁剪工具
        return cropBuilder.create()
    }
}
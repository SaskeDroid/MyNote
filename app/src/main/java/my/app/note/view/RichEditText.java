package my.app.note.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import my.app.note.R;

public class RichEditText extends AppCompatEditText {

    private static final String TAG = "RichEditText";
    private Context mContext;
    private Editable mEditable;

    public RichEditText(Context context) {
        super(context);
        this.mContext = context;
    }

    public RichEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public RichEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    /**
     * 插入一张图片
     *
     * @param bitmap
     * @param filePath
     */
    public void addImage(Bitmap bitmap, String filePath) {
        Log.i(TAG, filePath);
        String pathTag = "<img src=\"" + filePath + "\"/>";
        SpannableString spanString = new SpannableString(pathTag);
        // 获取屏幕的宽高
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        // 图片宽高
        int bmWidth = bitmap.getWidth();
        int bmHeight = bitmap.getHeight();
        //
        int zoomWidth = getWidth() - (paddingLeft + paddingRight);
        int zoomHeight = (int) (((float) zoomWidth / (float) bmWidth) * bmHeight);

        Bitmap newBitmap = zoomImage(bitmap, zoomWidth, zoomHeight);
        ImageSpan imgSpan = new ImageSpan(mContext, bitmap);
        spanString.setSpan(imgSpan, 0, pathTag.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (mEditable == null) {
            mEditable = getText(); // 获取EditText内容
        }
        int start = getSelectionStart(); // 设置欲添加的位置
        start = start > mEditable.length() ? mEditable.length() : start; // ensure not to get IndexOutOfBoundsException;
        mEditable.insert(start, spanString); // 设置spanString要添加的位置
        setText(mEditable);
        setSelection(start, spanString.length());
    }

    /**
     * 指定位置插入图片
     *
     * @param bitmap
     * @param filePath
     * @param start
     * @param end
     */
    public void addImage(Bitmap bitmap, String filePath, int start, int end) {
        Log.i(TAG, filePath);
        String pathTag = "<img src=\"" + filePath + "\"/>";
        SpannableString spanString = new SpannableString(pathTag);
        // 获取屏幕的宽高
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        // 图片宽高
        int bmWidth = bitmap.getWidth();
        int bmHeight = bitmap.getHeight();
        //
        int zoomWidth = getWidth() - (paddingLeft + paddingRight);
        int zoomHeight = (int) (((float) zoomWidth / (float) bmWidth) * bmHeight);
        Bitmap newBitmap = zoomImage(bitmap, zoomWidth, zoomHeight);
        ImageSpan imgSpan = new ImageSpan(mContext, bitmap);
        spanString.setSpan(imgSpan, 0, pathTag.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        Editable editable = getText(); // 获取EditText内容
        editable.delete(start, end); // 删除
        editable.insert(start, spanString); // 设置spanString要添加的位置
    }

    /**
     * 插入默认图片
     *
     * @param filePath
     * @param start
     * @param end
     */
    public void addDefaultImage(String filePath, int start, int end) {
        Log.i(TAG, filePath);
        String pathTag = "<img src=\"" + filePath + "\"/>";
        SpannableString spanString = new SpannableString(pathTag);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.broken_image);
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int bmWidth = bitmap.getWidth();
        int bmHeight = bitmap.getHeight();
        int zoomWidth = getWidth() - (paddingLeft + paddingRight);
        int zoomHeight = (int) (((float) zoomWidth / (float) bmWidth) * bmHeight);
        Bitmap newBitmap = zoomImage(bitmap, zoomWidth, zoomHeight);
        ImageSpan imgSpan = new ImageSpan(mContext, newBitmap);
        spanString.setSpan(imgSpan, 0, pathTag.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (mEditable == null) {
            mEditable = getText();
        }
        mEditable.delete(start, end);
        mEditable.insert(start, spanString);
    }

    /**
     * 对图片进行缩放
     *
     * @param bitmap
     * @param zoomWidth
     * @param zoomHeight
     * @return Bitmap
     */
    private Bitmap zoomImage(Bitmap bitmap, double zoomWidth, double zoomHeight) {
        // 获取这个图片的宽和高
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        // 如果宽度为0保持原图
        if (zoomWidth == 0) {
            zoomWidth = width;
            zoomHeight = height;
        }
        // 创建操作图片用的Matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) zoomWidth) / width;
        float scaleHeight = ((float) zoomHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, (int) width, (int) height, matrix, true);
    }

    public String getRichText() {
        return getText().toString();
    }

    public void setRichText(String content) {
        setText("");
        mEditable = getText();
        mEditable.append(content);
        // 遍历查找
        String str = "<img src=\"([/\\w\\W/\\/.]*)\"\\s*/>";
        Pattern pattern = Pattern.compile(str);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            final String filePath = matcher.group(1);
            // 需要替换的文本，即<img>path</img>
            String mString = matcher.group();
            final int startIndex = content.indexOf(mString);
            final int endIndex = startIndex + mString.length();
            initImageLoader();
            ImageLoader.getInstance().loadImage("file://" + filePath, getDisplayImageOptions(), new ImageLoadingListener() {

                @Override
                public void onLoadingStarted(String uri, View arg1) {
                    // TODO Auto-generated method stub
//                    addDefaultImage(filePath, startIndex, endIndex); // 插入默认图片
                }

                @Override
                public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onLoadingComplete(String uri, View arg1, Bitmap bitmap) {
                    // TODO Auto-generated method stub
                    addImage(bitmap, uri.replace("file://", ""), startIndex, endIndex);
                }

                @Override
                public void onLoadingCancelled(String arg0, View arg1) {
                    // TODO Auto-generated method stub
                }
            });
        }
        setText(mEditable);
    }

    private DisplayImageOptions getDisplayImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.broken_image)
                .showImageForEmptyUri(R.drawable.broken_image)
                .showImageOnFail(R.drawable.broken_image)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true).build();
        return options;
    }

    /**
     * This configuration tuning is custom. You can tune every option, you may tune some of them,
     * or you can create default configuration by ImageLoaderConfiguration.createDefault(this);
     * */
    public void initImageLoader() {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(mContext);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50MB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        ImageLoader.getInstance().init(config.build());
    }
}

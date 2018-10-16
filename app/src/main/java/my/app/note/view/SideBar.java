package my.app.note.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import my.app.note.R;

/**
 * Added by CCP on 2018.4.12 0012.
 *
 */

public class SideBar extends View {

    // 默认字体大小
    private static final float DEFAULT_TEXT_SIZE = 13F;

    // 默认字体颜色
    private static final int DEFAULT_TEXT_COLOR = Color.DKGRAY;

    // 选中字体颜色
    private static final int CHOOSE_TEXT_COLOR = Color.BLACK;

    // 默认背景颜色
    private static final int DEFAULT_BACKGROUND = Color.TRANSPARENT;

    // 选中背景颜色
    private static final int CHOOSE_BACKGROUND = Color.LTGRAY;

    // 字母表
    private static final String[] ALPHABET = {"*", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};

    // 当前选中索引
    private int currChoose = -1;

    // 提示当前选中字母的控件
    private TextView mTextDialog;

    // 画笔
    private Paint paint = new Paint();

    // 字母变化监听事件
    private OnTouchLetterChangedListener listener;

    public SideBar(Context context) {
        super(context);
    }

    public SideBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnTouchLetterChangedListener(OnTouchLetterChangedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 获取SideBar宽高
        int width = getWidth();
        int height = getHeight();

        // 设置每行字母高度
        int eachHeight = height / ALPHABET.length;

        for (int i = 0; i < ALPHABET.length; i++) {
            // 计算每个字母y坐标
            float xPos = width / 2 - paint.measureText(ALPHABET[i]) / 2;
            // 计算每个字母y坐标
            float yPos = eachHeight * i + eachHeight;

            paint.setColor(DEFAULT_TEXT_COLOR);
            paint.setAntiAlias(true);
            paint.setTextSize(sp2px(DEFAULT_TEXT_SIZE));

            if (i == currChoose) {
                paint.setColor(CHOOSE_TEXT_COLOR);
                paint.setFakeBoldText(true);
            }
            // 绘制文本
            canvas.drawText(ALPHABET[i], xPos, yPos, paint);
            // 重置画笔
            paint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float yPos = event.getY();
        int oldChoose = currChoose;

        // 点击y坐标所占总高度的比例*数组长度等于点击字母表中的个数
        int cc = (int) (yPos / getHeight() * ALPHABET.length);

        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundColor(DEFAULT_BACKGROUND);
                currChoose = -1;
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;

            default:
                setBackgroundColor(CHOOSE_BACKGROUND);
                if (oldChoose != cc) {
                    if (cc >= 0 && cc < ALPHABET.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(ALPHABET[cc]);
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(ALPHABET[cc]);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }
                        currChoose = cc;
                        invalidate();
                    }
                }
                break;
        }
        return true; // return true
    }

    // 绑定中间显示字母的TextView控件
    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    // sp转px
    public float sp2px(float spValue) {
        float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return spValue * fontScale + 0.5f;
    }

    // 接口
    public interface OnTouchLetterChangedListener {
        void onTouchingLetterChanged(String currLetter);
    }
}

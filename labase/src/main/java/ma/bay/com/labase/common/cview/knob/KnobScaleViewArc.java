package ma.bay.com.labase.common.cview.knob;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import ma.bay.com.labase.R;


/**
 * 旋钮弧线
 */
public class KnobScaleViewArc extends View {

    PaintFlagsDrawFilter paintFlagsDrawFilter;

    int width, height;
    private Paint paintBitmapButton;
    // 半径
    private int radius;
    // 刻度盘半径
    private int dialRadius;
    // 刻度高
    private int scaleHeight = dp2px(10);


    //根据图片动态缩放按钮图片大小
    private float zoomInButtomImageScale = 1f;

    // 按钮图片
    private Bitmap buttonImage;

    // 当前按钮旋转的角度
    private float rotateAngle;
    // 当前的角度
    private float currentAngle;

    private boolean isDown;
    private boolean isMove;
    //
    // 音量
    private int volume = 0;
    // 最低音量
    private int minVolume = 0;
    // 最高音量
    private int maxVolume = 15;

    // 四格代表音量1度
    private int angleRate = 3;

    // 初始图片按钮旋转角度
    private int startButtonAngle = 60;
    private int endButtonAngle = 300;
    private int diffValueButtonAngle = endButtonAngle - startButtonAngle;

    // 按钮宽高
    private int buttonWidth = dp2px(140);
    private int buttonHeight = dp2px(140);

    // 每格的角度
    private float angleOne = (float) diffValueButtonAngle / (maxVolume - minVolume) / angleRate;
    private String defaultDialColor = "#CCCCCC";
    private String selectDialColor = "#3CB7EA";

    private Paint paintArc;
    private int colorStart;
    private int colorEnd;

    public KnobScaleViewArc(Context context) {
        this(context, null);
    }

    public KnobScaleViewArc(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KnobScaleViewArc(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        buttonImage = BitmapFactory.decodeResource(getResources(), R.drawable.knob_btn_big);
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        initPaintParam();
    }

    private void initPaintParam() {
        paintBitmapButton = new Paint();

        paintArc = new Paint();
        paintArc.setStyle(Paint.Style.STROKE);
        paintArc.setAntiAlias(true);
        paintArc.setStrokeWidth(dp2px(2));
        paintArc.setDither(true);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 控件宽、高，取最小值
        width = height = Math.min(w, h);

        radius = width / 2 - dp2px(2);

        dialRadius = width / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 设置抗锯齿
        canvas.setDrawFilter(paintFlagsDrawFilter);
        // 画中间按钮
        drawBitmapButton(canvas);

        drawArc(canvas);
    }

    private void drawBitmapButton(Canvas canvas) {

        canvas.save();
        if (isDown) {
            zoomInButtomImageScale = 0.9f;
        } else {
            zoomInButtomImageScale = 1f;
        }
        canvas.scale(zoomInButtomImageScale, zoomInButtomImageScale, getWidth() / 2, getHeight() / 2);
        canvas.rotate(startButtonAngle + rotateAngle - 180, width / 2, height / 2);
        canvas.drawBitmap(buttonImage, (width - buttonWidth) / 2, (height - buttonHeight) / 2, paintBitmapButton);
        canvas.restore();
    }

    private void drawArc(Canvas canvas) {
        RectF rectF = new RectF(getWidth() / 2 - radius, getHeight() / 2 - radius, 2 * radius + (getWidth() / 2 - radius), 2 * radius + (getHeight() / 2 - radius));
        if (isEnable) {
            colorStart = Color.parseColor("#195571");
            colorEnd = Color.parseColor(selectDialColor);
        } else {
            colorStart = Color.parseColor("#383D57");
            colorEnd = Color.parseColor("#454B5C");
        }
        SweepGradient sweepGradient = new SweepGradient(getWidth() / 2, getHeight() / 2, colorStart, colorEnd);

        Matrix matrix = new Matrix();
        matrix.setRotate(130, getWidth() / 2, getHeight() / 2);//加上旋转还是很有必要的，每次最右边总是有一部分多余了,不太美观,也可以不加
        sweepGradient.setLocalMatrix(matrix);
        paintArc.setShader(sweepGradient);

        canvas.drawArc(rectF, 90 + startButtonAngle, diffValueButtonAngle, false, paintArc);
    }

    /**
     * 上一个进度，减少绘制次数
     */
    private int lastProgress;
    private boolean isEnable;

    public void setEnable(boolean enable) {
        isEnable = enable;
        if (isEnable) {
            buttonImage = BitmapFactory.decodeResource(getResources(), R.drawable.knob_btn_big);
        } else {
            buttonImage = BitmapFactory.decodeResource(getResources(), R.drawable.knob_btn_big_disable);
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnable) return true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDown = true;
                float downX = event.getX();
                float downY = event.getY();
                currentAngle = calcAngle(downX, downY);
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                isMove = true;
                float targetX;
                float targetY;
                downX = targetX = event.getX();
                downY = targetY = event.getY();
                float angle = calcAngle(targetX, targetY);

                // 滑过的角度增量
                float angleIncreased = angle - currentAngle;

                // 防止越界
                if (angleIncreased < -diffValueButtonAngle) {
                    angleIncreased = angleIncreased + 360;
                } else if (angleIncreased > diffValueButtonAngle) {
                    angleIncreased = angleIncreased - 360;
                }

                IncreaseAngle(angleIncreased);
                currentAngle = angle;
                invalidate();
                // 回调音量改变监听
                if (onVolumeChangeListener != null) {
                    if (volume != lastProgress) {
                        onVolumeChangeListener.onProgress(volume);
                    }
                }
                lastProgress = volume;
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                if (isDown) {
                    if (isMove) {
                        calcAngle();
                        isMove = false;
                    } else {
                        // 点击事件
                        if (onVolumeClickListener != null) {
                            onVolumeClickListener.onClick(volume);
                        }
                    }
                    isDown = false;
                    invalidate();
                }
                break;
            }
        }
        return true;
    }

    private void calcAngle() {
        // 纠正指针位置
        rotateAngle = (volume - minVolume) * angleRate * angleOne;
        // 回调音量改变监听
        if (onVolumeChangeListener != null) {
            onVolumeChangeListener.change(volume);
        }
    }

    /**
     * 刷新组件
     */
    private void invalidateView() {
        calcAngle();
        invalidate();
    }

    /**
     * 增加旋转角度
     *
     * @param angle 增加的角度
     */
    private void IncreaseAngle(float angle) {
        rotateAngle += angle;
        if (rotateAngle < 0) {
            rotateAngle = 0;
        } else if (rotateAngle > diffValueButtonAngle) {
            rotateAngle = diffValueButtonAngle;
        }
        // 加上0.5是为了取整时四舍五入
        volume = (int) ((rotateAngle / angleOne) / angleRate + 0.5) + minVolume;
    }


    /**
     * 以按钮圆心为坐标圆点，建立坐标系，求出(targetX, targetY)坐标与x轴的夹角
     *
     * @param targetX x坐标
     * @param targetY y坐标
     * @return (targetX, targetY)坐标与x轴的夹角
     */
    private float calcAngle(float targetX, float targetY) {
        float x = targetX - width / 2;
        float y = targetY - height / 2;
        double radian;

        if (x != 0) {
            float tan = Math.abs(y / x);
            if (x > 0) {
                if (y >= 0) {
                    radian = Math.atan(tan);
                } else {
                    radian = 2 * Math.PI - Math.atan(tan);
                }
            } else {
                if (y >= 0) {
                    radian = Math.PI - Math.atan(tan);
                } else {
                    radian = Math.PI + Math.atan(tan);
                }
            }
        } else {
            if (y > 0) {
                radian = Math.PI / 2;
            } else {
                radian = -Math.PI / 2;
            }
        }
        return (float) ((radian * 180) / Math.PI);
    }

    // 音量改变监听
    private OnVolumeChangeListener onVolumeChangeListener;
    // 控件点击监听
    private OnVolumeClickListener onVolumeClickListener;

    /**
     * 设置音量改变监听
     *
     * @param onVolumeChangeListener 监听接口
     */
    public void setOnVolumeChangeListener(OnVolumeChangeListener onVolumeChangeListener) {
        this.onVolumeChangeListener = onVolumeChangeListener;
    }

    /**
     * 设置点击监听
     *
     * @param onVolumeClickListener 点击回调接口
     */
    public void setOnClickListener(OnVolumeClickListener onVolumeClickListener) {
        this.onVolumeClickListener = onVolumeClickListener;
    }

    /**
     * 音量改变监听接口
     */
    public interface OnVolumeChangeListener {
        /**
         * 回调方法
         *
         * @param volume 音量
         */
        void change(int volume);

        /**
         * 滑动过程监听
         *
         * @param volume
         */
        void onProgress(int volume);
    }

    /**
     * 点击回调接口
     */
    public interface OnVolumeClickListener {
        /**
         * 点击回调方法
         *
         * @param volume 音量
         */
        void onClick(int volume);
    }

    public int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
        angleOne = (float) diffValueButtonAngle / (maxVolume - minVolume) / angleRate;
        invalidateView();
    }

    public int getMinVolume() {
        return minVolume;
    }

    public void setMinVolume(int minVolume) {
        this.minVolume = minVolume;
        angleOne = (float) diffValueButtonAngle / (maxVolume - minVolume) / angleRate;
    }

    public int getMaxVolume() {
        return maxVolume;
    }

    public void setMaxVolume(int maxVolume) {
        this.maxVolume = maxVolume;
        angleOne = (float) diffValueButtonAngle / (maxVolume - minVolume) / angleRate;
    }
}

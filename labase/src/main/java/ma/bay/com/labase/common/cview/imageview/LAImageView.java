package ma.bay.com.labase.common.cview.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;

import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;

import ma.bay.com.labase.R;

public class LAImageView extends AppCompatImageView {
    private float ratio = -10f;
    private float[] roundRect = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    private Path mPath;
    private RectF mRect;

    public LAImageView(Context context) {
        this(context, null);
    }

    public LAImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LAImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mPath = new Path();
        mRect = new RectF();
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, ma.bay.com.labase.R.styleable.LAImageView, 0, 0);
            ratio = typedArray.getFloat(R.styleable.LAImageView_ratio, -10f);
            float topLeft = typedArray.getDimension(ma.bay.com.labase.R.styleable.LAImageView_topLeftRadius, 0);
            float topRight = typedArray.getDimension(ma.bay.com.labase.R.styleable.LAImageView_topRightRadius, 0);
            float bottomLeft = typedArray.getDimension(ma.bay.com.labase.R.styleable.LAImageView_bottomLeftRadius, 0);
            float bottomRight = typedArray.getDimension(ma.bay.com.labase.R.styleable.LAImageView_bottomRightRadius, 0);
            if (typedArray.hasValue(R.styleable.LAImageView_radius)) {
                float radius = typedArray.getDimension(R.styleable.LAImageView_radius, 0);
                topLeft = topRight = bottomLeft = bottomRight = radius;
            }
            setRoundRectInternal(topLeft, topRight, bottomLeft, bottomRight);
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.ratio > 0.0f) {
            int measuredWidth = getMeasuredWidth();
            int measuredHeight = getMeasuredHeight();
            if (measuredWidth > 0) {
                setMeasuredDimension(MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec((int) ((((float) measuredWidth) * this.ratio) + 0.5f), MeasureSpec.EXACTLY));
            } else if (measuredHeight > 0) {
                setMeasuredDimension(MeasureSpec.makeMeasureSpec((int) ((((float) measuredHeight) * this.ratio) + 0.5f), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY));
            }
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //TODO 优化clipPath的锯齿问题
        mRect.left = 0;
        mRect.top = 0;
        mRect.right = getWidth();
        mRect.bottom = getHeight();
        mPath.addRoundRect(mRect, roundRect, Path.Direction.CW);
        canvas.clipPath(mPath);
        super.onDraw(canvas);
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
        requestLayout();

    }

    private void setRoundRectInternal(float topLeft, float topRight, float bottomLeft, float bottomRight) {
        this.roundRect[0] = topLeft;
        this.roundRect[1] = topLeft;
        this.roundRect[2] = topRight;
        this.roundRect[3] = topRight;
        this.roundRect[4] = bottomLeft;
        this.roundRect[5] = bottomLeft;
        this.roundRect[6] = bottomRight;
        this.roundRect[7] = bottomRight;
    }

    public void setRoundRect(float topLeft, float topRight, float bottomLeft, float bottomRight) {
        setRoundRectInternal(topLeft, topRight, bottomLeft, bottomRight);
        invalidate();
    }

}

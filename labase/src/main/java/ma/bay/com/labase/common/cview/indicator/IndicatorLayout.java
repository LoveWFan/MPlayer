package ma.bay.com.labase.common.cview.indicator;

import android.animation.ObjectAnimator;
import android.content.Context;
import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import ma.bay.com.labase.R;


/**
 * Created by wangle12138 on 2017/8/23.
 */

public class IndicatorLayout extends FrameLayout implements IIndicator, View.OnClickListener {
    private FailureListener mListener;

    private View mRootView;
    private ImageView mIvLoading;
    private View mViewFailure;
    private TextView mTvHint;
    private ImageView mIvFailure;

    private ObjectAnimator mAnimator;

    public IndicatorLayout(@NonNull Context context) {
        super(context);
    }

    public IndicatorLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setup();
    }

    void setup() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        mRootView = inflater.inflate(R.layout.layout_indicator, this, false);
        addView(mRootView);

        mIvFailure = (ImageView) mRootView.findViewById(R.id.failure);
        mViewFailure = mRootView.findViewById(R.id.view_indicator_failure);
        mIvLoading = (ImageView) mRootView.findViewById(R.id.view_indicator_loading);
        mTvHint = (TextView) mRootView.findViewById(R.id.view_indicator_text);

        mTvHint.setOnClickListener(this);
        mIvFailure.setOnClickListener(this);

        mAnimator = ObjectAnimator.ofFloat(mIvLoading, "rotation", 0, 360);
        mAnimator.setDuration(1000);

        mAnimator.setRepeatCount(ObjectAnimator.INFINITE);
    }

    @Override
    public void showIndicator() {
        mRootView.setVisibility(View.VISIBLE);
        mIvLoading.setVisibility(View.VISIBLE);
        mViewFailure.setVisibility(View.GONE);
        mAnimator.start();
    }

    @Override
    public void hideIndicator() {
        mRootView.setVisibility(View.GONE);
        mIvLoading.setVisibility(View.GONE);
        mViewFailure.setVisibility(View.GONE);

        mAnimator.cancel();
    }

    @Override
    public void showFailureIndicator() {
        mRootView.setVisibility(View.VISIBLE);
        mIvLoading.setVisibility(View.GONE);
        mViewFailure.setVisibility(View.VISIBLE);

        mAnimator.cancel();
    }

    @Override
    public void setOnHandleFailureListener(FailureListener listener) {
        this.mListener = listener;
    }

    public void setText(String text) {
        mTvHint.setText(text);
    }

    public void setFailureIcon(@DrawableRes int res) {
        mIvFailure.setImageDrawable(ContextCompat.getDrawable(getContext(), res));
    }

    @Override
    public void onClick(View v) {
        showIndicator();
        if (mListener != null) {
            mListener.onTryAgain();
        }
    }
}

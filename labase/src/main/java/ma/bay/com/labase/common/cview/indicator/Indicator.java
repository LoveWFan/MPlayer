package ma.bay.com.labase.common.cview.indicator;

import android.app.Activity;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Created by wangle12138 on 2017/8/23.
 */

public class Indicator {
    public static Indicator with(@NonNull View target) {
        if (target instanceof IndicatorLayout) {
            return new Indicator((IndicatorLayout) target);
        }

        if (target.getParent() instanceof IndicatorLayout) {
            return new Indicator((IndicatorLayout) target.getParent());
        }

        IndicatorLayout indicatorLayout = new IndicatorLayout(target.getContext());
        if (target.getParent() != null) {
            ViewGroup.LayoutParams params = target.getLayoutParams();
            ViewGroup parent = (ViewGroup) target.getParent();
            int index = parent.indexOfChild(target);
            parent.removeView(target);
            parent.addView(indicatorLayout, index, params);
        } else {
            ViewGroup.LayoutParams params = target.getLayoutParams();
            if (params != null) {
                indicatorLayout.setLayoutParams(params);
            }
        }

        // indicator
        IndicatorLayout.LayoutParams targetParams = new IndicatorLayout.LayoutParams(IndicatorLayout.LayoutParams.MATCH_PARENT, IndicatorLayout.LayoutParams.MATCH_PARENT);
        indicatorLayout.addView(target, targetParams);
        indicatorLayout.setup();

        return new Indicator(indicatorLayout);
    }

    public static Indicator with(@NonNull Activity activity) {
        View contentView = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        return with(contentView);
    }

    private IndicatorLayout mView;

    public Indicator(IndicatorLayout view) {
        mView = view;
    }

    public void show() {
        if (mView != null) {
            mView.showIndicator();
        }
    }

    public void hide() {
        if (mView != null) {
            mView.hideIndicator();
        }
    }

    public void showFailure() {
        if (mView != null) {
            mView.showFailureIndicator();
        }
    }

    public void setListener(FailureListener listener) {
        if (mView != null) {
            mView.setOnHandleFailureListener(listener);
        }
    }

    public void setText(String text) {
        if (mView != null) {
            mView.setText(text);
        }
    }

    public void setFailureIcon(@DrawableRes int res) {
        if (mView != null) {
            mView.setFailureIcon(res);
        }
    }

    public View getView() {
        return mView;
    }
}

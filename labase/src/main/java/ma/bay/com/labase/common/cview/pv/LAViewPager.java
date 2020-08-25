package ma.bay.com.labase.common.cview.pv;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import ma.bay.com.labase.R;


public class LAViewPager extends ViewPager {
	private boolean mScrollable = true;

	public LAViewPager(Context context) {
		super(context);
	}

	public LAViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray typedArray = null;
		try {
			typedArray = context.obtainStyledAttributes(attrs, R.styleable.LAViewPager, 0, 0);
			mScrollable = typedArray.getBoolean(R.styleable.LAViewPager_scrollable, true);
		} finally {
			if (typedArray != null) {
				typedArray.recycle();
			}
		}
	}

	public void setScrollable(boolean scrollable) {
		mScrollable = scrollable;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (!mScrollable) {
			return false;
		} else {
			return super.onInterceptTouchEvent(event);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (!mScrollable) {
			return false;
		} else {
			return super.onTouchEvent(ev);
		}
	}
}

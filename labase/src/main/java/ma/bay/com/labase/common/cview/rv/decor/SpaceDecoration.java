package ma.bay.com.labase.common.cview.rv.decor;

import android.graphics.Rect;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by chan on 2017/3/23.
 */
public class SpaceDecoration extends RecyclerView.ItemDecoration {
	private int mSpace;

	public SpaceDecoration(int space) {
		mSpace = space;
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		super.getItemOffsets(outRect, view, parent, state);

		final int currentPosition = parent.getChildLayoutPosition(view);
		int count = state.getItemCount();
		if (currentPosition < 0 || currentPosition >= count) {
			return;
		}

		RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
		if (layoutManager instanceof GridLayoutManager) {
			handleGridLayout(outRect, (GridLayoutManager) layoutManager, currentPosition, count);
		} else if (layoutManager instanceof LinearLayoutManager) {
			handleLinearLayout(outRect, (LinearLayoutManager) layoutManager, currentPosition, count);
		}
	}

	private void handleLinearLayout(Rect outRect, LinearLayoutManager linearLayoutManager, int currentPosition, int count) {
		if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
			if (currentPosition == 0) {
				outRect.set(0, 0, mSpace, 0);
			} else if (currentPosition == count - 1) {
				outRect.set(mSpace, 0, 0, 0);
			} else {
				outRect.set(mSpace, 0, mSpace, 0);
			}
		} else {
			if (currentPosition == 0) {
				outRect.set(0, 0, 0, mSpace);
			} else if (currentPosition == count - 1) {
				outRect.set(0, mSpace, 0, 0);
			} else {
				outRect.set(0, mSpace, 0, mSpace);
			}
		}
	}

	private void handleGridLayout(Rect outRect, GridLayoutManager gridLayoutManager, int currentPosition, int count) {
		int spanCount = gridLayoutManager.getSpanCount();

		int rowCount = count / spanCount;
		if (count % spanCount != 0) {
			++rowCount;
		}

		int currentColumn = currentPosition % spanCount;
		if (rowCount == 1) {
			if (currentColumn == 0) {
				outRect.set(0, 0, mSpace, 0);
			} else if (currentColumn == spanCount - 1) {
				outRect.set(mSpace, 0, 0, 0);
			} else {
				outRect.set(mSpace, 0, mSpace, 0);
			}
			return;
		}

		int currentRow = currentPosition / spanCount;
		if (currentRow == 0) {
			// 第一行
			if (currentColumn == 0) {
				outRect.set(0, 0, mSpace, mSpace);
			} else if (currentColumn == spanCount - 1) {
				outRect.set(mSpace, 0, 0, mSpace);
			} else {
				outRect.set(mSpace, 0, mSpace, mSpace);
			}
		} else if (currentRow + 1 == rowCount) {
			// 最后一行
			if (currentColumn == 0) {
				outRect.set(0, mSpace, mSpace, 0);
			} else if (currentColumn == spanCount - 1) {
				outRect.set(mSpace, mSpace, 0, 0);
			} else {
				outRect.set(mSpace, mSpace, mSpace, 0);
			}
		} else {
			if (currentColumn == 0) {
				outRect.set(0, mSpace, mSpace, mSpace);
			} else if (currentColumn == spanCount - 1) {
				outRect.set(mSpace, mSpace, 0, mSpace);
			} else {
				outRect.set(mSpace, mSpace, mSpace, mSpace);
			}
		}
	}
}


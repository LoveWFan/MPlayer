package ma.bay.com.labase.common.cview.rv;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
	// The minimum amount of items to have below your current scroll position
	// before loading more.
	// The current offset index of data you have loaded
	private int currentPage = 0;
	// The total number of items in the dataset after the last load
	private int previousTotalItemCount = 0;
	// True if we are still waiting for the last set of data to load.
	private boolean loading = true;
	// Sets the starting page index
	private int startingPageIndex = 0;

	private ScrollHandler mScrollHandler;

	public EndlessRecyclerOnScrollListener(LinearLayoutManager layoutManager) {
		if (layoutManager instanceof GridLayoutManager) {
			mScrollHandler = new GridLayoutScrollHandler((GridLayoutManager) layoutManager);
		} else {
			mScrollHandler = new LinearLayoutScrollHandler(layoutManager);
		}
	}

	// This happens many times a second during a scroll, so be wary of the code you place here.
	// We are given a few useful parameters to help us work out if we need to load some more data,
	// but first we check if we are waiting for the previous load to finish.
	@Override
	public void onScrolled(RecyclerView view, int dx, int dy) {
		mScrollHandler.onScrolled(view, dx, dy);
	}

	// Defines the process for actually loading more data based on page
	public abstract boolean onLoadMore(int page);

	public void reset() {
		currentPage = 0;
		previousTotalItemCount = 0;
		loading = true;
		startingPageIndex = 0;
	}


	private interface ScrollHandler {
		void onScrolled(RecyclerView view, int dx, int dy);
	}

	private class GridLayoutScrollHandler implements ScrollHandler {

		private GridLayoutManager mGridLayoutManager;

		public GridLayoutScrollHandler(GridLayoutManager gridLayoutManager) {
			mGridLayoutManager = gridLayoutManager;
		}

		@Override
		public void onScrolled(RecyclerView view, int dx, int dy) {
			int firstVisibleItem = mGridLayoutManager.findFirstVisibleItemPosition();
			int visibleItemCount = mGridLayoutManager.findLastVisibleItemPosition() - firstVisibleItem + 1;
			int totalItemCount = mGridLayoutManager.getItemCount();

			int maxLine = totalItemCount / mGridLayoutManager.getSpanCount();
			if (totalItemCount % mGridLayoutManager.getSpanCount() != 0) {
				++maxLine;
			}

			int firstLine = firstVisibleItem / mGridLayoutManager.getSpanCount();
			int visibleLine = visibleItemCount / mGridLayoutManager.getSpanCount();
			if (visibleItemCount % mGridLayoutManager.getSpanCount() != 0) {
				++visibleLine;
			}

			// If the total item count is zero and the previous isn't, assume the
			// list is invalidated and should be reset back to initial state
			if (totalItemCount < previousTotalItemCount) {
				currentPage = startingPageIndex;
				previousTotalItemCount = totalItemCount;
				if (totalItemCount == 0) {
					loading = true;
				}
			}
			// If it’s still loading, we check to see if the dataset count has
			// changed, if so we conclude it has finished loading and update the current page
			// number and total item count.
			if (loading && (totalItemCount > previousTotalItemCount)) {
				loading = false;
				previousTotalItemCount = totalItemCount;
			}

			if (!loading && (maxLine - visibleLine) <= firstLine) {
				if (onLoadMore(currentPage + 1)) {
					currentPage++;
				}
				loading = true;
			}
		}
	}

	private class LinearLayoutScrollHandler implements ScrollHandler {

		private LinearLayoutManager mLinearLayoutManager;

		public LinearLayoutScrollHandler(LinearLayoutManager linearLayoutManager) {
			mLinearLayoutManager = linearLayoutManager;
		}

		@Override
		public void onScrolled(RecyclerView view, int dx, int dy) {
			int firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
			int visibleItemCount = mLinearLayoutManager.findLastVisibleItemPosition() - firstVisibleItem + 1;
			int totalItemCount = mLinearLayoutManager.getItemCount();

			// If the total item count is zero and the previous isn't, assume the
			// list is invalidated and should be reset back to initial state
			if (totalItemCount < previousTotalItemCount) {
				currentPage = startingPageIndex;
				previousTotalItemCount = totalItemCount;
				if (totalItemCount == 0) {
					loading = true;
				}
			}
			// If it’s still loading, we check to see if the dataset count has
			// changed, if so we conclude it has finished loading and update the current page
			// number and total item count.
			if (loading && (totalItemCount > previousTotalItemCount)) {
				loading = false;
				previousTotalItemCount = totalItemCount;
			}

			// If it isn’t currently loading, we check to see if we have breached
			// the visibleThreshold and need to reload more data.
			// If we do need to reload some more data, we execute onLoadMore to fetch the data.
			if (!loading && (totalItemCount - visibleItemCount) <= firstVisibleItem) {
				if (onLoadMore(currentPage + 1)) {
					currentPage++;
				}
				loading = true;
			}
		}
	}
}
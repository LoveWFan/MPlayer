package ma.bay.com.labase.common.cview.rv;

/**
 * Created by chan on 2017/7/25.
 */

abstract class BaseLoadingEvent implements LoadingEvent {
	private boolean mIsGrabbing = false;

	@Override
	public void start() {
		mIsGrabbing = true;
		onEventStart();
	}

	@Override
	public void finish() {
		onEventSuccess();
		mIsGrabbing = false;
	}

	@Override
	public void failure() {
		onEventFailure();
		mIsGrabbing = false;
	}

	@Override
	public void drop() {
		onEventDrop();
		mIsGrabbing = false;
	}

	@Override
	public boolean isGrabbing() {
		return mIsGrabbing;
	}

	protected abstract void onEventStart();

	protected abstract void onEventSuccess();

	protected abstract void onEventFailure();

	protected abstract void onEventDrop();
}

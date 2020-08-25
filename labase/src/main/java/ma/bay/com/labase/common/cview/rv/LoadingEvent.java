package ma.bay.com.labase.common.cview.rv;

public interface LoadingEvent {
	void start();

	void finish();

	void failure();

	void drop();

	boolean isGrabbing();
}

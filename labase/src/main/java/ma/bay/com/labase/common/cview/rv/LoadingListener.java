package ma.bay.com.labase.common.cview.rv;

public interface LoadingListener {
	void onRefresh(LoadingEvent event);

	void onLoadMore(LoadingEvent event);

	void onReload(LoadingEvent event);
}

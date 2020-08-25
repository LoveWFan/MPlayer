package ma.bay.com.labase.common.cview.rv.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by octopusLW on 16/6/28.
 *
 * @param <VH> ViewHolder的泛型
 * @param <L>  Adapter的Listener接口的泛型
 * @param <D>  Adapter的数据类型，也就是{@link #setDataList(List)}方法中List的数据类型
 */
public abstract class BaseRVAdapter<VH extends BaseRVAdapter.ViewHolder, L extends BaseRVAdapter.Listener, D extends Object> extends RecyclerView.Adapter<VH> {

	protected Context mContext;
	protected List<D> mDataList = new ArrayList<>();
	private L mListener;

	public BaseRVAdapter(Context context) {
		mContext = context;
	}

	public void setDataList(List<D> dataList) {
		if (dataList != null) {
			mDataList.clear();
			mDataList.addAll(dataList);
			notifyDataSetChanged();
		}
	}

	public void addDataList(List<D> dataList) {
		int preCount = getItemCount();
		getDataList().addAll(dataList);
		notifyItemRangeInserted(preCount, dataList.size());
	}

	public List<D> getDataList() {
		return mDataList;
	}

	public D getItem(int position) {
		if (mDataList == null || mDataList.size() <= 0) {
			return null;
		}
		if (position < 0 || position >= mDataList.size()) {
			return null;
		}
		return mDataList.get(position);
	}

	@Override
	public int getItemCount() {
		return mDataList == null ? 0 : mDataList.size();
	}

	@Override
	public void onBindViewHolder(VH holder, int position, List<Object> payloads) {
		holder.position = position;
		holder.listener = getListener();
		super.onBindViewHolder(holder, position, payloads);
	}

	public L getListener() {
		return mListener;
	}

	public void setListener(L listener) {
		mListener = listener;
	}

	public interface Listener {
		void onItemClicked(int position);
	}

	public static class ViewHolder<L extends BaseRVAdapter.Listener> extends RecyclerView.ViewHolder {
		protected int position;
		protected L listener;

		public ViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (listener != null) {
						listener.onItemClicked(position);
					}
				}
			});
		}
	}

}

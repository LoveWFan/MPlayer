package com.poney.mplayer.biz.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.poney.mplayer.R;
import com.poney.mplayer.biz.ui.home.model.FileBean;
import com.poney.mplayer.biz.ui.home.video.VideoListActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ma.bay.com.labase.common.cview.rv.adapter.BaseRVAdapter;


public class HomeFragment extends Fragment {

    @BindView(R.id.file_recycler_view)
    RecyclerView fileRecyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.msg_error)
    TextView msgError;
    private HomeViewModel homeViewModel;
    private FileListAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, root);

        initView();
        initData();
        return root;
    }

    private void initData() {
        homeViewModel.getFileList().observe(getViewLifecycleOwner(), new Observer<List<FileBean>>() {
            @Override
            public void onChanged(List<FileBean> fileBeans) {
                mAdapter.setDataList(fileBeans);
                mAdapter.notifyDataSetChanged();
            }
        });

        homeViewModel.getErrorMsg().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                msgError.setText(s);
                msgError.setVisibility(View.VISIBLE);
            }
        });
        homeViewModel.getRefreshStatus().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean refresh) {
                refreshLayout.setRefreshing(refresh);
            }
        });

        // 下拉刷新监听
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        homeViewModel.getStorageFiles(getActivity());
    }

    private void refresh() {
        homeViewModel.getStorageFiles(getActivity());
    }

    private void initView() {
        mAdapter = new FileListAdapter(getActivity());
        mAdapter.setListener(new BaseRVAdapter.Listener() {
            @Override
            public void onItemClicked(int position) {
                Intent intent = new Intent(getActivity(), VideoListActivity.class);
                intent.putExtra("path", (mAdapter.getItemData(position)).path);
                getActivity().startActivity(intent);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        fileRecyclerView.setLayoutManager(layoutManager);
        fileRecyclerView.setItemAnimator(new DefaultItemAnimator());
        fileRecyclerView.setAdapter(mAdapter);

        // 这句话是为了，第一次进入页面的时候显示加载进度条
        refreshLayout.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, getResources()
                        .getDisplayMetrics()));
        refreshLayout.setProgressViewEndTarget(false, 200);
    }

    private static class FileListAdapter extends BaseRVAdapter<FileBeanViewHolder, BaseRVAdapter.Listener, FileBean> {
        public FileListAdapter(Context context) {
            super(context);
        }

        @NonNull
        @Override
        public FileBeanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new FileBeanViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_files_layout, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull FileBeanViewHolder holder, int position) {
            holder.tvPath.setText(getItemData(position).name);
            holder.tvCount.setText(getItemData(position).count + "个视频文件");
        }

        private FileBean getItemData(int position) {
            return mDataList.get(position);
        }
    }

    private static class FileBeanViewHolder extends BaseRVAdapter.ViewHolder {
        View mView;
        TextView tvPath, tvCount;

        public FileBeanViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            tvPath = (TextView) itemView.findViewById(R.id.tvPath);
            tvCount = (TextView) itemView.findViewById(R.id.tvCount);
        }

    }


}
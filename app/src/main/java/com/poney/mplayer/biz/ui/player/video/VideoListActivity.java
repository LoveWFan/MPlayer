package com.poney.mplayer.biz.ui.player.video;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poney.media.MediaDemoActivity;
import com.poney.mplayer.R;
import com.poney.mplayer.biz.ui.player.model.VideoBean;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ma.bay.com.labase.common.cview.rv.adapter.BaseRVAdapter;

public class VideoListActivity extends AppCompatActivity implements LifecycleOwner {

    @BindView(R.id.tvFilePath)
    TextView tvFilePath;
    @BindView(R.id.videoListView)
    RecyclerView videoListView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.iv_play_all)
    ImageView ivPlayAll;
    private VideoViewModel videoViewModel;
    private VideoListAdapter mAdapter;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        ButterKnife.bind(this);
        videoViewModel = ViewModelProviders.of(this).get(VideoViewModel.class);

        initView();
        initData();
    }

    private void initView() {
        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        tvFilePath.setText(path);

        mAdapter = new VideoListAdapter(this);
        mAdapter.setListener(new BaseRVAdapter.Listener() {
            @Override
            public void onItemClicked(int position) {
                handleVideoItemClick(position, mAdapter.getItemData(position));
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        videoListView.setLayoutManager(layoutManager);
        videoListView.setItemAnimator(new DefaultItemAnimator());
        videoListView.setAdapter(mAdapter);
    }

    private void handleVideoItemClick(int position, VideoBean itemData) {
        if (getVideoItemSuffix(itemData.path).equals("avi")) {
            MediaDemoActivity.intentTo(VideoListActivity.this, itemData.path);
        } else {
            VideoActivity.intentTo(VideoListActivity.this, path, position);
        }

    }

    private String getVideoItemSuffix(String path) {
        return path.substring(path.lastIndexOf(".") + 1);
    }

    private void initData() {
        videoViewModel.getProgress().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        videoViewModel.getVideoBeanList().observe(this, new Observer<List<VideoBean>>() {
            @Override
            public void onChanged(List<VideoBean> videoBeans) {
                mAdapter.setDataList(videoBeans);
            }
        });
        videoViewModel.getVideoData(new File(path));
    }

    private static class VideoListAdapter extends BaseRVAdapter<VideoBeanViewHolder, BaseRVAdapter.Listener, VideoBean> {
        public VideoListAdapter(Context context) {
            super(context);
        }

        @NonNull
        @Override
        public VideoBeanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VideoBeanViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_videos_layout, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull VideoBeanViewHolder holder, int position) {
            holder.tvName.setText(getItemData(position).name);
            holder.tvSize.setText(getItemData(position).size);
        }

        private VideoBean getItemData(int position) {
            return mDataList.get(position);
        }
    }

    private static class VideoBeanViewHolder extends BaseRVAdapter.ViewHolder {
        View mView;
        ImageView ivPic;
        TextView tvName, tvSize, tvlength;


        public VideoBeanViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ivPic = (ImageView) itemView.findViewById(R.id.ivPic);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvSize = (TextView) itemView.findViewById(R.id.tvSize);
            tvlength = (TextView) itemView.findViewById(R.id.tvlength);
            mView = itemView;
        }

    }
}
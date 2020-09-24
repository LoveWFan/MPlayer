package com.poney.mplayer.biz.ui.player.video;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.poney.mplayer.R;
import com.poney.mplayer.biz.ui.player.model.VideoBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoActivity extends AppCompatActivity {

    @BindView(R.id.exo_play)
    PlayerView exoPlay;
    @BindView(R.id.tv_name)
    TextView tvName;
    private SimpleExoPlayer simpleExoPlayer;
    private List<VideoBean> videoBeanList;
    private int curPosition;

    private static Intent newIntent(Context context, List<VideoBean> videoBeanList, int position) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putParcelableArrayListExtra("videoBeanList", (ArrayList<? extends Parcelable>) videoBeanList);
        intent.putExtra("position", position);
        return intent;
    }

    public static void intentTo(Context context, List<VideoBean> videoBeanList) {
        intentTo(context, videoBeanList, 0);
    }

    public static void intentTo(Context context, List<VideoBean> videoBeanList, int position) {
        context.startActivity(newIntent(context, videoBeanList, position));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);

        videoBeanList = getIntent().getParcelableArrayListExtra("videoBeanList");
        curPosition = getIntent().getIntExtra("position", 0);
        simpleExoPlayer = new SimpleExoPlayer.Builder(this).build();
        exoPlay.setPlayer(simpleExoPlayer);
        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                if (isLoading) {
                    // Load metadata for mediaId and update the UI.
                    String currentTag = (String) simpleExoPlayer.getCurrentTag();
                    tvName.setText(currentTag);
                }
            }

            @Override
            public void onPositionDiscontinuity(@Player.DiscontinuityReason int reason) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
        MediaSource mediaSource = buildMediaSource(getUri());
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.seekTo(curPosition, C.TIME_UNSET);
    }

    private Uri[] getUri() {
        Uri[] uris = new Uri[videoBeanList.size()];
        int i = 0;
        for (VideoBean videoBean : videoBeanList) {
            uris[i] = Uri.fromFile(new File(videoBean.path));
            i++;
        }
        return uris;
    }

    private MediaSource buildMediaSource(Uri... uriList) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, Util.getUserAgent(this, getString(R.string.app_name)));

        MediaSource[] mediaSourceList = new MediaSource[uriList.length];
        for (int i = 0; i < uriList.length; i++) {
            Uri uri = uriList[i];
            mediaSourceList[i] = new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .setTag(uri.getPath())
                    .createMediaSource(uri);

        }

        return new ConcatenatingMediaSource(mediaSourceList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        simpleExoPlayer.release();
    }
}
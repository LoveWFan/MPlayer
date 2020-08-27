package com.poney.mplayer.biz.ui.home.video;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.poney.mplayer.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoActivity extends AppCompatActivity {

    @BindView(R.id.exo_play)
    PlayerView exoPlay;
    @BindView(R.id.tv_name)
    TextView tvName;
    private SimpleExoPlayer simpleExoPlayer;
    private String rootPath;
    private int curPosition;

    public static Intent newIntent(Context context, String rootPath, int position) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra("rootPath", rootPath);
        intent.putExtra("position", position);
        return intent;
    }


    public static void intentTo(Context context, String rootPath, int position) {
        context.startActivity(newIntent(context, rootPath, position));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);

        rootPath = getIntent().getStringExtra("rootPath");
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
        MediaSource mediaSource = buildMediaSource(getUri(rootPath));
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.seekTo(curPosition, C.TIME_UNSET);
    }

    private Uri[] getUri(String rootPath) {
        File[] files = new File(rootPath).listFiles();
        Uri[] uris = new Uri[files.length];
        for (int i = 0; i < files.length; i++) {
            uris[i] = Uri.fromFile(files[i]);
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
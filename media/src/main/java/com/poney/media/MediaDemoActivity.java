package com.poney.media;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

public class MediaDemoActivity extends AppCompatActivity {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private Integer player;
    private String path;

    public static void intentTo(Context context, String path) {
        context.startActivity(newIntent(context, path));
    }

    private static Intent newIntent(Context context, String path) {
        Intent intent = new Intent(context, MediaDemoActivity.class);
        intent.putExtra("path", path);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_demo);
        // Example of a call to a native method
        initSfv();
    }

    private void initSfv() {
        path = getIntent().getStringExtra("path");
        if (TextUtils.isEmpty(path))
            return;
        SurfaceView sfv = findViewById(R.id.sfv);
        sfv.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (player == null) {
                    player = createPlayer(path, holder.getSurface());
                    play(player);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (player != null) {
                    pause(player);
                }
            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native int createPlayer(String path, Surface surface);

    public native void play(int player);

    public native void pause(int player);
}
package ma.bay.com.labase.common.effect;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


public class RecordEffect implements Runnable {
    private static final String TAG = "RecordEffect";
    private static RecordEffect sMotionEffect;

    private int mEffectId = 0;
    private SoundPool mSoundPool;
    private Handler mHandler;
    private Callback mCallback;

    private RecordEffect(Context context) {
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        try {
            AssetFileDescriptor assetFileDescriptor = context.getAssets().openFd("audio/sound_record_notify.aac");
            mEffectId = mSoundPool.load(assetFileDescriptor, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mHandler = new Handler(Looper.getMainLooper());
    }

    private void play(Callback callback) {
        try {
            mSoundPool.play(mEffectId, 1, 1, 0, 0, 1);
            mCallback = callback;
            d("post");
            mHandler.postDelayed(this, 450);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stop() {
        mCallback = null;
        d("remove all runnable");
        mHandler.removeCallbacks(this);
    }

    private static void d(String msg) {
        Log.d(TAG, msg);
    }

    public static void init(Context context) {
        getInstance(context);
    }

    private static RecordEffect getInstance(Context context) {
        if (sMotionEffect == null) {
            synchronized (MotionEffect.class) {
                if (sMotionEffect == null) {
                    sMotionEffect = new RecordEffect(context.getApplicationContext());
                }
            }
        }
        return sMotionEffect;
    }

    @Override
    public void run() {
        if (mCallback != null) {
            d("complete");
            mCallback.onComplete();
            mCallback = null;
        }
    }

    public static void invoke(Context context, final Callback callback) {
        release(context);
        final RecordEffect recordEffect = getInstance(context);
        if (recordEffect != null) {
            recordEffect.play(callback);
        }
    }

    public static void release(Context context) {
        RecordEffect recordEffect = getInstance(context);
        if (recordEffect != null) {
            recordEffect.stop();
        }
    }

    public interface Callback {
        void onComplete();
    }
}

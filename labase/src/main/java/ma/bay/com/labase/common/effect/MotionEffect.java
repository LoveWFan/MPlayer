package ma.bay.com.labase.common.effect;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import org.apache.commons.lang3.exception.ExceptionUtils;


/**
 * Created by wangl2138 on 2018/1/17.
 */

public class MotionEffect {
    private static final String TAG = "MotionEffect";
    private static MotionEffect sMotionEffect;

    private int mEffectId = 0;
    private SoundPool mSoundPool;

    private MotionEffect(Context context) {
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        try {
            AssetFileDescriptor assetFileDescriptor = context.getAssets().openFd("audio/sound_button.aac");
            mEffectId = mSoundPool.load(assetFileDescriptor, 1);
        } catch (Exception e) {
            i("init failed: " + ExceptionUtils.getStackTrace(e));
        }
    }

    private void play() {
        try {
            mSoundPool.play(mEffectId, 1, 1, 0, 0, 1);
        } catch (Exception e) {
            i("record_play failed: " + ExceptionUtils.getStackTrace(e));
        }
    }

    public static void invoke(Context context) {
        MotionEffect motionEffect = getInstance(context);
        if (motionEffect != null) {
            motionEffect.play();
        }
    }

    private static void i(String msg) {
        Log.i(TAG, msg);
    }

    public static void init(Context context) {
        getInstance(context);
    }

    private static MotionEffect getInstance(Context context) {
        if (sMotionEffect == null) {
            synchronized (MotionEffect.class) {
                if (sMotionEffect == null) {
                    sMotionEffect = new MotionEffect(context.getApplicationContext());
                }
            }
        }
        return sMotionEffect;
    }
}

package ma.bay.com.labase.common.glide;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class BlurTransform extends BitmapTransformation {
    private static final String ID = BlurTransform.class.getSimpleName();
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    public Bitmap blur(Bitmap source, BitmapPool bitmapPool) {
        if (source == null) {
            return null;
        }

        float scaleFactor = 1f;
        float radius = 10;

        Bitmap overlay = bitmapPool.get((int) (source.getWidth() / scaleFactor), (int) (source.getHeight() / scaleFactor),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(overlay);
        canvas.scale(scaleFactor, scaleFactor);

        ColorMatrix cMatrix = new ColorMatrix();
        int brightness = 20;
        cMatrix.setSaturation(1.0f);
        cMatrix.set(new float[]{1, 0, 0, 0, brightness, 0, 1, 0, 0, brightness, 0, 0, 1, 0, brightness, 0, 0, 0, 1, 0});

        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));
        canvas.drawBitmap(source, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int) radius, true);

        return overlay;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool bitmapPool, @NonNull Bitmap bitmap, int i, int i1) {
        return blur(bitmap, bitmapPool);
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}

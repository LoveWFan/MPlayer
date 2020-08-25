package ma.bay.com.labase.common.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;


public class CircleBoundTransform extends BitmapTransformation {
    // The version of this transformation, incremented to correct an error in a previous version.
    // See #455.
    private static final String ID = CircleBoundTransform.class.getSimpleName();
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);
    public static final int PAINT_FLAGS = Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG;
    private static final int CIRCLE_CROP_PAINT_FLAGS = PAINT_FLAGS | Paint.ANTI_ALIAS_FLAG;
    private static final Paint CIRCLE_CROP_SHAPE_PAINT = new Paint(CIRCLE_CROP_PAINT_FLAGS);
    private static final Paint CIRCLE_CROP_BITMAP_PAINT;
    private Paint mBoundPaint;

    static {
        CIRCLE_CROP_BITMAP_PAINT = new Paint(CIRCLE_CROP_PAINT_FLAGS);
        CIRCLE_CROP_BITMAP_PAINT.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    }

    public CircleBoundTransform(int color, float width) {
        // Intentionally empty.
        mBoundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBoundPaint.setColor(color);
        mBoundPaint.setStrokeWidth(width);
        mBoundPaint.setStyle(Paint.Style.STROKE);
    }


    @Deprecated
    public CircleBoundTransform(@SuppressWarnings("unused") Context context) {
        this(Color.WHITE, 5f);
    }


    @Deprecated
    public CircleBoundTransform(@SuppressWarnings("unused") BitmapPool bitmapPool) {
        this(Color.WHITE, 5f);
    }

    // Bitmap doesn't implement equals, so == and .equals are equivalent here.
    @SuppressWarnings("PMD.CompareObjectsWithEquals")
    @Override
    protected Bitmap transform(
            @NonNull BitmapPool pool, @NonNull Bitmap inBitmap, int destWidth, int destHeight) {
        int destMinEdge = Math.min(destWidth, destHeight);
        float radius = destMinEdge / 2f;

        int srcWidth = inBitmap.getWidth();
        int srcHeight = inBitmap.getHeight();

        float scaleX = destMinEdge / (float) srcWidth;
        float scaleY = destMinEdge / (float) srcHeight;
        float maxScale = Math.max(scaleX, scaleY);

        float scaledWidth = maxScale * srcWidth;
        float scaledHeight = maxScale * srcHeight;
        float left = (destMinEdge - scaledWidth) / 2f;
        float top = (destMinEdge - scaledHeight) / 2f;

        RectF destRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        // Alpha is required for this transformation.
        Bitmap toTransform = getAlphaSafeBitmap(pool, inBitmap);

        Bitmap result = pool.get(destMinEdge, destMinEdge, Bitmap.Config.ARGB_8888);
        result.setHasAlpha(true);


        Canvas canvas = new Canvas(result);
        // Draw a circle
        canvas.drawCircle(radius, radius, radius, CIRCLE_CROP_SHAPE_PAINT);
        // Draw the bitmap in the circle
        canvas.drawBitmap(toTransform, null, destRect, CIRCLE_CROP_BITMAP_PAINT);
        canvas.drawCircle(radius, radius, radius - mBoundPaint.getStrokeWidth() / 2.0f + 0.2f, mBoundPaint);
        canvas.setBitmap(null);

        if (!toTransform.equals(inBitmap)) {
            pool.put(toTransform);
        }

        return result;
    }

    private Bitmap getAlphaSafeBitmap(@NonNull BitmapPool pool,
                                      @NonNull Bitmap maybeAlphaSafe) {
        if (Bitmap.Config.ARGB_8888.equals(maybeAlphaSafe.getConfig())) {
            return maybeAlphaSafe;
        }

        Bitmap argbBitmap = pool.get(maybeAlphaSafe.getWidth(), maybeAlphaSafe.getHeight(),
                Bitmap.Config.ARGB_8888);
        new Canvas(argbBitmap).drawBitmap(maybeAlphaSafe, 0 /*left*/, 0 /*top*/, null /*pain*/);

        // We now own this Bitmap. It's our responsibility to replace it in the pool outside this method
        // when we're finished with it.
        return argbBitmap;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CircleBoundTransform;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}


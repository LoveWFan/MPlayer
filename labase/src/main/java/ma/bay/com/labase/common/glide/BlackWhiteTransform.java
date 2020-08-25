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


public class BlackWhiteTransform extends BitmapTransformation {
	private static final String ID = BlackWhiteTransform.class.getSimpleName();
	private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

	@Override
	protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
		return convertToBlackWhite(pool, toTransform);
	}

	public Bitmap convertToBlackWhite(BitmapPool pool, Bitmap source) {
		float scaleFactor = 1f;
		Bitmap overlay = pool.get((int) (source.getWidth() / scaleFactor), (int) (source.getHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(overlay);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(source, 0, 0, paint);
		return overlay;
	}

	@Override
	public void updateDiskCacheKey(MessageDigest messageDigest) {
		messageDigest.update(ID_BYTES);
	}
}

package ma.bay.com.labase.common.glide;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import android.widget.ImageView;

import ma.bay.com.labase.R;


public class ColorPlaceHolder {
    private static ColorPlaceHolder sColorPlaceHolder;

    private static int[] sColorPool = {
            R.color.color_965_orange,
            R.color.color_796_green,
            R.color.color_688_cyan,
            R.color.color_567_blue,
            R.color.color_866_red
    };
    private int mCurrentIndex = 0;


    public static void render(ImageView imageView) {
        imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), getColor()));
    }

    @DrawableRes
    public static int getColor() {
        ColorPlaceHolder holder = getInstance();
        holder.mCurrentIndex = (holder.mCurrentIndex + 1) % sColorPool.length;
        return sColorPool[holder.mCurrentIndex];
    }

    private static ColorPlaceHolder getInstance() {
        if (sColorPlaceHolder == null) {
            synchronized (ColorPlaceHolder.class) {
                if (sColorPlaceHolder == null) {
                    sColorPlaceHolder = new ColorPlaceHolder();
                }
            }
        }
        return sColorPlaceHolder;
    }
}

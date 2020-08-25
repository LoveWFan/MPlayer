package ma.bay.com.labase.common.glide;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import ma.bay.com.labase.R;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ImageLoader {
    private static final String TAG = "ImageLoader";


    public static void init() {
        try {
            ViewTarget.setTagId(R.id.glide_tag_id);
        } catch (Exception ignore) {
        }
    }

    public static Builder newTask(@NonNull RequestManager requestManager) {
        return new Builder(requestManager);
    }

    public static Builder newTask(Fragment fragment) {
        return new Builder(Glide.with(fragment));
    }

    public static Builder newTask(androidx.fragment.app.Fragment fragment) {
        return new Builder(Glide.with(fragment));
    }

    public static class Builder {
        private static final int TRANSFORM_CIRCLE = 1;
        private static final int TRANSFORM_ROUND_RECT = 2;
        private static final int TRANSFORM_CIRCLE_BOUND = 3;

        private RequestManager mRequestManager;

        private ImageView mImageView = null;
        private int mPlaceHolderResId = 0;
        private Drawable mPlaceHolderDrawable = null;
        private List<String> mUrls;
        private int mResourceId = 0;
        private int mTransform = 0;
        private float mRoundRectRadius = 0;
        private boolean mEnableCrossFade = true;
        private boolean mEnableBlackWhite = false;
        private boolean mEnableBlur = false;
        private DiskCacheStrategy mDiskCacheStrategy = null;
        private OnResourceReadyListener mOnResourceReadyListener = null;
        private OnLoadFailedListener mOnLoadFailedListener = null;
        private String mKey = null;
        private DecodeFormat mDecodeFormat = null;
        private boolean mWithoutAnim = false;
        private int mErrorResourceId = 0;
        private float mRoundBoundWidth;

        public Builder(@NonNull RequestManager requestManager) {
            mRequestManager = requestManager;
            mUrls = new ArrayList<>();
        }

        public Builder view(ImageView view) {
            mImageView = view;
            return this;
        }

        public Builder url(String url) {
            mUrls.clear();
            mUrls.add(url);
            return this;
        }

        public Builder url(List<String> urls) {
            mUrls.clear();
            if (urls != null) {
                mUrls.addAll(urls);
            }
            return this;
        }

        public Builder uri(Uri uri) {
            url(uri.toString());
            return this;
        }

        public Builder cache(String cacheFile, String url) {
            List<String> list = new ArrayList<>();
            list.add(url);
            return cache(cacheFile, list);
        }

        public Builder cache(String cacheFile, List<String> urls) {
            if (!TextUtils.isEmpty(cacheFile)) {
                File cache = new File(cacheFile);
                if (cache.exists() && cache.isFile()) {
                    d("cache hint");
                    urls.add(0, Uri.fromFile(cache).toString());
                }
            }
            return url(urls);
        }

        public Builder file(File file) {
            mUrls.clear();
            mUrls.add(file.getAbsolutePath());
            return this;
        }

        public Builder resId(int resId) {
            mResourceId = resId;
            return this;
        }

        public Builder placeholder(int resId) {
            mPlaceHolderResId = resId;
            return this;
        }

        public Builder placeholder(Drawable drawable) {
            mPlaceHolderDrawable = drawable;
            return this;
        }

        public Builder round() {
            mTransform = TRANSFORM_CIRCLE;
            return this;
        }

        public Builder roundRect(float radius) {
            mTransform = TRANSFORM_ROUND_RECT;
            mRoundRectRadius = radius;
            return this;
        }

        public Builder roundBound(float width) {
            mTransform = TRANSFORM_CIRCLE_BOUND;
            mRoundBoundWidth = width;
            return this;
        }

        public Builder blur() {
            mEnableBlur = true;
            return this;
        }

        public Builder crossFade(boolean enable) {
            mEnableCrossFade = enable;
            return this;
        }

        public Builder blackWhite() {
            mEnableBlackWhite = true;
            return this;
        }

        public Builder diskStrategy(DiskCacheStrategy strategy) {
            mDiskCacheStrategy = strategy;
            return this;
        }

        public Builder resourceReadyListener(OnResourceReadyListener onResourceReadyListener) {
            mOnResourceReadyListener = onResourceReadyListener;
            return this;
        }

        public Builder loadFailedListener(OnLoadFailedListener onLoadFailedListener) {
            mOnLoadFailedListener = onLoadFailedListener;
            return this;
        }

        public Builder signature(String key) {
            mKey = key;
            return this;
        }

        public Builder format(DecodeFormat format) {
            mDecodeFormat = format;
            return this;
        }

        public Builder dontAnim() {
            mWithoutAnim = true;
            return this;
        }

        public Builder errorRes(int resId) {
            mErrorResourceId = resId;
            return this;
        }

        public void start() {
            mRequestManager.clear(mImageView);
            if (mTransform == TRANSFORM_ROUND_RECT && mImageView != null) {
                mImageView.post(new Runnable() {
                    @Override
                    public void run() {
                        doStart();
                    }
                });
            } else {
                doStart();
            }
        }

        private void doStart() {
            RequestBuilder<Drawable> request;
            RequestOptions options = new RequestOptions();

            if (mUrls != null && !mUrls.isEmpty()) {
                request = mRequestManager.load(mUrls.get(0));
            } else if (mResourceId != 0) {
                request = mRequestManager.load(mResourceId);
            } else {
                return;
            }

            if (mDecodeFormat != null) {
                options = options.format(mDecodeFormat);
            }
            if (mPlaceHolderResId != 0) {
                options = options.placeholder(mPlaceHolderResId);
            } else if (mPlaceHolderDrawable != null) {
                options = options.placeholder(mPlaceHolderDrawable);
            }

            if (mTransform == TRANSFORM_CIRCLE) {
                options = options.circleCrop();
            } else if (mTransform == TRANSFORM_ROUND_RECT && mImageView != null) {
                options = options.transform(new RoundRectTransform(mImageView.getMeasuredWidth(), mRoundRectRadius));
            } else if (mTransform == TRANSFORM_CIRCLE_BOUND) {
                options = options.transform(new CircleBoundTransform(Color.WHITE, mRoundBoundWidth));
            }

            if (mDiskCacheStrategy != null) {
                options = options.diskCacheStrategy(mDiskCacheStrategy);
            }

            if (mEnableBlackWhite) {
                options = options.transform(new BlackWhiteTransform());
            }

            if (mEnableBlur) {
                options = options.transform(new BlurTransform());
            }

            if (!TextUtils.isEmpty(mKey)) {
                Key key = new Key() {
                    @Override
                    public void updateDiskCacheKey(MessageDigest messageDigest) {
                        try {
                            messageDigest.update(mKey.getBytes(STRING_CHARSET_NAME));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                };
                options = options.signature(key);
            }

            if (mWithoutAnim) {
                options = options.dontAnimate();
            }

            if (mErrorResourceId != 0) {
                options.error(mErrorResourceId);
            }

            request.apply(options);

            if (mEnableCrossFade) {
                request.transition(withCrossFade());
            }

            if (mOnResourceReadyListener != null || mOnLoadFailedListener != null) {
                request.listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (mOnLoadFailedListener != null) {
                            mOnLoadFailedListener.onLoadFailed(e);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (mOnResourceReadyListener != null) {
                            mOnResourceReadyListener.onResourceReady();
                        }
                        return false;
                    }
                });
            }

            if (mImageView != null) {
                if (mImageView.getVisibility() != View.VISIBLE) {
                    request.into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                            if (mImageView != null) {
                                mImageView.setImageDrawable(resource);
                            }
                        }
                    });
                } else {
                    request.into(mImageView);
                }
            }
        }
    }

    private static void d(String msg) {
        Log.d(TAG, msg);
    }

    public interface OnResourceReadyListener {
        void onResourceReady();
    }

    public interface OnLoadFailedListener {
        void onLoadFailed(Exception e);
    }
}

package ma.bay.com.labase.common.cview.lood;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ma.bay.com.labase.R;

public class LoodView extends FrameLayout {
    //轮播图图片数量
    private final static int IMAGE_COUNT = 4;
    //自动轮播时间间隔
    private final static int TIME_INTERVAL = 3;
    //自动轮播启用开关
    private final static boolean isAutoPlay = true;
    //ImageView资源ID
    private int[] imageResIds;
    private FPagerAdapter fPagerAdapter;
    //    private ArrayList<String> imageNet = new ArrayList<>();
    private List<ImageView> imageViewList;
    private List<View> dotViewList;
    private ViewPager viewPager;
    private boolean isFromCache = true;
    private Context mContext;
    //当前轮播页面
    private int count = 0;
    private int currentItem;
    //定时任务
    private ScheduledExecutorService scheduledExecutorService;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                viewPager.setCurrentItem(currentItem, false);
            } else {
                viewPager.setCurrentItem(currentItem);
            }

        }
    };
    private int lastPosition = 1;


    public LoodView(Context context) {
        super(context);
        mContext = context;
    }

    public LoodView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
        mContext = context;
    }

    public LoodView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        mContext = context;
        initImageView();
        initUI(context);
        if (isAutoPlay) {
            startPlay();
        }

    }

    public void onDestroy() {
        scheduledExecutorService.shutdownNow();
        scheduledExecutorService = null;
    }

    /**
     * 开始轮播图切换
     */

    public void startPlay() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new LoopTask(), 3, TIME_INTERVAL, TimeUnit.SECONDS);
    }

    /**
     * 停止切换
     */
    private void stopPlay() {
        scheduledExecutorService.shutdown();
    }

    /**
     * 初始化UI
     *
     * @param context
     */
    private void initUI(Context context) {
        currentItem = 1;
        LayoutInflater.from(context).inflate(R.layout.lood_view, this, true);
        imageViewList.clear();

        count = imageResIds.length;

        for (int i = 0; i <= count + 1; i++) {
            ImageView mAlbumArt = new ImageView(context);

            Object url = null;
            if (i == 0) {
                url = imageResIds[count - 1];
            } else if (i == count + 1) {
                url = imageResIds[0];
            } else {
                url = imageResIds[i - 1];
            }

            Glide.with(context).load(url).into(mAlbumArt);
            imageViewList.add(mAlbumArt);
        }

        dotViewList.add(findViewById(R.id.v_dot1));
        dotViewList.add(findViewById(R.id.v_dot2));
        dotViewList.add(findViewById(R.id.v_dot3));
        dotViewList.add(findViewById(R.id.v_dot4));


        viewPager = (ViewPager) findViewById(R.id.viewPager);

        fPagerAdapter = new FPagerAdapter();
        viewPager.setAdapter(fPagerAdapter);

        viewPager.setFocusable(true);
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new MyPageChangeListener());
    }

    private void initImageView() {
        imageResIds = new int[]{
                R.mipmap.first,
                R.mipmap.second,
                R.mipmap.third,
                R.mipmap.fourth
        };
        imageViewList = new ArrayList<>();
        dotViewList = new ArrayList<>();
    }

    private class FPagerAdapter extends PagerAdapter {


        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            container.addView(imageViewList.get(position));
            View view = imageViewList.get(position);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "你点击了第" + (position) + "个位置", Toast.LENGTH_SHORT).show();
                }
            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViewList.get(position));
        }

        @Override
        public int getCount() {
            return imageViewList.size();
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
            super.restoreState(state, loader);
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(ViewGroup container) {
            super.startUpdate(container);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;

            dotViewList.get((lastPosition - 1 + count) % count).setBackgroundResource(R.mipmap.grey_point);
            dotViewList.get((position - 1 + count) % count).setBackgroundResource(R.mipmap.red_point);
            lastPosition = position;

            if (position == 0) position = count;
            if (position > count) position = 1;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case 0://No operation
                    if (currentItem == 0) {
                        viewPager.setCurrentItem(count, false);
                    } else if (currentItem == count + 1) {
                        viewPager.setCurrentItem(1, false);
                    }
                    break;
                case 1://start Sliding
                    if (currentItem == count + 1) {
                        viewPager.setCurrentItem(1, false);
                    } else if (currentItem == 0) {
                        viewPager.setCurrentItem(count, false);
                    }
                    break;
                case 2://end Sliding
                    break;
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isAutoPlay) {
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                startPlay();
            } else if (action == MotionEvent.ACTION_DOWN) {
                stopPlay();
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    private class LoopTask implements Runnable {
        @Override
        public void run() {

            if (count > 1 && isAutoPlay) {
                currentItem = currentItem % (count + 1) + 1;
                if (currentItem == 1) {
                    handler.obtainMessage(currentItem).sendToTarget();
                } else {
                    handler.obtainMessage(currentItem).sendToTarget();
                }
            }


        }
    }

    /**
     * 销毁ImageView回收资源
     */
    private void destoryBitmaps() {
        for (int i = 0; i < IMAGE_COUNT; i++) {
            ImageView imageView = imageViewList.get(i);
            Drawable drawable = imageView.getDrawable();
            if (drawable != null)
                //解除drawable对view的引用
                drawable.setCallback(null);
        }
    }
}


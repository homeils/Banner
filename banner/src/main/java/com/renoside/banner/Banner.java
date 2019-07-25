package com.renoside.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Banner extends ConstraintLayout {

    /**
     * 控件
     */
    private ViewPager viewPager;
    private TextView title;
    private LinearLayout points;
    private LinearLayout background;
    /**
     * 图片加载器
     */
    private ImageLoader imageLoader;
    /**
     * 页面集合
     */
    private List<ImageView> imageViews;
    /**
     * 标题集合
     */
    private List<String> titles;
    /**
     * 标题字体颜色
     */
    private int titleColor = 0xFFFFFFFF;
    /**
     * 导航点大小
     */
    private int pointSize = 15;
    /**
     * 上一个页面的位置
     */
    private int prePosition = 0;
    /**
     * 轮播间隔
     */
    private int relay = 4000;
    /**
     * 是否已经滑动
     */
    private boolean isDragging = false;
    /**
     * 点击监听
     */
    private OnBannerListener onBannerListener;
    /**
     * 设置自动轮播消息队列
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            handler.sendEmptyMessageDelayed(0, relay);
        }
    };

    public Banner(Context context) {
        super(context);
        initView();
    }

    public Banner(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.banner, this, true);
        initView();
    }

    public Banner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化控件和集合
     */
    private void initView() {
        viewPager = findViewById(R.id.viewpager);
        title = findViewById(R.id.title);
        points = findViewById(R.id.points);
        background = findViewById(R.id.background);
        imageViews = new ArrayList<>();
        titles = new ArrayList<>();
    }

    /**
     * 设置图片加载器
     *
     * @param imageLoader
     */
    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    /**
     * 设置导航点大小
     *
     * @param size
     */
    public void setPointSize(int size) {
        this.pointSize = size;
    }

    /**
     * 设置轮播间隔时间
     *
     * @param relay
     */
    public void setRelay(int relay) {
        this.relay = relay;
    }

    /**
     * 设置图片集合并生成导航点
     *
     * @param imageList
     */
    public void setImages(List<?> imageList) {
        imageViews.clear();
        points.removeAllViews();
        for (int i = 0; i < imageList.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageLoader.conImageLoader(getContext(), imageList.get(i), imageView);
            imageViews.add(imageView);
            ImageView point = new ImageView(getContext());
            point.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pointSize, pointSize);
            if (i == 0) {
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
                params.leftMargin = 8;
            }
            point.setLayoutParams(params);
            points.addView(point);
        }
    }

    /**
     * 设置标题文本颜色
     *
     * @param color
     */
    public void setTitleColor(int color) {
        this.titleColor = color;
    }

    /**
     * 设置标题
     *
     * @param titleList
     */
    public void setTitles(List<String> titleList) {
        titles.clear();
        for (int i = 0; i < titleList.size(); i++) {
            String tmp = titleList.get(i);
            titles.add(tmp);
        }
    }

    /**
     * ViewPager设置适配器及监听
     */
    public void start() {
        viewPager.setAdapter(new ViewPagerBanner());
        int position = 1000 / 2 - 1000 / 2 % imageViews.size();
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(new PageChangeListener());
        if (titles != null && titles.size() != 0) {
            title.setTextColor(titleColor);
            title.setText(titles.get(prePosition));
        } else {
            background.setVisibility(View.GONE);
        }
        handler.sendEmptyMessageDelayed(0, relay);
    }

    /**
     * 设置轮播监听
     */
    public void setOnBannerListener(OnBannerListener onBannerListener) {
        this.onBannerListener = onBannerListener;
    }

    /**
     * ViewPager适配器内部类
     */
    class ViewPagerBanner extends PagerAdapter {

        /**
         * 页面总数
         *
         * @return
         */
        @Override
        public int getCount() {
            return 1000;
        }

        /**
         * 相当于getView方法
         *
         * @param container ViewPager本身
         * @param position  当前实例化页面位置
         * @return
         */
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            if (imageViews != null && imageViews.size() != 0) {
                ImageView imageView = imageViews.get(position % imageViews.size());
                /**
                 * 设置手指按下或滑动监听
                 */
                imageView.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                handler.removeCallbacksAndMessages(null);
                                break;
                            case MotionEvent.ACTION_MOVE:
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                break;
                            case MotionEvent.ACTION_UP:
                                handler.removeCallbacksAndMessages(null);
                                handler.sendEmptyMessageDelayed(0, relay);
                                break;
                        }
                        return false;
                    }
                });
                /**
                 * 设置点击监听
                 */
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBannerListener.onClick(position % imageViews.size());
                    }
                });
                container.addView(imageView);
                return imageView;
            } else {
                return super.instantiateItem(container, position);
            }
        }

        /**
         * 比较View和Object是否是同一个实例
         *
         * @param view   页面
         * @param object instantiateItem方法返回的结果
         * @return
         */
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        /**
         * 释放资源
         *
         * @param container ViewPager
         * @param position  要释放的位置
         * @param object    要释放的实例页面
         */
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((ImageView) object);
        }

    }

    /**
     * ViewPager监听内部类
     */
    class PageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 页面滚动时回调这个方法
         *
         * @param position             当前页面位置
         * @param positionOffset       滑动页面百分比
         * @param positionOffsetPixels 滑动像素
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 页面被选中时回调这个方法
         *
         * @param position 被选中位置
         */
        @Override
        public void onPageSelected(int position) {
            /**
             * 设置title
             */
            if (titles != null && titles.size() != 0 && (imageViews.size() == titles.size())) {
                title.setTextColor(titleColor);
                title.setText(titles.get(position % imageViews.size()));
            }
            /**
             * 设置point
             */
            if (imageViews != null && imageViews.size() != 0) {
                points.getChildAt(prePosition).setEnabled(false);
                points.getChildAt(position % imageViews.size()).setEnabled(true);
                prePosition = position % imageViews.size();
            }
        }

        /**
         * 页面滚动状态变化时回调这个方法
         * 静止 -》 滑动
         * 滑动 -》 静止
         * 静止 -》 拖拽
         *
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {
            /**
             * 拖拽、滑动、静止
             */
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                isDragging = true;
                handler.removeCallbacksAndMessages(null);
            } else if (state == ViewPager.SCROLL_STATE_SETTLING) {

            } else if (state == ViewPager.SCROLL_STATE_IDLE && isDragging) {
                isDragging = false;
                handler.removeCallbacksAndMessages(null);
                handler.sendEmptyMessageDelayed(0, relay);
            }
        }

    }

}

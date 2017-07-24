package com.dev.selectmultiimage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.dev.selectmultiimage.utils.DensityUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by yiw on 2016/1/6.
 */
public class ImagePagerActivity extends Activity {
    public static final String INTENT_IMGURLS = "imgurls";
    public static final String INTENT_POSITION = "position";
    public static final String INTENT_IMAGESIZE = "imagesize";

    private List<View> viewList = new ArrayList<View>();
    private LinearLayout llIndicator;
    public ImageSize imageSize;
    private int startPos;
    private ArrayList<String> imgUrls;


    public static void startImagePagerActivity(Context context, List<String> imgUrls, int position, ImageSize imageSize) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putStringArrayListExtra(INTENT_IMGURLS, new ArrayList<String>(imgUrls));
        intent.putExtra(INTENT_POSITION, position);
        intent.putExtra(INTENT_IMAGESIZE, imageSize);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        setContentView(R.layout.activity_imagepager);
        ViewPager vpDisplay = (ViewPager) findViewById(R.id.vp_display);
        llIndicator = (LinearLayout) findViewById(R.id.ll_indicator);

        getIntentData();

        ImageAdapter mAdapter = new ImageAdapter(this);
        mAdapter.setDatas(imgUrls);
        mAdapter.setImageSize(imageSize);
        vpDisplay.setAdapter(mAdapter);
        vpDisplay.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < viewList.size(); i++) {
                    viewList.get(i).setSelected(i == position ? true : false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vpDisplay.setCurrentItem(startPos);

        addIndicatorView(llIndicator, startPos, imgUrls);
    }

    public Activity getActivity() {
        return this;
    }

    private void getIntentData() {
        startPos = getIntent().getIntExtra(INTENT_POSITION, 0);
        imgUrls = getIntent().getStringArrayListExtra(INTENT_IMGURLS);
        imageSize = (ImageSize) getIntent().getSerializableExtra(INTENT_IMAGESIZE);
    }

    private void addIndicatorView(LinearLayout guideGroup, int startPos, ArrayList<String> imgUrls) {
        if (imgUrls != null && imgUrls.size() > 0) {
            viewList.clear();
            for (int i = 0; i < imgUrls.size(); i++) {
                View view = new View(this);
                view.setBackgroundResource(R.drawable.bg_indicator_selector);
                view.setSelected(i == startPos ? true : false);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(getActivity(), 6), DensityUtil.dip2px(getActivity(), 6));
                layoutParams.setMargins(10, 0, 0, 0);
                guideGroup.addView(view, layoutParams);
                viewList.add(view);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static class ImageAdapter extends PagerAdapter {

        private List<String> datas = new ArrayList<String>();
        private LayoutInflater inflater;
        private Context context;
        private ImageSize imageSize;
        private ImageView smallImageView = null;

        public void setDatas(List<String> datas) {
            if (datas != null)
                this.datas = datas;
        }

        public void setImageSize(ImageSize imageSize) {
            this.imageSize = imageSize;
        }

        public ImageAdapter(Context context) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (datas == null) return 0;
            return datas.size();
        }


        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = inflater.inflate(R.layout.item_pager_image, container, false);
            if (view != null) {
                final PhotoView imageView = (PhotoView) view.findViewById(R.id.image);

                if (imageSize != null) {
                    //预览imageView
                    smallImageView = new ImageView(context);
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(imageSize.getWidth(), imageSize.getHeight());
                    layoutParams.gravity = Gravity.CENTER;
                    smallImageView.setLayoutParams(layoutParams);
                    smallImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ((FrameLayout) view).addView(smallImageView);
                }

                //loading
                final ProgressBar loading = new ProgressBar(context);
                FrameLayout.LayoutParams loadingLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                loadingLayoutParams.gravity = Gravity.CENTER;
                loading.setLayoutParams(loadingLayoutParams);
                ((FrameLayout) view).addView(loading);

                final String imgurl = datas.get(position);

                Glide.with(context)
                        .load(imgurl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存多个尺寸
                        .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
                        .error(R.drawable.no_image)
                        .into(new GlideDrawableImageViewTarget(imageView) {
                            @Override
                            public void onLoadStarted(Drawable placeholder) {
                                super.onLoadStarted(placeholder);
                               /* if(smallImageView!=null){
                                    smallImageView.setVisibility(View.VISIBLE);
                                    Glide.with(context).load(imgurl).into(smallImageView);
                                }*/
                                loading.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                /*if(smallImageView!=null){
                                    smallImageView.setVisibility(View.GONE);
                                }*/
                                loading.setVisibility(View.GONE);
                            }

                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                                super.onResourceReady(resource, animation);
                                loading.setVisibility(View.GONE);
                                /*if(smallImageView!=null){
                                    smallImageView.setVisibility(View.GONE);
                                }*/
                            }
                        });
                imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(View view, float x, float y) {
                        ((Activity) context).finish();
                    }
                });
                container.addView(view, 0);
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }


    }

    public static class ImageSize implements Serializable {

        private int width;
        private int height;

        public ImageSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }

    @Override
    protected void onDestroy() {
        viewList.clear();
        super.onDestroy();
    }
}

package com.dev.superframeapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.dev.superframe.base.BaseActivity;
import com.dev.superframe.view.ptr.PtrClassicFrameLayout;
import com.dev.superframe.view.ptr.PtrDefaultHandler2;
import com.dev.superframe.view.ptr.PtrFrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2017/7/20.
 */

public class PullToRefreshActivity extends BaseActivity {

    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.pfl_display)
    PtrClassicFrameLayout pflDisplay;

    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulltorefresh);
        ButterKnife.bind(this);

        initView();
        initData();
        initEvent();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initView() {
        pflDisplay.setLastUpdateTimeRelateObject(this);
        pflDisplay.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                pflDisplay.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        pflDisplay.refreshComplete();
                        tvCount.setText(page + "");
                    }
                }, 1000);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pflDisplay.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        pflDisplay.refreshComplete();
                        tvCount.setText(page + "");
                    }
                }, 1000);
            }
        });

        // the following are default settings
//        pflDisplay.setResistance(1.7f); // you can also set foot and header separately
//        pflDisplay.setRatioOfHeaderHeightToRefresh(1.2f);
        //pflDisplay.setDurationToClose(1000);  // you can also set foot and header separately
        // default is false
        pflDisplay.setPullToRefresh(false);

        // default is true
//        pflDisplay.setKeepHeaderWhenRefresh(true);
        pflDisplay.postDelayed(new Runnable() {
            @Override
            public void run() {
                pflDisplay.autoRefresh();
            }
        }, 100);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}

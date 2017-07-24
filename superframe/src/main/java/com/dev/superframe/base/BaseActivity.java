package com.dev.superframe.base;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.superframe.R;
import com.dev.superframe.base.presenter.ActivityPresenter;
import com.dev.superframe.manger.ThreadManager;
import com.dev.superframe.ui.dialog.LoadingDialog;
import com.dev.superframe.utils.StringUtil;
import com.dev.superframe.utils.SystemBarTintUtil;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * 基础android.support.v4.app.FragmentActivity，通过继承可获取或使用 里面创建的 组件 和 方法
 * *onFling内控制左右滑动手势操作范围，可自定义
 *
 * @author Lemon
 * @use extends BaseActivity, 具体参考 .DemoActivity 和 .DemoFragmentActivity
 * @see ActivityPresenter#getActivity
 * @see #context
 * @see #view
 * @see #fragmentManager
 * @see #setContentView
 * @see #runUiThread
 * @see #runThread
 * @see #onDestroy
 */
public abstract class BaseActivity extends SwipeBackActivity implements ActivityPresenter {
    private static final String TAG = "BaseActivity";

    /**
     * 该Activity实例，命名为context是因为大部分方法都只需要context，写成context使用更方便
     *
     * @warn 不能在子类中创建
     */
    protected BaseActivity context = null;
    /**
     * 该Activity的界面，即contentView
     *
     * @warn 不能在子类中创建
     */
    protected View view = null;
    /**
     * 布局解释器
     *
     * @warn 不能在子类中创建
     */
    protected LayoutInflater inflater = null;
    /**
     * Fragment管理器
     *
     * @warn 不能在子类中创建
     */
    protected FragmentManager fragmentManager = null;

    private boolean isAlive = false;
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

        context = (BaseActivity) getActivity();
        isAlive = true;
        fragmentManager = getSupportFragmentManager();

        inflater = getLayoutInflater();

        threadNameList = new ArrayList<String>();
    }

    /**
     * activity的默认标题TextView，layout.xml中用@id/tvBaseTitle绑定。子Activity内调用autoSetTitle方法 会优先使用INTENT_TITLE
     *
     * @warn 如果子Activity的layout中没有android:id="@id/tvBaseTitle"的TextView，使用tvBaseTitle前必须在子Activity中赋值
     * @see #autoSetTitle
     */
    @Nullable
    protected TextView tvBaseTitle;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        SystemBarTintUtil.setSystemBarTint(getActivity(), R.color.theme_color);
        tvBaseTitle = (TextView) findViewById(R.id.tv_base_title);//绑定默认标题TextView
    }

    /**
     * 用于 打开activity以及activity之间的通讯（传值）等；一些通讯相关基本操作（打电话、发短信等）
     */
    protected Intent intent = null;

    /**
     * 退出时之前的界面进入动画,可在finish();前通过改变它的值来改变动画效果
     */
    protected int enterAnim = R.anim.fade;

    /**
     * 退出时该界面动画,可在finish();前通过改变它的值来改变动画效果
     */
    protected int exitAnim = R.anim.right_push_out;

    /**
     * 通过id查找并获取控件，并setOnClickListener
     *
     * @param id
     * @param l
     * @return
     */
    @SuppressWarnings("unchecked")
    public <V extends View> V findViewById(int id, OnClickListener l) {
        V v = (V) findViewById(id);
        v.setOnClickListener(l);
        return v;
    }

    //自动设置标题方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * 自动把标题设置为上个Activity传入的INTENT_TITLE，建议在子类initView中使用
     * *这个方法没有return，tvTitle = tvBaseTitle，直接用tvBaseTitle
     *
     * @must 在UI线程中调用
     */
    protected void autoSetTitle() {
        autoSetTitle(tvBaseTitle);
    }

    /**
     * 自动把标题设置为上个Activity传入的INTENT_TITLE，建议在子类initView中使用
     *
     * @param tvTitle
     * @return tvTitle 返回tvTitle是为了可以写成一行，如 tvTitle = autoSetTitle((TextView) findViewById(titleResId));
     * @must 在UI线程中调用
     */
    protected TextView autoSetTitle(TextView tvTitle) {
        if (tvTitle != null && StringUtil.isNotEmpty(getIntent().getStringExtra(INTENT_TITLE), false)) {
            tvTitle.setText(StringUtil.getCurrentString());
        }
        return tvTitle;
    }

    /**
     * 自动把标题设置为上个Activity传入的INTENT_TITLE，建议在子类initView中使用
     *
     * @param title
     * @return tvTitle 返回tvTitle是为了可以写成一行，如 tvTitle = autoSetTitle((TextView) findViewById(titleResId));
     * @must 在UI线程中调用
     */
    protected void autoSetTitle(String title) {
        if (tvBaseTitle != null) {
            tvBaseTitle.setText(StringUtil.getString(title));
        }
    }

    //自动设置标题方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //显示与关闭进度弹窗方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * 展示加载进度条,无标题
     *
     * @param stringResId
     */
    public void showProgressDialog(int stringResId) {
        try {
            showProgressDialog(context.getResources().getString(stringResId));
        } catch (Exception e) {
            Log.e(TAG, "showProgressDialog  showProgressDialog(null, context.getResources().getString(stringResId));");
        }
    }

    /**
     * 展示加载进度条,无标题
     *
     * @param message
     */
    public void showProgressDialog(final String message) {
        runUiThread(new Runnable() {
            @Override
            public void run() {
                if (LoadingDialog.mLoadingDialog.isShowing() == true) {
                    LoadingDialog.cancelDialogForLoading();
                }
                if (StringUtil.isNotEmpty(message, false)) {
                    LoadingDialog.showDialogForLoading(getActivity(), message, true);
                }
            }
        });
    }

    /**
     * 隐藏加载进度
     */
    public void dismissProgressDialog() {
        runUiThread(new Runnable() {
            @Override
            public void run() {
                if (LoadingDialog.mLoadingDialog != null && LoadingDialog.mLoadingDialog.isShowing() == true) {
                    LoadingDialog.cancelDialogForLoading();
                }
            }
        });
    }
    //显示与关闭进度弹窗方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //启动新Activity方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * 打开新的Activity，向左滑入效果
     *
     * @param intent
     */
    public void toActivity(Intent intent) {
        toActivity(intent, true);
    }

    /**
     * 打开新的Activity
     *
     * @param intent
     * @param showAnimation
     */
    public void toActivity(Intent intent, boolean showAnimation) {
        toActivity(intent, -1, showAnimation);
    }

    /**
     * 打开新的Activity，向左滑入效果
     *
     * @param intent
     * @param requestCode
     */
    public void toActivity(Intent intent, int requestCode) {
        toActivity(intent, requestCode, true);
    }

    /**
     * 打开新的Activity
     *
     * @param intent
     * @param requestCode
     * @param showAnimation
     */
    public void toActivity(final Intent intent, final int requestCode, final boolean showAnimation) {
        runUiThread(new Runnable() {
            @Override
            public void run() {
                if (intent == null) {
                    Log.w(TAG, "toActivity  intent == null >> return;");
                    return;
                }
                //fragment中使用context.startActivity会导致在fragment中不能正常接收onActivityResult
                if (requestCode < 0) {
                    startActivity(intent);
                } else {
                    startActivityForResult(intent, requestCode);
                }
                if (showAnimation) {
                    overridePendingTransition(R.anim.right_push_in, R.anim.hold);
                } else {
                    overridePendingTransition(R.anim.null_anim, R.anim.null_anim);
                }
            }
        });
    }
    //启动新Activity方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //获取Intent传值方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public String getIntentText(String key) {
        return getIntent().getExtras() != null && getIntent().getExtras().containsKey(key) ? getIntent().getExtras().getString(key) : "";
    }
    //获取Intent传值方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //获取View方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public View getView(int viewId) {
        View view = LayoutInflater.from(getActivity()).inflate(viewId, null);
        return view;
    }
    //获取View方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //show short toast 方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * 快捷显示short toast方法，需要long toast就用 Toast.makeText(string, Toast.LENGTH_LONG).show(); ---不常用所以这个类里不写
     *
     * @param stringResId
     */
    public void showShortToast(int stringResId) {
        try {
            showShortToast(context.getResources().getString(stringResId));
        } catch (Exception e) {
            Log.e(TAG, "showShortToast  context.getResources().getString(resId)" +
                    " >>  catch (Exception e) {" + e.getMessage());
        }
    }

    /**
     * 快捷显示short toast方法，需要long toast就用 Toast.makeText(string, Toast.LENGTH_LONG).show(); ---不常用所以这个类里不写
     *
     * @param string
     */
    public void showShortToast(String string) {
        showShortToast(string, false);
    }

    /**
     * 快捷显示short toast方法，需要long toast就用 Toast.makeText(string, Toast.LENGTH_LONG).show(); ---不常用所以这个类里不写
     *
     * @param string
     * @param isForceDismissProgressDialog
     */
    public void showShortToast(final String string, final boolean isForceDismissProgressDialog) {
        runUiThread(new Runnable() {
            @Override
            public void run() {
                if (isForceDismissProgressDialog) {
                    dismissProgressDialog();
                }
                Toast.makeText(context, "" + string, Toast.LENGTH_SHORT).show();
            }
        });
    }
    //show short toast 方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //运行线程 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * 在UI线程中运行，建议用这个方法代替runOnUiThread
     *
     * @param action
     */
    public final void runUiThread(Runnable action) {
        if (isAlive() == false) {
            Log.w(TAG, "runUiThread  isAlive() == false >> return;");
            return;
        }
        runOnUiThread(action);
    }

    /**
     * 线程名列表
     */
    protected List<String> threadNameList;

    /**
     * 运行线程
     *
     * @param name
     * @param runnable
     * @return
     */
    public final Handler runThread(String name, Runnable runnable) {
        if (isAlive() == false) {
            Log.w(TAG, "runThread  isAlive() == false >> return null;");
            return null;
        }
        name = StringUtil.getTrimedString(name);
        Handler handler = ThreadManager.getInstance().runThread(name, runnable);
        if (handler == null) {
            Log.e(TAG, "runThread handler == null >> return null;");
            return null;
        }

        if (threadNameList.contains(name) == false) {
            threadNameList.add(name);
        }
        return handler;
    }

    //运行线程 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //Activity的返回按钮和底部弹窗的取消按钮几乎是必备，正好原生支持反射；而其它比如Fragment极少用到，也不支持反射<<<<<<<<<

    /**
     * 返回按钮被点击，默认处理是onBottomDragListener.onDragBottom(false)，重写可自定义事件处理
     *
     * @param v
     * @use layout.xml中的组件添加android:onClick="onReturnClick"即可
     * @warn 只能在Activity对应的contentView layout中使用；
     * *给对应View setOnClickListener会导致android:onClick="onReturnClick"失效
     */
    @Override
    public void onReturnClick(View v) {
        Log.d(TAG, "onReturnClick >>>");
        onBackPressed();//会从最外层子类调finish();BaseBottomWindow就是示例
    }

    /**
     * 前进按钮被点击，默认处理是onBottomDragListener.onDragBottom(true)，重写可自定义事件处理
     *
     * @param v
     * @use layout.xml中的组件添加android:onClick="onForwardClick"即可
     * @warn 只能在Activity对应的contentView layout中使用；
     * *给对应View setOnClickListener会导致android:onClick="onForwardClick"失效
     */
    @Override
    public void onForwardClick(View v) {
        Log.d(TAG, "onForwardClick >>>");
    }
    //Activity常用导航栏右边按钮，而且底部弹窗BottomWindow的确定按钮是必备；而其它比如Fragment极少用到，也不支持反射>>>>>


    @Override
    public final boolean isAlive() {
        return isAlive && context != null;// & ! isFinishing();导致finish，onDestroy内runUiThread不可用
    }

    @Override
    public final boolean isRunning() {
        return isRunning & isAlive();
    }

    @Override
    public void finish() {
        super.finish();//必须写在最前才能显示自定义动画
        runUiThread(new Runnable() {
            @Override
            public void run() {
                if (enterAnim > 0 && exitAnim > 0) {
                    try {
                        overridePendingTransition(enterAnim, exitAnim);
                    } catch (Exception e) {
                        Log.e(TAG, "finish overridePendingTransition(enterAnim, exitAnim);" +
                                " >> catch (Exception e) {  " + e.getMessage());
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "\n onResume <<<<<<<<<<<<<<<<<<<<<<<");
        super.onResume();
        isRunning = true;
        Log.d(TAG, "onResume >>>>>>>>>>>>>>>>>>>>>>>>\n");
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "\n onPause <<<<<<<<<<<<<<<<<<<<<<<");
        super.onPause();
        isRunning = false;
        Log.d(TAG, "onPause >>>>>>>>>>>>>>>>>>>>>>>>\n");
    }

    /**
     * 销毁并回收内存
     *
     * @warn 子类如果要使用这个方法内用到的变量，应重写onDestroy方法并在super.onDestroy();前操作
     */
    @Override
    protected void onDestroy() {
        Log.d(TAG, "\n onDestroy <<<<<<<<<<<<<<<<<<<<<<<");
        dismissProgressDialog();
        ThreadManager.getInstance().destroyThread(threadNameList);
        if (view != null) {
            try {
                view.destroyDrawingCache();
            } catch (Exception e) {
                Log.w(TAG, "onDestroy  try { view.destroyDrawingCache();" +
                        " >> } catch (Exception e) {\n" + e.getMessage());
            }
        }

        isAlive = false;
        isRunning = false;
        super.onDestroy();

        inflater = null;
        view = null;
        tvBaseTitle = null;

        fragmentManager = null;
        threadNameList = null;

        intent = null;
        context = null;
        Log.d(TAG, "onDestroy >>>>>>>>>>>>>>>>>>>>>>>>\n");
    }


    //手机返回键和菜单键实现同点击标题栏左右按钮效果<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    private boolean isOnKeyLongPress = false;

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        isOnKeyLongPress = true;
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (isOnKeyLongPress) {
            isOnKeyLongPress = false;
            return true;
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                break;
            case KeyEvent.KEYCODE_MENU:
                break;
            default:
                break;
        }

        return super.onKeyUp(keyCode, event);
    }
    //手机返回键和菜单键实现同点击标题栏左右按钮效果>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}
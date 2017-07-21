package com.dev.superframe.base.presenter;

/**
 * Activity和Fragment的公共逻辑接口
 *
 * @author Lemon
 * @use Activity或Fragment implements Presenter
 */
public interface Presenter {

    static final String INTENT_TITLE = "INTENT_TITLE";
    static final String INTENT_CONTENT = "INTENT_CONTENT";
    static final String INTENT_TIME = "INTENT_TIME";
    static final String INTENT_URL = "INTENT_URL";
    static final String INTENT_TYPE = "INTENT_TYPE";

    static final String INTENT_ID = "INTENT_ID";
    static final String INTENT_IDS = "INTENT_IDS";
    static final String INTENT_DATA = "INTENT_DATA";

    static final String RESULT_DATA = "RESULT_DATA";
    static final String RESULT_CONTENT = "RESULT_CONTENT";
    static final String RESULT_ID = "RESULT_ID";

    /**
     * UI显示方法(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)
     *
     * @must Activity-在子类onCreate方法内初始化View(setContentView)后调用；Fragment-在子类onCreateView方法内初始化View后调用
     */
    void initView();

    /**
     * Data数据方法(存在数据获取或处理代码，但不存在事件监听代码)
     *
     * @must Activity-在子类onCreate方法内初始化View(setContentView)后调用；Fragment-在子类onCreateView方法内初始化View后调用
     */
    void initData();

    /**
     * Event事件方法(只要存在事件监听代码就是)
     *
     * @must Activity-在子类onCreate方法内初始化View(setContentView)后调用；Fragment-在子类onCreateView方法内初始化View后调用
     */
    void initEvent();


    /**
     * 是否存活(已启动且未被销毁)
     */
    boolean isAlive();

    /**
     * 是否在运行
     */
    boolean isRunning();
}
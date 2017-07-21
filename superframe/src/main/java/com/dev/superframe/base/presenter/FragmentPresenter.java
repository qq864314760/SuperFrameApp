package com.dev.superframe.base.presenter;

import android.app.Activity;

/**Fragment的逻辑接口
 * @author Lemon
 * @use implements FragmentPresenter
 * @warn 对象必须是Fragment
 */
public interface FragmentPresenter extends Presenter {

	/**
	 * 该Fragment在Activity添加的所有Fragment中的位置
	 */
	static final String ARGUMENT_POSITION = "ARGUMENT_POSITION";
	
	static final int RESULT_OK = Activity.RESULT_OK;
	static final int RESULT_CANCELED = Activity.RESULT_CANCELED;
}
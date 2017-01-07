package com.yiyekeji.coolschool.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.zhy.autolayout.AutoRelativeLayout;

/**定制一个正方形
 * Created by lxl on 2016/08/13.
 */
public class SquareLayout extends AutoRelativeLayout {
	public SquareLayout(Context context) {
		super(context);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public SquareLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public SquareLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public SquareLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}
}

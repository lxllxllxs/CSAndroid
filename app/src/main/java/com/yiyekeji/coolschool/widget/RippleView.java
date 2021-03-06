package com.yiyekeji.coolschool.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.yiyekeji.coolschool.R;
import com.zhy.autolayout.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 模仿咻一咻
 *
 * @author LGL
 *
 */
public class RippleView extends View {

	private Paint paint;
	private float maxWidth = 255;
	// 是否运行
	private boolean isStarting = false;
	private ArrayList<Integer> alphaList = new ArrayList<Integer>();
	private List<Float> startWidthList = new ArrayList<Float>();
	private Context context;
	private  float startWide;
	private float scale;

	public RippleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context=context;
		init();

	}

	public RippleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		init();
	}

	public RippleView(Context context) {
		super(context);
		this.context=context;
		init();
	}

	private void init() {
		paint = new Paint();
		// 设置博文的颜色
		paint.setColor(ContextCompat.getColor(context, R.color.theme_red));
		paint.setAntiAlias(true);
		alphaList.add(255);// 圆心的不透明度
		startWidthList.add((float) 0);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int[] size= ScreenUtils.getScreenSize(context,false);
		startWide=size[0]/10;
		maxWidth = size[0]/2;
		scale=(maxWidth-startWide)/255;
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (isStarting) {
			dynamic(canvas);
		} else {
			drawCircle(canvas);
		}

	}

	private void drawCircle(Canvas canvas){
		setBackgroundColor(Color.TRANSPARENT);// 颜色：完全透明
		paint.setAlpha(255);
		// 这个半径决定你想要多大的扩散面积
		canvas.drawCircle(getWidth() / 2, getHeight() / 2, startWide,
				paint);
	}

	/**
	 * 波纹动画
	 * @param canvas
     */
	private void dynamic(Canvas canvas) {
		setBackgroundColor(Color.TRANSPARENT);// 颜色：完全透明
		// 依次绘制 同心圆
		for (int i = 0; i < alphaList.size(); i++) {
			int alpha = alphaList.get(i);
			// 圆半径
			float startWidth = startWidthList.get(i);
			paint.setAlpha(alpha);
			// 这个半径决定你想要多大的扩散面积
			canvas.drawCircle(getWidth() / 2, getHeight() / 2, startWidth + startWide,
					paint);
			// 同心圆扩散
			if (isStarting && alpha > 0 && startWidth < maxWidth) {
				alphaList.set(i, alpha - 1);
				startWidthList.set(i, startWidth + 1);
			}
		}
		if (isStarting
				&&startWidthList.get(startWidthList.size() - 1)== maxWidth / 5) {
			alphaList.add(255);
			startWidthList.add((float)0);
		}
		// 同心圆数量达到10个，删除最外层圆
		if (isStarting && startWidthList.size() == 10) {
			startWidthList.remove(0);
			alphaList.remove(0);
		}
		// 刷新界面
		invalidate();
	}

	// 执行动画
	public void start() {
		isStarting = true;
		invalidate();
	}

	// 停止动画
	public void stop() {
		isStarting = false;
		invalidate();
	}

	// 判断是都在不在执行
	public boolean isStarting() {
		return isStarting;
	}

}

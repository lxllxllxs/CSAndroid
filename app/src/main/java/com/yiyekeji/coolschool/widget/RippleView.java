package com.yiyekeji.coolschool.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 模仿咻一咻
 * 
 * @author LGL
 *
 */
public class RippleView extends View {
	int screenHeight,screenWeight;
	float width;
	float height;
	private Paint paint;
	private int maxWidth = 255;
	// 是否运行
	private boolean isStarting = false;
	private List<String> alphaList = new ArrayList<String>();
	private List<String> startWidthList = new ArrayList<String>();
	private Context context;
	private Timer mTimer = new Timer();
	private TimerTask timerTask;

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

		alphaList.add("255");// 圆心的不透明度
		startWidthList.add("0");

		timerTask=new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
				LogUtil.d(System.currentTimeMillis()+"");
			}
		};
		mTimer.schedule(timerTask,1000,300);
	}

	int count=0;
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==0){
				count++;
				if (count==5) {
					alpha = alpha - 30;
					if (alpha<0){
						alpha=255;
					}
					count=0;
				} else {
					alpha--;
				}
				startWidth=startWidth+20;
				LogUtil.d(alpha+"");
				paint.setAlpha(alpha);
				/*if (alpha==0){
					alpha=255;
				}*/
				invalidate();
			}
		}
	};

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	int alpha=255;
	float startWidth=20;
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawCircle(getWidth() / 2, getHeight() / 2, startWidth, paint);
		/*// 依次绘制 同心圆
		for (int i = 0; i < alphaList.size(); i++) {
			int alpha = Integer.parseInt(alphaList.get(i));
			// 圆半径
			int startWidth = Integer.parseInt(startWidthList.get(i));
			paint.setAlpha(alpha);
			// 这个半径决定你想要多大的扩散面积
			canvas.drawCircle(getWidth() / 2, getHeight() / 2, startWidth + 100, paint);
			// 同心圆扩散
			if (isStarting && alpha > 0 && startWidth < getWidth()) {
				alphaList.set(i, (alpha - 1) + "");
				startWidthList.set(i, (startWidth + 1) + "");
			}
			LogUtil.d("onDraw",i+"");
		}
		if (isStarting && Integer
						.parseInt(startWidthList.get(startWidthList.size() - 1)) == maxWidth / 1) {
			alphaList.add("255");
			startWidthList.add("0");
		}
		// 同心圆数量达到10个，删除最外层圆
		if (isStarting && startWidthList.size() == 10) {
			startWidthList.remove(0);
			alphaList.remove(0);
		}
		// 刷新界面
		invalidate();*/
	}

	// 执行动画
	public void start() {
		isStarting = true;
	}

	// 停止动画
	public void stop() {
//		isStarting = false;
		mTimer.cancel();
	}

	// 判断是都在不在执行
	public boolean isStarting() {
		return isStarting;
	}


	private float measureH(int heightMeasureSpec) {
		int mode = MeasureSpec.getMode(heightMeasureSpec);
		int size = MeasureSpec.getSize(heightMeasureSpec);
		if (mode == MeasureSpec.AT_MOST) {
			return screenHeight;
		} else if (mode == MeasureSpec.EXACTLY) {
			return size;
		}else {
			return  size;
		}
	}
	private float measureW(int widthMeasureSpec) {
		int mode = MeasureSpec.getMode(widthMeasureSpec);
		int size = MeasureSpec.getSize(widthMeasureSpec);
		if (mode == MeasureSpec.AT_MOST) {
			return screenWeight;
		} else if (mode == MeasureSpec.EXACTLY) {
			return size;
		}else {
			return  size;
		}
	}

}

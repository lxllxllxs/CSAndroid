package com.yiyekeji.coolschool.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.zhy.autolayout.AutoRelativeLayout;


/**
 * 自定义字体的textview
 * ① 在xml中使用前在父布局加上xmlns:ftv="http://schemas.android.com/apk/res/com.yiyekeji.pay"，
 *   即可在控件属性中使用ftv:font="XXX"，不使用该属性，默认用兰亭，lantingBold为兰亭粗
 * ② 假如添加新字体，先在attrs.xml中找到此控件，在font下添加新的enum，然后在assets从加入相对应的ttf文件，
 *   最后在这个类的setFont方法中添加if判断语句和补全path即可  
 * */
public class CButton extends AutoRelativeLayout {
	private Context context;
	public CButton(Context context) {
		this(context, null);
		this.context=context;
	}

	public CButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		initView(attrs);
	}

	private TextView tv;
	private void initView(AttributeSet attrs) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CButton);

		LayoutInflater.from(context).inflate(R.layout.layout_font_text_view, this);
		tv=(TextView)findViewById(R.id.fontTextView);
		for(int i=0; i<typedArray.getIndexCount(); i++) {
			int attr = typedArray.getIndex(i);
			switch(attr) {
				case R.styleable.CButton_cb_text: {
					tv.setText(typedArray.getText(0));
					break;
				}
				case R.styleable.CButton_cb_color: {
					tv.setBackgroundColor(typedArray.getColor(0,0));
					break;
				}
			}
		}
		typedArray.recycle();
	}


}

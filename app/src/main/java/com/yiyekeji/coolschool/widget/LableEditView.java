package com.yiyekeji.coolschool.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.zhy.autolayout.AutoRelativeLayout;

/**
 * Created by lxl on 2016/9/20.
 */
public class LableEditView extends AutoRelativeLayout {
    private Context context;
    private String mEditText,mText;
    private float textSize;
    TextView textView;
    EditText editText;
    private  int line=1;
    private Boolean isDigits = false;



    public LableEditView(Context context) {
        super(context);
    }

    public LableEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        initView(attrs);
    }

    public LableEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        initView(attrs);
    }


    public void initView(AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LableEditView);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.LableEditView_edtText:
                    mEditText = a.getString(attr);
                    break;
                case R.styleable.LableEditView_text:
                    mText = a.getString(attr);
                    break;
                case R.styleable.LableEditView_textSize:
                    textSize =a.getDimension(attr,25);
                    break;
                case R.styleable.LableEditView_line:
                    line =a.getInteger(attr,1);
                    break;
                case R.styleable.LableEditView_receiveDigits:
            //        line =a.getInteger(attr,1);
                    isDigits = a.getBoolean(attr,false);
                    break;
            }

        }
        a.recycle();

        LayoutInflater.from(context).inflate(R.layout.layout_lable_edit_view, this);
        textView=(TextView) findViewById(R.id.tv_lable);
        textView.setText(mText);
        textView.setTextColor(ContextCompat.getColor(context,R.color.black));

        editText=(EditText)findViewById(R.id.edt_content);
        editText.setHint(mEditText);
        editText.setLines(line);
        if(isDigits){
            editText.setKeyListener(new NumberKeyListener() {
                @Override
                protected char[] getAcceptedChars() {
                    return new char[]{ '1', '2', '3', '4', '5', '6', '7', '8','9', '0'};
                }

                @Override
                public int getInputType() {
                    return InputType.TYPE_CLASS_PHONE;
                }
            });
        }

    }

    public void setEditText(String text){
        editText.setText(text);
    }

    public String getEditText(){
        return editText.getText().toString();
    }

    public void setEditTextEnable(boolean istrue){
        editText.setEnabled(istrue);
    }
}

package com.yiyekeji.coolschool.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
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

        LayoutInflater.from(context).inflate(R.layout.layout_lable_edit_view, this);
        editText=(EditText)findViewById(R.id.edt_content);
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
                case R.styleable.LableEditView_isPwd:
                    if (a.getBoolean(attr,false)){
                        editText.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框
                    }
                    break;
            }

        }
        a.recycle();

        textView=(TextView) findViewById(R.id.tv_lable);
        textView.setText(mText);

        editText.setHint(mEditText);
        editText.setLines(line);

    }

    public void addTextChangedListener(TextWatcher textWatcher){
        editText.addTextChangedListener(textWatcher);
    }

    public void setLabelText(String text){
        textView.setText(text);
    }

    public EditText getEdit(){
        return editText;
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

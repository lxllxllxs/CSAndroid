package com.yiyekeji.coolschool.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.yiyekeji.coolschool.R;
import com.zhy.autolayout.AutoRelativeLayout;


/**
 * 购买 加入购物车弹窗 产品信息
 * Created by lxl on 2016/9/7.
 */
public class CountView extends AutoRelativeLayout {

    ImageView ivReduce;
    EditText edtBuyNum;
    ImageView ivAdd;
    int count=0;
    private Context context;
    int total;
    public CountView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public CountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    public void setTotalGoods(int totalGoods){
        count=0;
        edtBuyNum.setText(count+"");
        this.total=totalGoods;
    }

    public CountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    /**
     * 传activity设置标题
     */
    public void initView() {
        LayoutInflater.from(context).inflate(R.layout.layout_product_detail_popwindow_count, this);
        edtBuyNum=(EditText)findViewById(R.id.edt_buy_number);
        ivAdd=(ImageView)findViewById(R.id.iv_add);
        ivReduce=(ImageView)findViewById(R.id.iv_reduce);

        ivAdd.setOnClickListener(ck);
        ivReduce.setOnClickListener(ck);
        edtBuyNum.addTextChangedListener(new TextWatcher() {
            int deletePostion;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            /**
             * 只能为正整数，不能大于库存量
             * 在xml中对editview 指定输入类型
             * @param s
             */
            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())){
                    count=0;
                    edtBuyNum.setText(count+"");
                    return;
                }
                int buynum=Integer.valueOf(s.toString());
                if (buynum>total){
                    Toast.makeText(context, "没有足够库存！", Toast.LENGTH_SHORT).show();
                    count=total;
                    edtBuyNum.setText(count+"");
                    return;
                }
            }
        });

    }

    private OnClickListener ck=new OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.iv_add:
                    count++;
                    //判断购买量的合法性
                    if (count>total){
                        count--;
                        return;
                    }
                    edtBuyNum.setText(count+"");
                    break;
                case R.id.iv_reduce:
                    count--;
                    if (count<0){
                        count++;
                        return;
                    }
                    edtBuyNum.setText(count+"");
                    break;
            }

        }
    };

}

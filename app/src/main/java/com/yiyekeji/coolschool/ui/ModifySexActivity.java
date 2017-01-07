package com.yiyekeji.coolschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.widget.TitleBar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by lxl on 2017/1/7.
 */
public class ModifySexActivity extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.tv_male)
    TextView tvMale;
    @InjectView(R.id.iv_male)
    ImageView ivMale;
    @InjectView(R.id.tv_female)
    TextView tvFemale;
    @InjectView(R.id.iv_female)
    ImageView ivFemale;
    @InjectView(R.id.tv_secret)
    TextView tvSecret;
    @InjectView(R.id.iv_secret)
    ImageView ivSecret;

    private int sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_sex);
        ButterKnife.inject(this);
        sex=getIntent().getIntExtra("sex",2);
        initView();
    }

    private void initView() {
        switch (sex){
            case 0:
                setMale();
                break;
            case 1:
                setFemale();
                break;
            case 2:
                setSecret();
                break;
        }

    }

    @OnClick({R.id.tv_male, R.id.tv_female, R.id.tv_secret})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_male:
                setMale();
                sex=1;
                break;
            case R.id.tv_female:
                setFemale();
                sex=0;
                break;
            case R.id.tv_secret:
                setSecret();
                sex=2;
                break;
        }
        Intent intent1 = new Intent();
        intent1.putExtra("sex", sex);
        setResult(RESULT_OK,intent1);
    }

    private void setSecret(){
        ivMale.setVisibility(View.INVISIBLE);
        ivFemale.setVisibility(View.INVISIBLE);
        ivSecret.setVisibility(View.VISIBLE);
    }

    private void setMale(){
        ivFemale.setVisibility(View.INVISIBLE);
        ivSecret.setVisibility(View.INVISIBLE);
        ivMale.setVisibility(View.VISIBLE);
    }

    private void setFemale(){
        ivMale.setVisibility(View.INVISIBLE);
        ivSecret.setVisibility(View.INVISIBLE);
        ivFemale.setVisibility(View.VISIBLE);
    }

}

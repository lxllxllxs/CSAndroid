package com.yiyekeji.coolschool.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.TuCao;
import com.yiyekeji.coolschool.inter.RollCallService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GlideUtil;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lxl on 2017/2/18.
 */
public class TuCaoDetailAty extends BaseActivity {

    TuCao tuCao;
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.tv_postMan)
    TextView tvPostMan;
    @InjectView(R.id.iv_sex)
    ImageView ivSex;
    @InjectView(R.id.tv_date)
    TextView tvDate;
    @InjectView(R.id.tv_content)
    TextView tvContent;
    @InjectView(R.id.iv_detail)
    ImageView ivDetail;
    @InjectView(R.id.tv_viewCount)
    TextView tvViewCount;
    @InjectView(R.id.tv_commentCount)
    TextView tvCommentCount;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tucao_detail);
        ButterKnife.inject(this);
        initData();
        initView();
    }

    private void initView() {
        titleBar.initView(this);

        if (!TextUtils.isEmpty(tuCao.getImgUrl())) {
            ivDetail.setVisibility(View.VISIBLE);
            GlideUtil.setImageToView(tuCao.getImgUrl(), ivDetail);
        }
        tvCommentCount.setText(tuCao.getCommentCount());
        tvContent.setText(tuCao.getContent());
        tvDate.setText(tuCao.getDate().substring(5,16));
        tvPostMan.setText(tuCao.getAuthor());
        if (tuCao.getSex().equals("1")) {
            ivSex.setImageResource(R.mipmap.ic_man);
        } else {
            ivSex.setImageResource(R.mipmap.ic_female);
        }
    }

    private void initData() {
        tuCao = getIntent().getParcelableExtra("tuCao");
    }


    private void addCourse() {
        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", App.geTokenId());
        params.put("userNum", App.userInfo.getUserNum());
        RollCallService service = RetrofitUtil.create(RollCallService.class);
        Call<ResponseBody> call = service.insertCourse(params);
        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissDialog();
                String jsonString = GsonUtil.toJsonString(response);
                if (response.code() != 200) {
                    showShortToast("网络错误" + response.code());
                    return;
                }
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    showShortToast(rb.getMessage());
                    finish();
                } else {
                    showShortToast(rb.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissDialog();
                showShortToast(getString(R.string.response_err));
            }
        });
    }
}

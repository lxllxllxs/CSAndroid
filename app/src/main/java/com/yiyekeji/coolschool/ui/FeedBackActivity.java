package com.yiyekeji.coolschool.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.CommonService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lxl on 2017/2/4.
 */
public class FeedBackActivity extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.edt_feedback)
    EditText edtFeedback;
    @InjectView(R.id.tv_confirm)
    TextView tvConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.inject(this);
        initView();
}

    private void initView() {
        titleBar.initView(this);
    }
    private void commitFeedBack() {
        if (TextUtils.isEmpty(edtFeedback.getText().toString())) {
            showShortToast("内容不能为空！");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        String userNum = App.userInfo == null ? "0000000000" : App.userInfo.getUserNum();
        params.put("userNum", userNum);
        params.put("content", edtFeedback.getText().toString());
        CommonService service = RetrofitUtil.create(CommonService.class);
        Call<ResponseBody> call = service.commitFeedBack(params);
        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getLoadDialog().dismiss();
                String jsonString = GsonUtil.toJsonString(response);
                if (response.code() != 200) {
                    showShortToast("网络错误" + response.code());
                    return;
                }
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    showShortToast("提交成功，谢谢你的反馈！");
                    finish();
                } else {
                    showShortToast(rb.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getLoadDialog().dismiss();
                showShortToast(getString(R.string.response_err));
            }
        });
    }
    @OnClick(R.id.tv_confirm)
    public void onClick() {
        commitFeedBack();
    }
}

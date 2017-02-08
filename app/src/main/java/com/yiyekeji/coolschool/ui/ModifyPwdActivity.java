package com.yiyekeji.coolschool.ui;

import android.os.Bundle;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.UserService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.CButton;
import com.yiyekeji.coolschool.widget.LableEditView;
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
 * Created by lxl on 2017/1/7.
 */
public class ModifyPwdActivity extends BaseActivity {

    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.ledt_oldPwd)
    LableEditView ledtOldPwd;
    @InjectView(R.id.ledt_newPwd)
    LableEditView ledtNewPwd;
    @InjectView(R.id.ledt_reaptPwd)
    LableEditView ledtReaptPwd;
    @InjectView(R.id.cb_confirm)
    CButton cbConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_modify_pwd);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        titleBar.initView(this);

    }

    @OnClick(R.id.cb_confirm)
    public void onClick() {
        if (ledtNewPwd.getEditText().length()<6||ledtNewPwd.getEditText().length()>8){
            showShortToast("密码长度为6~8！");
            return;
        }
        if (!ledtNewPwd.getEditText().equals(ledtReaptPwd.getEditText())){
            showShortToast("新密码两次输入不一致！");
            return;
        }
        UserService service = RetrofitUtil.create(UserService.class);

        Map<String, Object> params = new HashMap<>();
        params.put("id",App.userInfo.getId());
        params.put("tokenId",App.userInfo.getTokenId());
        params.put("oldPsw",ledtOldPwd.getEditText());
        params.put("newPsw",ledtNewPwd.getEditText());

        Call<ResponseBody> call = service.appUpdatePsw(params);
        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getLoadDialog().dismiss();
                if (response.code() != 200) {
                    showShortToast("网络错误" + response.code());
                    return;
                }
                ResponseBean rb=GsonUtil.fromJSon(response, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    showShortToast("修改成功！");
                    startActivity(LoginActivity.class);
                } else {
                    showShortToast(rb.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getLoadDialog().dismiss();
                showShortToast("网络错误！");

            }
        });
    }
}

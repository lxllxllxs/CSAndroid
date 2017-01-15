package com.yiyekeji.coolschool.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.HaveName;
import com.yiyekeji.coolschool.inter.RollCallService;
import com.yiyekeji.coolschool.ui.adapter.HaveNameAdapter;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.RippleView;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lxl on 2017/1/11.
 */
public class DynamicSignInActivity extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;

    List<HaveName> haveNameList = new ArrayList<>();
    HaveNameAdapter mAdapter;
    @InjectView(R.id.rippleView)
    RippleView rippleView;

    private int number;
    private RollCallService service;
    Map<String, Object> params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_sign_in);
        ButterKnife.inject(this);
        initView();
        initData();
    }

    private void initView() {
        titleBar.initView(this);
        rippleView.start();

        params.put("tokenId", App.userInfo.getTokenId());
        params.put("tnum", App.userInfo.getUserNum());
        service = RetrofitUtil.create(RollCallService.class);
        //两秒更一次 共三分钟 一共90
        timer.schedule(mTask,0,2000);
    }



    private int countDown=0;
    private Timer timer = new Timer();
    private TimerTask mTask=new TimerTask() {
        @Override
        public void run() {
            if (countDown==90){
                timer.cancel();
                handler.sendEmptyMessage(0);
                return;
            }
            countDown++;
            handler.sendEmptyMessage(1);
        }
    };



    private Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    rippleView.stop();
                    getSignsInfo();
                    break;
                case 1:
                    getNewSignIn();
                    break;
                case 3:
                    cancelRollCall();
                    break;
            }
        }
    };

    private void cancelRollCall() {
        Call<ResponseBody> call = service.cancelRollCall(params);
        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getLoadDialog().dismiss();
                if (response.code()!=200){
                    showShortToast("网络错误"+response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    showShortToast("已取消点名！");
                    finish();
                } else {
                    showShortToast(rb.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getLoadDialog().dismiss();
            }
        });
    }


    private void getSignsInfo() {
        Call<ResponseBody> call= service.getSignsInfo(params);
        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getLoadDialog().dismiss();
                if (response.code()!=200){
                    showShortToast("网络错误"+response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    showShortToast("返回旷课名单成功！");
                } else {
                    showShortToast(rb.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getLoadDialog().dismiss();
                showShortToast(t.toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!rippleView.isStarting()){
            rippleView.start();
        }
        mTask.run();
    }

    @Override
    protected void onPause() {
        super.onPause();
        rippleView.stop();
        mTask.cancel();
    }

    private void initData() {
    }


    private void  getNewSignIn(){
        Call<ResponseBody> call= service.getNewSignsNum(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()!=200){
                    showShortToast("网络错误"+response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    number = Integer.valueOf(GsonUtil.getValueByTag(jsonString, "signNum"));
                    updateNumber();
                } else {
                    showShortToast(rb.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getLoadDialog().dismiss();
                showShortToast(t.toString());
            }
        });
    }

    private void updateNumber() {
        showShortToast(""+number);
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定停止点名吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.sendEmptyMessage(3);
                    }
                })
                .setNegativeButton("取消", null);
        builder.show();

    }
}

package com.yiyekeji.coolschool.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.ClassAbsenceInfo;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.HaveName;
import com.yiyekeji.coolschool.inter.RollCallService;
import com.yiyekeji.coolschool.ui.adapter.HaveNameAdapter;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.LogUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.RippleView;

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

    List<HaveName> haveNameList = new ArrayList<>();
    HaveNameAdapter mAdapter;
    @InjectView(R.id.rippleView)
    RippleView rippleView;
    @InjectView(R.id.tv_count)
    TextView tvCount;

    private final  int MAX_COUNT=30;
    private String number;
    private RollCallService service;
    Map<String, Object> params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_sign_in);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        rippleView.start();
        params.put("tokenId", App.userInfo.getTokenId());
        params.put("tnum", App.userInfo.getUserNum());
        service = RetrofitUtil.create(RollCallService.class);
        //两秒更一次 共2分钟 一共60
        timer.schedule(getmTask(), 0, 2000);
    }

    private int countDown = 0;
    private Timer timer = new Timer();


    private TimerTask getmTask(){
        return new TimerTask() {
            @Override
            public void run() {
                LogUtil.d("DynamicSignInActivity:TimerTask正在运行"+countDown);
                if (countDown >= MAX_COUNT) {
                    timer.cancel();
                    handler.sendEmptyMessage(0);
                    return;
                }
                countDown++;
                handler.sendEmptyMessage(1);
            }
        };
    }


    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
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
                if (response.code() != 200) {
                    showShortToast("网络错误" + response.code());
                    finish();
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
                showShortToast("网络错误！");
                finish();
            }
        });
    }

    private void getSignsInfo() {
        Call<ResponseBody> call = service.getSignsInfo(params);
        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getLoadDialog().dismiss();
                if (response.code() != 200) {
                    showShortToast("网络错误" + response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    ArrayList<ClassAbsenceInfo> infos=GsonUtil.listFromJSon(jsonString,
                            new TypeToken<List<ClassAbsenceInfo>>() {
                    }.getType(), "signsInfo");
                    Intent intent = new Intent(DynamicSignInActivity.this, AbsenceRecordAty.class);
                    intent.putParcelableArrayListExtra("infos", infos);
                    startActivity(intent);
                    finish();
                } else if (rb.getResult().equals("2")){
                    showShortToast(rb.getMessage());
                }
                else {
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
        updateNumber();
        if (!rippleView.isStarting()&&countDown<MAX_COUNT) {
            rippleView.start();
            timer = new Timer();
            //两秒更一次 共2分钟 一共60
            timer.schedule(getmTask(), 0, 2000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        rippleView.stop();
        timer.cancel();
    }

    private void getNewSignIn() {
        Call<ResponseBody> call = service.getNewSignsNum(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() != 200) {
                    showShortToast("网络错误" + response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    number = GsonUtil.getValueByTag(jsonString, "signNum");
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
        tvCount.setText("已签到\n"+number);
    }


    @Override
    public void onBackPressed() {
        if (countDown == MAX_COUNT) {
            finish();
            return;
        }
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

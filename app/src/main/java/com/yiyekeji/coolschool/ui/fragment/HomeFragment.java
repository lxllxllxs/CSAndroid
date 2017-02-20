package com.yiyekeji.coolschool.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.google.gson.reflect.TypeToken;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.CourseInfo;
import com.yiyekeji.coolschool.bean.MainMenu;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.StudentSign;
import com.yiyekeji.coolschool.inter.RollCallService;
import com.yiyekeji.coolschool.ui.AddCourseAty;
import com.yiyekeji.coolschool.ui.CreateDeliverOrderAty;
import com.yiyekeji.coolschool.ui.CreatePrintOrderAty;
import com.yiyekeji.coolschool.ui.CreateTakeExpressOrderAty;
import com.yiyekeji.coolschool.ui.TeacherRollCallActivitiy;
import com.yiyekeji.coolschool.ui.adapter.HomeAdapter;
import com.yiyekeji.coolschool.ui.base.BaseFragment;
import com.yiyekeji.coolschool.utils.BdLocationUtlis;
import com.yiyekeji.coolschool.utils.CheckEmulatorUtils;
import com.yiyekeji.coolschool.utils.CommonUtils;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.NetUtils;
import com.yiyekeji.coolschool.utils.RetrofitUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 */
public class HomeFragment extends BaseFragment {
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.tv_addCourse)
    TextView tvAddCourse;
    private List<MainMenu> mainMenuList = new ArrayList<>();
    public BDLocationListener myListener = new MyLocationListener();
    public LocationClient mLocationClient = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
        initView();
    }

    private void initData() {
        MainMenu m1 = null;
        if (App.userInfo.getRoleType() == 1) {
            m1 = new MainMenu("点名", R.mipmap.ic_roll_call, TeacherRollCallActivitiy.class);
        } else {
            mLocationClient = new LocationClient(App.getContext());
            mLocationClient.registerLocationListener(myListener);
            m1 = new MainMenu("签到", R.mipmap.ic_sign_in, null);
        }
        /**
         * 订水送水的交换图标
         */
        MainMenu m2 = new MainMenu("我要寄件", R.mipmap.ic_take_express, CreateTakeExpressOrderAty.class);
        MainMenu m3 = new MainMenu("代拿快递", R.mipmap.ic_deliver, CreateDeliverOrderAty.class);
//        MainMenu m4 = new MainMenu("订水", R.mipmap.ic_deliver_water, null);
//        MainMenu m5 = new MainMenu("再来一桶", R.mipmap.ic_order_water, null);
        MainMenu m6 = new MainMenu("打印", R.mipmap.ic_print, CreatePrintOrderAty.class);
//        MainMenu m7 = new MainMenu("广告", R.mipmap.ic_ad, null);
        mainMenuList.add(m1);
        mainMenuList.add(m2);
        mainMenuList.add(m3);
//        mainMenuList.add(m4);
//        mainMenuList.add(m5);
        mainMenuList.add(m6);
//        mainMenuList.add(m7);
    }

    private void initView() {

        HomeAdapter mAdapter = new HomeAdapter(getActivity(), mainMenuList);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
//        recyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickLitener(new HomeAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if (App.userInfo.getRoleType() == 0 && position == 0) {
                    // 除了夜神 根本装不了 哈哈
                    if (CheckEmulatorUtils.isEmulator(getActivity())) {
                        showShortToast("签到失败，模拟器？");
                        return;
                    }
                    //打开模拟位置的话要终止
                    if (Settings.Secure.getInt(getActivity().getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0) {
                        showShortToast("签到失败,请关闭模拟位置");
                        return;
                    }
                    showLoadDialog("");
                    BdLocationUtlis.initLocation(mLocationClient);
                    mLocationClient.start();
                }
            }
        });
    }

    private double latitude, longitude;
    RollCallService service;
    List<CourseInfo> infos;

    private void getMyCoures() {
        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", App.userInfo.getTokenId());
        params.put("userNum", App.userInfo.getUserNum());
        service = RetrofitUtil.create(RollCallService.class);
        Call<ResponseBody> call = service.getMyCourse(params);
        showLoadDialog("正在签到");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() != 200) {
                    getLoadDialog().dismiss();
                    showShortToast("网络错误" + response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                infos = GsonUtil.listFromJSon(jsonString,
                        new TypeToken<List<CourseInfo>>() {
                        }.getType(), "courseInfo");
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (infos != null) {
                    startSignIn();
                } else {
                    getLoadDialog().dismiss();
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

    private void startSignIn() {
        List<String> courseNos = new ArrayList<>();
        for (CourseInfo info : infos) {
            courseNos.add(info.getCourseNo());
        }
        StudentSign signIn = new StudentSign();
        signIn.setIp(NetUtils.getIpAddress());
        signIn.setCourseNo(courseNos);
        signIn.setImei(CommonUtils.getIMEI());
        signIn.setTokenId(App.userInfo.getTokenId());
        signIn.setUserNum(App.userInfo.getUserNum());
        signIn.setX(longitude);
        signIn.setY(latitude);
        service = RetrofitUtil.create(RollCallService.class);
        Call<ResponseBody> call = service.studentSignIn(signIn);
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
                    showDialog(true);
                } else {
                    showDialog(false);
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

    AlertDialog dlg;

    public void showDialog(boolean isSuccess) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogNoBg);
        LayoutInflater inflater = getLayoutInflater(null);
        View layout = inflater.inflate(R.layout.layout_sigin_success, null);//获取自定义布局
        ImageView imageView = (ImageView) layout.findViewById(R.id.iv_success);
        TextView tvMsg = (TextView) layout.findViewById(R.id.tv_message);
        if (!isSuccess) {
            tvMsg.setText("签到失败");
            imageView.setImageResource(R.mipmap.fail);
        }
        builder.setView(layout);
        dlg = builder.create();
        Window window = dlg.getWindow();
        window.setWindowAnimations(R.style.dialog_anim_style);
        dlg.show();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 1000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onStop() {
        if (dlg != null && dlg.isShowing()) {
            dlg.dismiss();
        }
        super.onStop();
    }

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                dlg.dismiss();
            }
        }
    };

    @OnClick(R.id.tv_addCourse)
    public void onClick() {
        if (App.userInfo.getRoleType() == 1) {
            startActivity(new Intent(getActivity(),AddCourseAty.class));
        } else {
            showDialog();
        }
    }


    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            getLoadDialog().dismiss();
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            getMyCoures();
        }
        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
    }

    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View layout = LayoutInflater.from(getActivity()).inflate(R.layout.layout_add_coures_dialog, null);
        builder.setView(layout);
        builder.setTitle("输入课程编号");//设置标题内容
        final EditText editText = (EditText)layout.findViewById(R.id.edt_addCourse);

        builder.setPositiveButton("添加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (TextUtils.isEmpty(editText.getText())) {
                    showShortToast("编号不能为空！");
                    return;
                }
                stuAddCourse(editText.getText().toString());
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        final AlertDialog dlg = builder.create();
        dlg.show();
    }

    private void stuAddCourse(String coureseNo){
        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", App.geTokenId());
        params.put("userNum", App.userInfo.getUserNum());
        params.put("courseNo", coureseNo);
        RollCallService service = RetrofitUtil.create(RollCallService.class);
        Call<ResponseBody> call = service.insertStudentCourse(params);
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
                    showShortToast(rb.getMessage());
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
}
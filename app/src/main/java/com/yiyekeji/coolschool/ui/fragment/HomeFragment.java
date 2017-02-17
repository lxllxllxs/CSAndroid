package com.yiyekeji.coolschool.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.CourseInfo;
import com.yiyekeji.coolschool.bean.MainMenu;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.StudentSign;
import com.yiyekeji.coolschool.inter.RollCallService;
import com.yiyekeji.coolschool.ui.CreateDeliverOrderAty;
import com.yiyekeji.coolschool.ui.CreatePrintOrderAty;
import com.yiyekeji.coolschool.ui.CreateTakeExpressOrderAty;
import com.yiyekeji.coolschool.ui.TeacherRollCallActivitiy;
import com.yiyekeji.coolschool.ui.adapter.HomeAdapter;
import com.yiyekeji.coolschool.ui.base.BaseFragment;
import com.yiyekeji.coolschool.utils.CheckEmulatorUtils;
import com.yiyekeji.coolschool.utils.CommonUtils;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.LogUtil;
import com.yiyekeji.coolschool.utils.NetUtils;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 */
public class HomeFragment extends BaseFragment implements LocationListener {
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    private LocationManager locationManager;
    private List<MainMenu> mainMenuList = new ArrayList<>();
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
        recyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickLitener(new HomeAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if(App.userInfo.getRoleType()==0&&position==0){
                    // 除了夜神 根本装不了 哈哈
                    if (CheckEmulatorUtils.isEmulator(getActivity())) {
                        showShortToast("签到失败，模拟器？");
                        return;
                    }
                    //打开模拟位置的话要终止
                    if (Settings.Secure.getInt(getActivity().getContentResolver(),Settings.Secure.ALLOW_MOCK_LOCATION,0)!= 0 ){
                        showShortToast("签到失败,请关闭模拟位置");
                        return;
                    }
                    setLocation();
                }
            }
        });
    }

    private void setLocation(){
        showLoadDialog("正在定位...");
        locationManager = (LocationManager)getActivity(). getSystemService(getActivity().LOCATION_SERVICE);
        if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                getLoadDialog().dismiss();
                showLongToast("没有NET定位权限！");
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        } else if (locationManager.getProvider(LocationManager.GPS_PROVIDER) != null){
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                getLoadDialog().dismiss();
                showLongToast("没有GPS定位权限！");
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }
    private double latitude,longitude;
    @Override
    public void onLocationChanged(Location location) {
        getLoadDialog().dismiss();
        latitude=location.getLatitude();
        longitude=location.getLongitude();

//        showLongToast(location.toString()+"==="+latitude+longitude);
        // 如果只是需要定位一次，这里就移除监听，停掉服务。如果要进行实时定位，可以在退出应用或者其他时刻停掉定位服务。
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            getLoadDialog().dismiss();
            showLongToast("没有GPS定位权限！");
            return;
        }
        locationManager.removeUpdates(this);
        LogUtil.d("onLocationChanged:"+latitude+"="+longitude);
        getMyCoures();
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        getLoadDialog().dismiss();
    }

    @Override
    public void onProviderEnabled(String provider) {
    }
    @Override
    public void onProviderDisabled(String provider) {
    }

    RollCallService service;
    List<CourseInfo> infos;
    private void getMyCoures() {
        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", App.userInfo.getTokenId());
        params.put("userNum", App.userInfo.getUserNum());
        service = RetrofitUtil.create(RollCallService.class);
        Call<ResponseBody> call= service.getMyCourse(params);
        showLoadDialog("正在签到");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()!=200){
                    getLoadDialog().dismiss();
                    showShortToast("网络错误"+response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                infos= GsonUtil.listFromJSon(jsonString,
                        new TypeToken<List<CourseInfo>>() {}.getType(),"courseInfo") ;
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (infos!=null) {
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

    private int count=0;

    private void  startSignIn(){
        List<String> courseNos = new ArrayList<>();
        for (CourseInfo info:infos){
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
        Call<ResponseBody> call= service.studentSignIn(signIn);
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
    public void showDialog(boolean isSuccess){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.DialogNoBg);
        LayoutInflater inflater = getLayoutInflater(null);
        View layout = inflater.inflate(R.layout.layout_sigin_success, null);//获取自定义布局
        ImageView imageView=(ImageView) layout.findViewById(R.id.iv_success);
        TextView tvMsg=(TextView) layout.findViewById(R.id.tv_message);
        if (!isSuccess){
            tvMsg.setText("签到失败");
            imageView.setImageResource(R.mipmap.fail);
        }
        builder.setView(layout);
        dlg= builder.create();
        Window window = dlg.getWindow();
        window.setWindowAnimations(R.style.dialog_anim_style);
        dlg.show();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        },1000);
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

    private Handler handler=new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0){
                dlg.dismiss();
            }
        }
    };
}
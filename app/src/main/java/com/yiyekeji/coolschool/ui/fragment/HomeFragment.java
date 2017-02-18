package com.yiyekeji.coolschool.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.Poi;
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
import com.yiyekeji.coolschool.utils.BdLocationUtlis;
import com.yiyekeji.coolschool.utils.CheckEmulatorUtils;
import com.yiyekeji.coolschool.utils.CommonUtils;
import com.yiyekeji.coolschool.utils.GsonUtil;
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
public class HomeFragment extends BaseFragment  {
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
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
                    showLoadDialog("");
                    BdLocationUtlis.initLocation(mLocationClient);
                    mLocationClient.start();
                }
            }
        });
    }

    private double latitude,longitude;
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

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            getLoadDialog().dismiss();
            //获取定位结果
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());    //获取定位时间
            sb.append("\nerror code : ");
            sb.append(location.getLocType());    //获取类型类型
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());    //获取纬度信息
            latitude=location.getLatitude();
            longitude=location.getLongitude();
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());    //获取经度信息
            sb.append("\nradius : ");
            sb.append(location.getRadius());    //获取定位精准度
            if (location.getLocType() == BDLocation.TypeGpsLocation){
                // GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());    // 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());    //获取卫星数
                sb.append("\nheight : ");
                sb.append(location.getAltitude());    //获取海拔高度信息，单位米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());    //获取方向信息，单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                // 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());    //获取运营商信息
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                // 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());    //位置语义化信息
            List<Poi> list = location.getPoiList();    // POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.i("BaiduLocationApiDem", sb.toString());
            //
            getMyCoures();
        }
        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

}
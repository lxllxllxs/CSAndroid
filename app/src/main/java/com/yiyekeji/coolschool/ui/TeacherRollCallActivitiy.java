package com.yiyekeji.coolschool.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.google.gson.reflect.TypeToken;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.CourseInfo;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.RollCallService;
import com.yiyekeji.coolschool.ui.adapter.CourseInfoAdapter;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.BdLocationUtlis;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.LogUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.DividerItemDecoration;
import com.yiyekeji.coolschool.widget.TitleBar;

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
 * Created by lxl on 2017/1/9.
 */
public class TeacherRollCallActivitiy extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    private LocationManager locationManager;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private List<CourseInfo> courseInfos = new ArrayList<>();
    private CourseInfoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_call);
        ButterKnife.inject(this);
        checkPermission();

    }

    final int READ_PHONE_STATE_REQUEST_CODE=0x122;
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.READ_PHONE_STATE
                    },
                    READ_PHONE_STATE_REQUEST_CODE);
            return;
        }
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        initData();
        initView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode,grantResults);
    }
    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == READ_PHONE_STATE_REQUEST_CODE) {
           /* if (grantResults[0] == PackageManager.PERMISSION_GRANTED&&grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            } else {
                startActivity(LoginActivity.class);
            }*/
            checkPermission();
        }
    }

    private void initData() {
        BdLocationUtlis.initLocation(mLocationClient);
        mAdapter = new CourseInfoAdapter(this, courseInfos);
        showLoadDialog("正在定位...");
        mLocationClient.start();
    }

    RollCallService service;

    private void getCourseList() {
        service = RetrofitUtil.create(RollCallService.class);
        Map<String, Object> params = new HashMap<>();
        params.put("tnum", App.userInfo.getUserNum());
        params.put("tokenId", App.userInfo.getTokenId());
        Call<ResponseBody> call = service.getAllCourse(params);
        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getLoadDialog().dismiss();
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    courseInfos = GsonUtil.listFromJSon(jsonString,
                            new TypeToken<List<CourseInfo>>() {
                            }.getType(), "courseInfo");
                    LogUtil.d("CourseInfo", courseInfos.size());
                    mAdapter.notifyDataSetChanged(courseInfos);
                } else {
                    showShortToast(rb.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void startRollCall(final CourseInfo info) {
        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", App.userInfo.getTokenId());
        params.put("courseNo", info.getCourseNo());
        params.put("courseId", info.getId());
        params.put("courseName", info.getCourseName());
        params.put("userNum", App.userInfo.getUserNum());
        params.put("realName", App.userInfo.getName());
        params.put("xPosition", longitude);
        params.put("yPosition", latitude);
        RollCallService callService = RetrofitUtil.create(RollCallService.class);
        Call<ResponseBody> call = callService.startCallName(params);
        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getLoadDialog().dismiss();
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    showShortToast("开始点名");
                    Intent intent=new Intent(TeacherRollCallActivitiy.this,DynamicSignInActivity.class);
                    intent.putExtra("info",info);
                    startActivity(intent);
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

    private void initView() {
        titleBar.initView(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setOnItemClickLitener(new CourseInfoAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                startRollCall(courseInfos.get(position));
            }
        });
    }

    private double latitude, longitude;
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            getLoadDialog().dismiss();
            //使用百度坐标系统  113.029552,22.622779 亿业坐标
//            latitude = 22.622779;
//            longitude = 113.029552;
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            mLocationClient.unRegisterLocationListener(this);
            getCourseList();
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
}

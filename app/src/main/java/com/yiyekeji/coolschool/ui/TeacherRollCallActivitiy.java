package com.yiyekeji.coolschool.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.CourseInfo;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.RollCallService;
import com.yiyekeji.coolschool.ui.adapter.CourseInfoAdapter;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
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
public class TeacherRollCallActivitiy extends BaseActivity implements LocationListener {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    private LocationManager locationManager;

    private List<CourseInfo> courseInfos = new ArrayList<>();
    private CourseInfoAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_call);
        ButterKnife.inject(this);
        initData();
        initView();
    }


    private void initData() {
        mAdapter=new CourseInfoAdapter(this,courseInfos);
        setLocation();
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
                            new TypeToken<List<CourseInfo>>() {}.getType(), "courseInfo");
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

    private void startRollCall(CourseInfo info){
        Map<String, Object> params = new HashMap<>();

        params.put("tokenId", App.userInfo.getTokenId());
        params.put("courseNo", info.getCourseNo());
        params.put("courseId", info.getId());
        params.put("courseName", info.getCourseName());
        params.put("userNum", App.userInfo.getUserNum());
        params.put("realName", App.userInfo.getName());
//        params.put("xPosition",latitude);
//        params.put("yPosition",longitude);
        params.put("xPosition",longitude);
        params.put("yPosition",latitude);
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
                    showShortToast("已开始点名");
                    startActivity(DynamicSignInActivity.class);
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
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setOnItemClickLitener(new CourseInfoAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
//                setLocation();
                startRollCall(courseInfos.get(position));
            }
        });
    }

    private void setLocation(){
        showLoadDialog("正在定位...");
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                getLoadDialog().dismiss();
                showLongToast("没有NET定位权限！");
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }
        if (locationManager.getProvider(LocationManager.GPS_PROVIDER) != null){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getLoadDialog().dismiss();
            showLongToast("没有GPS定位权限！");
            return;
        }
        locationManager.removeUpdates(this);
        getCourseList();
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
        getLoadDialog().dismiss();
    }

}

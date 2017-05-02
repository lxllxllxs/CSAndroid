package com.yiyekeji.coolschool.ui.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.AndroidVersion;
import com.yiyekeji.coolschool.bean.CourseInfo;
import com.yiyekeji.coolschool.bean.MainMenu;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.StudentSign;
import com.yiyekeji.coolschool.bean.TuCao;
import com.yiyekeji.coolschool.inter.CommonService;
import com.yiyekeji.coolschool.inter.RollCallService;
import com.yiyekeji.coolschool.inter.TuCaoService;
import com.yiyekeji.coolschool.ui.AddCourseAty;
import com.yiyekeji.coolschool.ui.CommitPullMsgAty;
import com.yiyekeji.coolschool.ui.CreateDeliverOrderAty;
import com.yiyekeji.coolschool.ui.CreateTakeExpressOrderAty;
import com.yiyekeji.coolschool.ui.PullMsgListActivtiy;
import com.yiyekeji.coolschool.ui.QueryScoreAty;
import com.yiyekeji.coolschool.ui.ScheduleAty;
import com.yiyekeji.coolschool.ui.TeacherRollCallActivitiy;
import com.yiyekeji.coolschool.ui.TuCaoDetailAty;
import com.yiyekeji.coolschool.ui.adapter.HomeAdapter;
import com.yiyekeji.coolschool.ui.adapter.TuCaoAdapter;
import com.yiyekeji.coolschool.ui.base.BaseFragment;
import com.yiyekeji.coolschool.utils.BdLocationUtlis;
import com.yiyekeji.coolschool.utils.CheckEmulatorUtils;
import com.yiyekeji.coolschool.utils.CommonUtils;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.NetUtils;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.DividerItemDecoration;

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
    @InjectView(R.id.tv_rollCall)
    TextView tvRollCall;
    @InjectView(R.id.iv_pullMsg)
    ImageView ivPullMsg;
    @InjectView(R.id.iv_unRead)
    ImageView ivUnRead;
    @InjectView(R.id.recyclerView_hot)
    RecyclerView recyclerViewHot;
    private List<MainMenu> mainMenuList = new ArrayList<>();
    public BDLocationListener myListener = new MyLocationListener();
    public LocationClient mLocationClient = null;
    private double latitude, longitude;
    RollCallService service;
    List<CourseInfo> infos;
    TuCaoAdapter pullRreshAdapter;
    List<TuCao> tuCaoList = new ArrayList<>();
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
        if (App.userInfo.getRoleType() == 1) {
            tvRollCall.setText("点名");
            MainMenu m1 = new MainMenu("发布通知", R.mipmap.ic_pullmsg, CommitPullMsgAty.class);
            mainMenuList.add(m1);
            ivPullMsg.setVisibility(View.INVISIBLE);
        } else {
            MainMenu m6 = new MainMenu("课程表", R.mipmap.ic_schedule, ScheduleAty.class);
            MainMenu m4 = new MainMenu("查成绩", R.mipmap.ic_query, QueryScoreAty.class);
            mainMenuList.add(m4);
            mainMenuList.add(m6);
            ivPullMsg.setVisibility(View.VISIBLE);
        }
        MainMenu m2 = new MainMenu("我要寄件", R.mipmap.ic_package, CreateTakeExpressOrderAty.class);
        MainMenu m3 = new MainMenu("代拿快递", R.mipmap.ic_deliver, CreateDeliverOrderAty.class);

        mainMenuList.add(m2);
        mainMenuList.add(m3);

        checkUpdate();//开启更新检测
        getTop5TuCaoList();
    }

    private void initView() {

        HomeAdapter mAdapter = new HomeAdapter(getActivity(), mainMenuList);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickLitener(new HomeAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        });
        recyclerView.setNestedScrollingEnabled(false);
        //热门话题top5
        pullRreshAdapter = new TuCaoAdapter(getContext(), tuCaoList);
        recyclerViewHot.setLayoutManager(new LinearLayoutManager(getContext()));
        pullRreshAdapter.setOnItemClickLitener(new TuCaoAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), TuCaoDetailAty.class);
                intent.putExtra("tuCao", tuCaoList.get(position));
                startActivity(intent);
            }
        });
        recyclerViewHot.setAdapter(pullRreshAdapter);
        recyclerViewHot.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));
        recyclerViewHot.setNestedScrollingEnabled(false);
    }


    private void getMyCoures() {
        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", App.userInfo.getTokenId());
        params.put("userNum", App.userInfo.getUserNum());
        service = RetrofitUtil.create(RollCallService.class);
        Call<ResponseBody> call = service.getMyCourse(params);
        showLoadDialog("正在签到", getActivity());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() != 200) {
                    dismissDialog();
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
                    dismissDialog();
                    showShortToast(rb.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissDialog();
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
                dismissDialog();
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
                dismissDialog();
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
        //成双成对
        imageView.setImageResource(R.mipmap.success);
        tvMsg.setText("签到成功");
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
        }, 2000);
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

    @OnClick({R.id.tv_addCourse,R.id.tv_rollCall, R.id.iv_pullMsg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_addCourse:
                if (App.userInfo.getRoleType() == 1) {
                    startActivity(new Intent(getActivity(), AddCourseAty.class));
                } else {
                    showDialog();
                }
                break;
            case R.id.tv_rollCall:
                if (App.userInfo.getRoleType() == 1) {
                    startActivity(new Intent(getActivity(), TeacherRollCallActivitiy.class));
                } else {
                    startLocation();
                }
                break;
            case R.id.iv_pullMsg:
                ivUnRead.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(getActivity(), PullMsgListActivtiy.class);
                startActivity(intent);
                break;
        }
    }

    public void setIvUnReadVisiable() {
        ivUnRead.setVisibility(View.VISIBLE);
    }

    private void startLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                            Manifest.permission.READ_PHONE_STATE
                    },
                    READ_PHONE_STATE_REQUEST_CODE);
            return;
        }
        mLocationClient = new LocationClient(App.getContext());
        mLocationClient.registerLocationListener(myListener);
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
        showLoadDialog("", getActivity());
        BdLocationUtlis.initLocation(mLocationClient);
        mLocationClient.start();
    }

    final int READ_PHONE_STATE_REQUEST_CODE = 0x122;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == READ_PHONE_STATE_REQUEST_CODE) {
            startLocation();
        }
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            dismissDialog();
            mLocationClient.unRegisterLocationListener(myListener);
            //使用百度坐标系统  113.029552,22.622779 亿业坐标
//            latitude = 22.622779;
//            longitude = 113.029552;
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            getMyCoures();
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View layout = LayoutInflater.from(getActivity()).inflate(R.layout.layout_add_coures_dialog, null);
        builder.setView(layout);
        builder.setTitle("输入课程编号");//设置标题内容
        final EditText editText = (EditText) layout.findViewById(R.id.edt_addCourse);

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

    private void stuAddCourse(String coureseNo) {
        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", App.geTokenId());
        params.put("userNum", App.userInfo.getUserNum());
        params.put("courseNo", coureseNo);
        RollCallService service = RetrofitUtil.create(RollCallService.class);
        Call<ResponseBody> call = service.insertStudentCourse(params);
        showLoadDialog("", getActivity());
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

    private void getTop5TuCaoList() {
        TuCaoService service = RetrofitUtil.create(TuCaoService.class);
        Map<String, Object> params = new HashMap<>();
        Call<ResponseBody> call = service.getTop5TuCaoList(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (getContext() == null) {
                    return;
                }
                dismissDialog();
                if (response.code() != 200) {
                    showShortToast("网络错误" + response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")) {
                    List<TuCao> tempList = GsonUtil.listFromJSon(jsonString,
                            new TypeToken<List<TuCao>>() {
                            }.getType(), "tuCaoList");
                    if (tempList != null) {
                    /*    if (tempList.size()<1){
//                            showShortToast("暂无更多内容");
                            return;
                        }*/
                        tuCaoList.addAll(tempList);
                        pullRreshAdapter.notifyDataSetChanged();
                    } else {
                        showShortToast("发生错误");
                    }
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


    AndroidVersion version = null;
    private void checkUpdate() {
        CommonService service = RetrofitUtil.create(CommonService.class);
        Call<ResponseBody> call = service.checkVersion();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (!rb.getResult().equals("1")) {
//                    showShortToast("已是最新版本");
                    return;
                }
                version = GsonUtil.fromJSon(jsonString,
                        AndroidVersion.class, "checkVersion");
                if (version != null) {
                    if (isNeedUpdate()) {
                        AlertDialog.Builder buidler = new AlertDialog.Builder(getContext());
                        buidler.setMessage(version.getDate() + "\n" + version.getContent())
                                .setTitle(getString(R.string.update_title))
                                .setNegativeButton(getString(R.string.update_cancle), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton(getString(R.string.update_confirm), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
//                                        startDownLoad();
                                        //直接借用其他浏览器打开连接
                                        Intent intent = new Intent();
                                        intent.setAction("android.intent.action.VIEW");
                                        Uri content_url = Uri.parse(version.getUrl());
                                        intent.setData(content_url);
                                        startActivity(intent);

                                    }
                                })
                                .show();
                    }
                } else {
//                    showShortToast(getString(R.string.check_version));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private boolean isNeedUpdate() {
        return version.getVersion() > (CommonUtils.getAppVersion());
    }
}
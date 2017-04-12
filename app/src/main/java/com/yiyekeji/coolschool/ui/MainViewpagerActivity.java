package com.yiyekeji.coolschool.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.CourseInfo;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.bean.TuCao;
import com.yiyekeji.coolschool.dao.PullMsg;
import com.yiyekeji.coolschool.db.DbUtil;
import com.yiyekeji.coolschool.inter.RollCallService;
import com.yiyekeji.coolschool.inter.StudentService;
import com.yiyekeji.coolschool.inter.TuCaoService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.ui.fragment.AccountFragment;
import com.yiyekeji.coolschool.ui.fragment.CategoryFragment;
import com.yiyekeji.coolschool.ui.fragment.HomeFragment;
import com.yiyekeji.coolschool.ui.fragment.TuCaoFragment;
import com.yiyekeji.coolschool.utils.DateUtils;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.widget.FakeTabHost;

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

public class MainViewpagerActivity extends BaseActivity {

    @InjectView(R.id.viewpager)
    ViewPager viewpager;
    @InjectView(R.id.tabHost)
    FakeTabHost tabHost;
    private List<Fragment> fragmentList = new ArrayList<>();


    private String[] titles = {"首页", "商场","吐槽","我的"};
    private int[] resId = {R.mipmap.ic_home, R.mipmap.ic_market, R.mipmap.ic_tucao, R.mipmap.ic_my};
    private int[] resIdNo = {R.mipmap.ic_home_no, R.mipmap.ic_market_no, R.mipmap.ic_tucao_no, R.mipmap.ic_my_no};
    private List<PullMsg> pullMsgs;
    private List<CourseInfo> infos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_viewpager);
        ButterKnife.inject(this);
        initView();
        checkPermission();
        initData();
        initViewPager();
//        test();
    }

    private void initViewPager() {
        fragmentList.add(new HomeFragment());
        fragmentList.add(new CategoryFragment());
        fragmentList.add(new TuCaoFragment());//这个第二期加
        fragmentList.add(new AccountFragment());
        tabHost.setTabHost(fragmentList, titles, resId, resIdNo, viewpager);
    }

    private void test() {
        TuCao tucao = new TuCao();
        tucao.setDate(DateUtils.getTimeString());
        tucao.setContent("This is the first Tucao");
        tucao.setAuthor("luoxuelin");
        tucao.setCommentCount("1");
        tucao.setSex("1");
        tucao.setUserNo(App.getUserInfo().getUserNum());
        TuCaoService service = RetrofitUtil.create(TuCaoService.class);
        Call<ResponseBody> call = service.insertTuCao(tucao);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() != 200) {
                    dismissDialog();
                    showShortToast("网络错误" + response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (rb.getResult().equals("1")){
                    showLongToast("publish 吐槽 success");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void initData() {
        if (App.getUserInfo().getRoleType()==0){
            getMyCoures();
        }
    }
    private void getMyCoures() {
        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", App.userInfo.getTokenId());
        params.put("userNum", App.userInfo.getUserNum());
        RollCallService service = RetrofitUtil.create(RollCallService.class);
        Call<ResponseBody> call = service.getMyCourse(params);
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
                    getPullMsg();
                } else {
                    showShortToast(rb.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showShortToast(t.toString());
            }
        });
    }


    private void getPullMsg() {
        Map<String, Object> params = new HashMap<>();
        StudentService service = RetrofitUtil.create(StudentService.class);
        ArrayList<String> courseNos = new ArrayList<>();
        for (CourseInfo info : infos) {
            courseNos.add(info.getCourseNo());
        }
        params.put("courseNoList",courseNos);
        Call<ResponseBody> call = service.getPullMsg(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() != 200) {
                    dismissDialog();
                    showShortToast("网络错误" + response.code());
                    return;
                }
                String jsonString = GsonUtil.toJsonString(response);
                pullMsgs = GsonUtil.listFromJSon(jsonString,
                        new TypeToken<List<PullMsg>>() {
                        }.getType(), "pullMsgList");
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (pullMsgs != null) {
                    for (PullMsg msg : pullMsgs) {
                        DbUtil.insertPullMsg(msg);
                    }
                } else {
                    showShortToast(rb.getMessage());
                }
                //无论结果如何都要检查有无未读消息
                checkUnReadMsg();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showShortToast(t.toString());
            }
        });
    }

    /**
     * 查表
     * 检查PullMsg中有无isRead为0
     * 不为空则设置小红点
     */
    private void checkUnReadMsg() {
        boolean haveUnRead = DbUtil.havaUnReadMsg();
        if (haveUnRead) {
            ((HomeFragment)fragmentList.get(0)).setIvUnReadVisiable();
        }
    }
    public void showMsgDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("有未读消息！");//设置标题内容

        builder.setPositiveButton("查看", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                startActivity(PullMsgListActivtiy.class);
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
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(intent);
            }
        }
    }

    private void initView() {
    }

    /**
     * 连续点击两次 关闭
     */
    long[] mHits = new long[2];

    @Override
    public void onBackPressed() {
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        //实现左移，然后最后一个位置更新距离开机的时间，如果最后一个时间和最开始时间小于500，即双击
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        showShortToast("再次点击退出应用");
        if (mHits[0] >= (SystemClock.uptimeMillis() - 1000)) {
            App.removeAllActivity();
        }
    }
}

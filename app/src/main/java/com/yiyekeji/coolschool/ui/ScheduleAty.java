package com.yiyekeji.coolschool.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.api.HtmlParser;
import com.yiyekeji.coolschool.api.Lesson;
import com.yiyekeji.coolschool.api.WyuStuSystemApi;
import com.yiyekeji.coolschool.bean.UserInfo;
import com.yiyekeji.coolschool.db.DbUtil;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.ConstantUtils;
import com.yiyekeji.coolschool.utils.ThreadPools;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lxl on 2017/3/26.
 */
public class ScheduleAty extends BaseActivity {
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.inject(this);
        initView();
        initData();
    }

    private void initView() {
        titleBar.initView(this);
        //先加载
        String rawHtml=DbUtil.getScheduleString();
        Map<Integer, Map<Integer, List<Lesson>>> lesson= HtmlParser.parseHtmlForLesson(rawHtml);
        showWebView(rawHtml);
    }

    private void initData() {
        if (App.getUserInfo() != null) {
            UserInfo info = App.getUserInfo();
            getFirstCookie(info.getUserNum(), info.getPassword());
        }
    }

    /**
     *这个方法是防止App登录密码和子系统密码不一致导致登不上
     * 设置的
     */
    private void needSubSysPwd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View layout = LayoutInflater.from(this).inflate(R.layout.layout_input_sub_pwd, null);
        builder.setView(layout);
        builder.setTitle("输入学生子系统密码");//设置标题内容
        final EditText editText = (EditText) layout.findViewById(R.id.edt_addCourse);

        builder.setPositiveButton("获取课程表", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (TextUtils.isEmpty(editText.getText())) {
                    showShortToast("密码不能为空！");
                    return;
                }
                getFirstCookie(App.getUserInfo().getUserNum(),editText.getText().toString());
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


    private static final int Error = 369;
    private static final int SUCCESS = 895;
    Map<Integer, Map<Integer, List<Lesson>>> hashMap;

    /**
     * API直接返回HTml字符串 用WebView加载即可
     * 或是处理后封装好的数据
     * @param studentNo
     * @param pwd
     */
    private void getFirstCookie(final String studentNo, final String pwd) {
        showLoadDialog("");
        ThreadPools.getInstance().addRunnable(new Runnable() {
            @Override
            public void run() {
                WyuStuSystemApi api = new WyuStuSystemApi();
                try {
                    String result = api.login(studentNo, pwd);
                    Message msg = new Message();

                    if (result.equals(ConstantUtils.LOGIN_SYS_ERROR)) {
                        msg.obj = "子系统出错,请重试！";
                        msg.what = Error;
                        handler.sendMessage(msg);
                        return;
                    }
                    if (result.equals(ConstantUtils.LOGIN_PSW_ERROR)) {
                        msg.obj = "学校系统用户名或者密码错误";
                        msg.what = Error;
                        handler.sendMessage(msg);
                        return;
                    }
//                    hashMap = api.getCourse(result);
                    result = api.getCourse(result);
                    //here save data
                    DbUtil.insertScheduleString(result);
                    msg.what = SUCCESS;
                    msg.obj = result;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    showShortToast("连接超时，请重试！");
                    e.printStackTrace();
                }
            }
        });
    }


    private void showWebView(String html) {
        // 设置WevView要显示的网页
        webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
        webView.getSettings().setJavaScriptEnabled(true); //设置支持Javascript
        webView.requestFocus(); //触摸焦点起作用.如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件。
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //设置点击网页里面的链接还是在当前的webview里跳转
                view.loadUrl(url);
                return true;
            }
        });

    }

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismissDialog();
            switch (msg.what) {
                case Error:
                    showShortToast(msg.obj.toString());
                    needSubSysPwd();
                    break;
                case SUCCESS:
//                    LogUtil.d("课程信息列表：" + hashMap.size());
                    showWebView(msg.obj.toString());
                    break;
            }
        }
    };
}

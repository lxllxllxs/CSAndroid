package com.yiyekeji.coolschool.ui;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yiyekeji.coolschool.Config;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.AndroidVersion;
import com.yiyekeji.coolschool.bean.ResponseBean;
import com.yiyekeji.coolschool.inter.CommonService;
import com.yiyekeji.coolschool.ui.base.BaseActivity;
import com.yiyekeji.coolschool.utils.CommonUtils;
import com.yiyekeji.coolschool.utils.GlideUtil;
import com.yiyekeji.coolschool.utils.GsonUtil;
import com.yiyekeji.coolschool.utils.LogUtil;
import com.yiyekeji.coolschool.utils.RetrofitUtil;
import com.yiyekeji.coolschool.utils.SPUtils;
import com.yiyekeji.coolschool.widget.TitleBar;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lxl on 2017/2/4.
 */
public class AboutActivity extends BaseActivity {
    @InjectView(R.id.ll_checkUpdate)
    LinearLayout llCheckUpdate;
    @InjectView(R.id.title_bar)
    TitleBar titleBar;
    @InjectView(R.id.iv_shareApp)
    ImageView ivShareApp;
    @InjectView(R.id.ll_feedback)
    LinearLayout llFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.inject(this);
        initView();
        initData();
    }

    private void initData() {
        getQrCodeUrl();
    }

    private void initView() {
        titleBar.initView(this);
    }


    private void getQrCodeUrl() {
        CommonService service = RetrofitUtil.create(CommonService.class);
        Call<ResponseBody> call = service.getQrCodeUrl();
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
                    GlideUtil.setImageToView(rb.getMessage(), ivShareApp);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getLoadDialog().dismiss();
                showShortToast(getString(R.string.response_err));
            }
        });
    }

    @OnClick({R.id.ll_checkUpdate,R.id.ll_feedback})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_checkUpdate:
                checkUpdate();
                break;
            case R.id.ll_feedback:
                startActivity(FeedBackActivity.class);
                break;
        }
    }

    /**
     *
     */
    AndroidVersion version = null;

    private void checkUpdate() {
        CommonService service = RetrofitUtil.create(CommonService.class);
        Call<ResponseBody> call = service.checkVersion();
        showLoadDialog("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getLoadDialog().dismiss();
                String jsonString = GsonUtil.toJsonString(response);
                ResponseBean rb = GsonUtil.fromJSon(jsonString, ResponseBean.class);
                if (!rb.getResult().equals("1")) {
                    showShortToast("已是最新版本");
                    return;
                }
                version = GsonUtil.fromJSon(jsonString,
                        AndroidVersion.class, "checkVersion");
                if (version != null) {
                    if (isNeedUpdate()) {
                        AlertDialog.Builder buidler = new AlertDialog.Builder(AboutActivity.this);
                        buidler.setMessage(version.getContent() + "\n" + version.getDate())
                                .setTitle("准备下载更新？")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
                    showShortToast(getString(R.string.check_version));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getLoadDialog().dismiss();
                showShortToast(getString(R.string.response_err));
            }
        });
    }

    long downloadId;

    private void startDownLoad() {
        final DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(version.getUrl()));
        request.setDestinationInExternalPublicDir(Config.TEMP_FILE, "CoolSchool.apk");
        request.setTitle("正在更新");
        request.setDescription("");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setMimeType("application/cn.trinea.download.file");
        downloadId = downloadManager.enqueue(request);

        File folder = new File(Config.SD_PATH + Config.TEMP_FILE);
        if (!folder.exists() && folder.isDirectory()) {
            folder.mkdirs();
        }
        SPUtils.put(this, "downloadcomplete", downloadId);

        handler.postDelayed(runnable, 1000);
// 注册广播接收器，当下载完成时自动安装
    /*    IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                long myDwonloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadId == myDwonloadID) {
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    Uri downloadFileUri = downloadManager.getUriForDownloadedFile(downloadId);
                    install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                    install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(install);
                }
            }
        };
        registerReceiver(receiver, filter);*/
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Query query = new DownloadManager.Query();
            SharedPreferences sPreferences = getSharedPreferences("downloadcomplete", 0);
            query.setFilterById(downloadId);
            Cursor cursor = manager.query(query);
            if (!cursor.moveToFirst()) {
                cursor.close();
                return;
            }
            long id = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            String localFilename = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
            long downloadedSoFar = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            long totalSize = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            if (downloadedSoFar == totalSize) {
                cursor.close();
                return;
            }
            LogUtil.d("进度：", downloadedSoFar * 100 / totalSize + "%");
            cursor.close();
        }
    };

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    handler.postDelayed(runnable, 1000);
                    break;
                case 1:

                    break;
            }
        }
    };

    private boolean isNeedUpdate() {
        if (version.getVersion() > (CommonUtils.getAppVersion())) {
            return true;
        }
        return false;
    }

}

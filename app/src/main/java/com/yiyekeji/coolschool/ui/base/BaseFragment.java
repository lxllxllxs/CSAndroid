package com.yiyekeji.coolschool.ui.base;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.ui.LoginActivity;
import com.yiyekeji.coolschool.widget.LoadDialog;

import java.lang.reflect.Field;

public class BaseFragment extends Fragment {
    private Toast shortToast, longToast ;
    private  LoadDialog fragmentDialog;
    private final static String RELOGIN_MESSAGE = "账号已在其它设备登录,请重新登录";
    protected void showShortToast(String text) {
        if(shortToast==null){
            shortToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        }
        if (TextUtils.isEmpty(text)) {
            return;
        }
        /**
         * 先这样处理
         */
        if (text.equals(RELOGIN_MESSAGE)) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            getActivity().startActivity(intent);
        }
        shortToast.setText(text);
        shortToast.show();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    protected void showLongToast(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if(shortToast==null){
            shortToast = Toast.makeText(getContext(), text, Toast.LENGTH_LONG);
        }else{
            shortToast.setText(text);
        }
        shortToast.show();
    }
    /**
     * 创建loadDialog
     *mdialog静态持有引用被finsh掉的activity 导致android.view.WindowManager$BadTokenException: Unable to add window -- token android.os.Bind。。
     * @param msg
     */
    public  void showLoadDialog(String msg, Context context) {
        if (context == null) {
            return;
        }
        if (fragmentDialog==null) {
            fragmentDialog = new LoadDialog(context, R.layout.layout_load_dialog, R.style.DialogLogin);
        }
        if (fragmentDialog.isShowing()) {
            return;
        }
        initLoadDialog(msg);
        // 默认不能按屏幕取消dialog
        fragmentDialog.setCanceledOnTouchOutside(false);
        fragmentDialog.show();
    }

    /**
     *当fragment进行到detached状态时，它会重置它的内部状态。
     然而，它没有重置mChildFragmentManager.这是当前版本support库的一个bug.
     这导致在Fragment重新attach时，它(fragment)没有重新attachm childFragmentManager，从而引发异常.
     解决方案：
     在每个调用getChildFragmentManager()的fragment中加入：
     */
    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 载入loadDialog控件
     *
     * @param msg
     * @param
     */
    protected  void initLoadDialog(String msg) {

        View view = fragmentDialog.getEntryView();
        ((TextView) view.findViewById(R.id.CtvInitTip)).setText(msg);
        if (TextUtils.isEmpty(msg)) {
            ((TextView) view.findViewById(R.id.CtvInitTip))
                    .setVisibility(View.GONE);
        }
    }

    /**
     * 获取dialog对象
     * @return
     */
    protected void dismissDialog() {
        if (fragmentDialog != null) {
            fragmentDialog.dismiss();
        }
    }
}

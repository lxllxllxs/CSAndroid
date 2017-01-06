package com.yiyekeji.coolschool.ui.base;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.yiyekeji.coolschool.App;
import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.widget.LoadDialog;

import java.lang.reflect.Field;

public class BaseFragment extends Fragment {
    private Toast shortToast, longToast ;
    private static LoadDialog fragmentDialog;

    protected void showShortToast(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if(shortToast==null){
            shortToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        }else{
            shortToast.setText(text);
        }
        shortToast.show();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    protected void showLongToast(CharSequence text) {
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
    public  void showLoadDialog(String msg) {
        if (BaseActivity.mdDialog == null) {
            BaseActivity.mdDialog = new LoadDialog(App.getContext(), R.layout.layout_load_dialog,
                    R.style.DialogLogin);
            BaseActivity.mdDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        if (BaseActivity.mdDialog.isShowing()) {
            return;
        }
        initProgressDialog(msg);
        // 默认不能按屏幕取消dialog
        BaseActivity.mdDialog.setCanceledOnTouchOutside(false);
        BaseActivity.mdDialog.show();
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
    protected  void initProgressDialog(String msg) {

        View view = BaseActivity.mdDialog.getEntryView();
        ((TextView) view.findViewById(R.id.CtvInitTip)).setText(msg);
        if (TextUtils.isEmpty(msg)) {
            ((TextView) view.findViewById(R.id.CtvInitTip))
                    .setVisibility(View.GONE);
        }
    }

    /**
     * 获取dialog对象
     *
     * @return
     */
    protected LoadDialog getLoadDialog() {
        return BaseActivity.mdDialog;
    }
}

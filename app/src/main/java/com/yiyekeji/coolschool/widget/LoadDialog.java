package com.yiyekeji.coolschool.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 自定义Load的dialog
 *
 * @author lwj
 *
 */
public class LoadDialog extends Dialog {
    private View EntryView;
    private Context c;

    /**
     * Style:style.xml中布局样式, ResId：xml布局Id
     *
     * @param context
     * @param ResId
     * @param Style
     */
    public LoadDialog(Context context, int ResId, int Style) {
        super(context, Style);
        this.c = context;
        LayoutInflater factory = LayoutInflater.from(c);
        EntryView = factory.inflate(ResId, null);
    }

    public LoadDialog(Context context, int ResId) {
        super(context, ResId);
    }

    public LoadDialog(Context context, boolean cancelable,
                      OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(EntryView);
        /**
         * 可以添加dialog属性，例如模糊度，色调，或者xml中添加
         */
        // // lp=mdialog.getWindow().getAttributes();
        // // // 模糊度
        // //
        // //
        // mdialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
        // // WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        // // mdialog.getWindow().setAttributes(lp);
        // // lp.alpha=1f;//（0.0-1.0）透明度
        // // //黑暗度为
        // // lp.dimAmount=1f;
    }

    public View getEntryView() {
        return EntryView;
    }

    public void setEntryView(View entryView) {
        EntryView = entryView;
        setContentView(EntryView);
    }

}


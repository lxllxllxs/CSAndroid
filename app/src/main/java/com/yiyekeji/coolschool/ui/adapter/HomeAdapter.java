package com.yiyekeji.coolschool.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyekeji.coolschool.R;
import com.yiyekeji.coolschool.bean.MainMenu;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 *
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>
{

    private LayoutInflater mInflater;
    private List<MainMenu> mainMenuList;
    private Context context;
    public HomeAdapter(Context context,List<MainMenu> mainMenuList)
    {   this.mainMenuList=mainMenuList;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View arg0)
        {
            super(arg0);
            AutoUtils.auto(arg0);
        }
        TextView tv_name;
        ImageView iv_icon;
        LinearLayout ll_parent;
    }

    @Override
    public int getItemCount()
    {
        return mainMenuList.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = mInflater.inflate(R.layout.item_home_menu_adapter, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
        viewHolder.iv_icon=(ImageView)view.findViewById(R.id.iv_icon);
        viewHolder.ll_parent=(LinearLayout)view.findViewById(R.id.ll_parent);
        return viewHolder;
    }


    /**
     * 设置布局控件内容
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i)
    {
        final MainMenu  mainMenu=mainMenuList.get(i);
        viewHolder.tv_name.setText(mainMenu.getName());
        viewHolder.iv_icon.setImageResource(mainMenu.getResId());
        viewHolder.ll_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainMenu.getTargetActivity() != null) {
                    Intent intent=new Intent(context,mainMenu.getClass());
                    context.startActivity(intent);
                }
            }
        });
    }

    /**
     * 自定义一个回调点击函数
     * @author lxl
     */
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public OnItemClickLitener mOnItemClickLitener;

    /**
     * 点击事件设置回调
     *
     * @param mOnItemClickLitener
     */
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


}

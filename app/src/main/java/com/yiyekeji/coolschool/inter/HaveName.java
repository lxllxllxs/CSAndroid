package com.yiyekeji.coolschool.inter;

public interface HaveName {
    String getName();
    boolean isSelect();//需要显示选中状态的重写这个方法
    void setSelect(boolean b);//需要显示选中状态的重写这个方法
}

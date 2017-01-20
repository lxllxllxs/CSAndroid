package com.yiyekeji.coolschool.bean;

/**
 * Created by lxl on 2017/1/20.
 */
public class ProductModel {
    private String pmTitle="";
    private int pmBalance=0;
    private float pmPrice=0;

    public String getPmTitle() {
        return pmTitle;
    }

    public void setPmTitle(String pmTitle) {
        this.pmTitle = pmTitle;
    }

    public int getPmBalance() {
        return pmBalance;
    }

    public void setPmBalance(int pmBalance) {
        this.pmBalance = pmBalance;
    }

    public float getPmPrice() {
        return pmPrice;
    }

    public void setPmPrice(float pmPrice) {
        this.pmPrice = pmPrice;
    }
}

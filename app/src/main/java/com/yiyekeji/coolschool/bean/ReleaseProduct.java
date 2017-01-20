package com.yiyekeji.coolschool.bean;

import java.util.ArrayList;

/**
 * Created by lxl on 2017/1/20.
 */
public class ReleaseProduct {

    private String pTitle;
    private String pDescrition;
    private String supplierNum;
    private String pUnit;
    private int categoryId;

    private ArrayList<Integer> pictureIdList;
    private ArrayList<ProductModel> modelList;

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getpDescrition() {
        return pDescrition;
    }

    public void setpDescrition(String pDescrition) {
        this.pDescrition = pDescrition;
    }

    public String getSupplierNum() {
        return supplierNum;
    }

    public void setSupplierNum(String supplierNum) {
        this.supplierNum = supplierNum;
    }

    public String getpUnit() {
        return pUnit;
    }

    public void setpUnit(String pUnit) {
        this.pUnit = pUnit;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public ArrayList<Integer> getPictureIdList() {
        return pictureIdList;
    }

    public void setPictureIdList(ArrayList<Integer> pictureIdList) {
        this.pictureIdList = pictureIdList;
    }

    public ArrayList<ProductModel> getModelList() {
        return modelList;
    }

    public void setModelList(ArrayList<ProductModel> modelList) {
        this.modelList = modelList;
    }


}
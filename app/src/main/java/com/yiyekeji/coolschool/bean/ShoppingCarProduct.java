package com.yiyekeji.coolschool.bean;

import com.yiyekeji.coolschool.inter.HaveName;

import java.util.List;

/**
 * Created by lxl on 2017/2/9.
 */
public class ShoppingCarProduct implements HaveName {


    /**
     * supplierName : 宝元
     * supplierNum : 3112002927
     * supplierPhone : 13658596524
     * productList : [{"pId":12,"pmTitle":"大","pmPrice":3.5,"pmCount":1,"pTitle":"砂糖橘","pUnit":"斤","cartItemId":4,"imagePath":"http://59.110.143.46:8080/cs/glWrBEJAIMG_0091.JPG","pmId":14}]
     */

    private String supplierName;
    private String supplierNum;
    private String supplierPhone;
    private List<ProductListBean> productList;

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierNum() {
        return supplierNum;
    }

    public void setSupplierNum(String supplierNum) {
        this.supplierNum = supplierNum;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }

    public void setSupplierPhone(String supplierPhone) {
        this.supplierPhone = supplierPhone;
    }

    public List<ProductListBean> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductListBean> productList) {
        this.productList = productList;
    }


    @Override
    public String getName() {
        return getSupplierName();
    }

    private boolean isSelect;
    @Override
    public boolean isSelect() {
        return isSelect;
    }

    @Override
    public void setSelect(boolean b) {
        this.isSelect = b;
    }
}

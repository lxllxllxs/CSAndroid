package com.yiyekeji.coolschool.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lxl on 2017/2/14.
 */
public class SellerProductOrder implements Parcelable {


    /**
     * pOrderItemList : [{"message":"A msg","pmPrice":1.9,"pmTitle":"A","pmCount":2,"pTitle":"lxl test orderlist","pmId":25,"pUnit":"kg","pImage":"http://59.110.143.46:8080/cs/OWUyYttg4.jpg"},{"message":"b msg","pmPrice":2,"pmTitle":"B","pmCount":2,"pTitle":"lxl test orderlist","pmId":26,"pUnit":"kg","pImage":"http://59.110.143.46:8080/cs/OWUyYttg4.jpg"}]
     * receiveName : 学林
     * sum : 7.8
     * receivePhone : 15019861460
     * receiveAddr : 43-214
     * poState : 0
     * timeType : 午饭时间
     */

    private String receiveName;
    private double sum;
    private String receivePhone;
    private String receiveAddr;
    private int poState;
    private String timeType;
    private List<POrderItemListBean> pOrderItemList;

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getReceivePhone() {
        return receivePhone;
    }

    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }

    public String getReceiveAddr() {
        return receiveAddr;
    }

    public void setReceiveAddr(String receiveAddr) {
        this.receiveAddr = receiveAddr;
    }

    public int getPoState() {
        return poState;
    }

    public void setPoState(int poState) {
        this.poState = poState;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public List<POrderItemListBean> getPOrderItemList() {
        return pOrderItemList;
    }

    public void setPOrderItemList(List<POrderItemListBean> pOrderItemList) {
        this.pOrderItemList = pOrderItemList;
    }

    public static class POrderItemListBean {
        /**
         * message : A msg
         * pmPrice : 1.9
         * pmTitle : A
         * pmCount : 2
         * pTitle : lxl test orderlist
         * pmId : 25
         * pUnit : kg
         * pImage : http://59.110.143.46:8080/cs/OWUyYttg4.jpg
         */

        private String message;
        private double pmPrice;
        private String pmTitle;
        private int pmCount;
        private String pTitle;
        private int pmId;
        private String pUnit;
        private String pImage;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public double getPmPrice() {
            return pmPrice;
        }

        public void setPmPrice(double pmPrice) {
            this.pmPrice = pmPrice;
        }

        public String getPmTitle() {
            return pmTitle;
        }

        public void setPmTitle(String pmTitle) {
            this.pmTitle = pmTitle;
        }

        public int getPmCount() {
            return pmCount;
        }

        public void setPmCount(int pmCount) {
            this.pmCount = pmCount;
        }

        public String getPTitle() {
            return pTitle;
        }

        public void setPTitle(String pTitle) {
            this.pTitle = pTitle;
        }

        public int getPmId() {
            return pmId;
        }

        public void setPmId(int pmId) {
            this.pmId = pmId;
        }

        public String getPUnit() {
            return pUnit;
        }

        public void setPUnit(String pUnit) {
            this.pUnit = pUnit;
        }

        public String getPImage() {
            return pImage;
        }

        public void setPImage(String pImage) {
            this.pImage = pImage;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.receiveName);
        dest.writeDouble(this.sum);
        dest.writeString(this.receivePhone);
        dest.writeString(this.receiveAddr);
        dest.writeInt(this.poState);
        dest.writeString(this.timeType);
        dest.writeList(this.pOrderItemList);
    }

    public SellerProductOrder() {
    }

    protected SellerProductOrder(Parcel in) {
        this.receiveName = in.readString();
        this.sum = in.readDouble();
        this.receivePhone = in.readString();
        this.receiveAddr = in.readString();
        this.poState = in.readInt();
        this.timeType = in.readString();
        this.pOrderItemList = new ArrayList<POrderItemListBean>();
        in.readList(this.pOrderItemList, POrderItemListBean.class.getClassLoader());
    }

    public static final Creator<SellerProductOrder> CREATOR = new Creator<SellerProductOrder>() {
        @Override
        public SellerProductOrder createFromParcel(Parcel source) {
            return new SellerProductOrder(source);
        }

        @Override
        public SellerProductOrder[] newArray(int size) {
            return new SellerProductOrder[size];
        }
    };
}

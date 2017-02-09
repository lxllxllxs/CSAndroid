package com.yiyekeji.coolschool.bean;

public  class ProductListBean {
        /**
         * pId : 12
         * pmTitle : 大
         * pmPrice : 3.5
         * pmCount : 1
         * pTitle : 砂糖橘
         * pUnit : 斤
         * cartItemId : 4
         * imagePath : http://59.110.143.46:8080/cs/glWrBEJAIMG_0091.JPG
         * pmId : 14
         */

        private int pId;
        private String pmTitle;
        private double pmPrice;
        private int pmCount;
        private String pTitle;
        private String pUnit;
        private int cartItemId;
        private String imagePath;
        private int pmId;

        public int getPId() {
            return pId;
        }

        public void setPId(int pId) {
            this.pId = pId;
        }

        public String getPmTitle() {
            return pmTitle;
        }

        public void setPmTitle(String pmTitle) {
            this.pmTitle = pmTitle;
        }

        public double getPmPrice() {
            return pmPrice;
        }

        public void setPmPrice(double pmPrice) {
            this.pmPrice = pmPrice;
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

        public String getPUnit() {
            return pUnit;
        }

        public void setPUnit(String pUnit) {
            this.pUnit = pUnit;
        }

        public int getCartItemId() {
            return cartItemId;
        }

        public void setCartItemId(int cartItemId) {
            this.cartItemId = cartItemId;
        }

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        public int getPmId() {
            return pmId;
        }

        public void setPmId(int pmId) {
            this.pmId = pmId;
        }
    }
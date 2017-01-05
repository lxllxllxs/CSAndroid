package com.yiyekeji.coolschool.bean;

/**
 * Created by lxl on 2017/1/5.
 */
public class ResponseBean {
    private String result;
    private String message;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ResponseBean{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

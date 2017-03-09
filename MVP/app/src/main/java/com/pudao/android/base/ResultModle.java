package com.pudao.android.base;

/**
 * Created by pucheng on 2017/3/9.
 * 网络请求返回结果的模型
 */

public class ResultModle<T> {

    private int code = 0;
    private String msg;
    private T data;
    private T rows;

    public T getRows() {
        return rows;
    }

    public void setRows(T rows) {
        this.rows = rows;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}

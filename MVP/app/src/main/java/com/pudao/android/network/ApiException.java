package com.pudao.android.network;

import android.text.TextUtils;

/**
 * Created by pucheng on 2017年3月16日21:56:45.
 * 对网络请求错误作统一处理
 */
public class ApiException extends RuntimeException {

    public static final int CONVERSION_RESULT_NULL = -10001;

    public ApiException(String msg) {
        super(genErrorInfo(-1, msg));
    }

    public ApiException(int code) {
        super(genErrorInfo(code, null));
    }

    public ApiException(int code, String msg) {
        this(genErrorInfo(code, msg));
    }

    private static String genErrorInfo(int code, String msg) {
        if (!TextUtils.isEmpty(msg)) {
            return msg;
        }
        String message = getApiExceptionMessage(code);
        if (!TextUtils.isEmpty(message)){
            return message;
        }
        return "error code:" + code;
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     *
     * @param code
     * @return
     */
    private static String getApiExceptionMessage(int code) {
        String message = null;
        switch (code) {
            case CONVERSION_RESULT_NULL:
                message = "请求结果转换为Null";
                break;
        }
        return null;
    }
}


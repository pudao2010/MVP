package com.pudao.android.app;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * Created by pucheng on 2017/2/20.
 *
 */

public class AppCrashHandler implements Thread.UncaughtExceptionHandler{

    public static final String TAG = "CrashHandler";

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static AppCrashHandler INSTANCE = new AppCrashHandler();
    private Context mContext;

    private AppCrashHandler() {
    }

    public static AppCrashHandler getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            //ex.printStackTrace();
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        ex.printStackTrace();

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "应用出现未知异常,即将退出.", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return true;
    }

}

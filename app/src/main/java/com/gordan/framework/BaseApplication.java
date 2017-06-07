package com.gordan.framework;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Stack;

/**
 * Created by Gordan on 2015/10/22.
 */
public class BaseApplication extends Application
{
    public static final String PATH_ERROR_LOG = "error.log";

    public static boolean runningFirst;//标识 该APP是否是第一次运行(前提是不切换屏幕)

    private UEHandler ueHandler;

    private Stack<Activity> stack = new Stack<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        runningFirst=true;
        ueHandler = new UEHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(ueHandler);
    }

    public void pushActiviy(Activity a) {
        stack.push(a);
    }
    public void popActiviy(Activity a) {
        stack.remove(a);
    }
    public void finishAll() {
        Activity a;
        for(int i=stack.size()-1; i>=0; i--) {
            a = stack.get(i);
            a.finish();
        }
    }
    public void finishOthers() {
        Activity a;
        for(int i=stack.size()-2; i>=0; i--) {
            a = stack.get(i);
            a.finish();
        }
    }

    public void finishMultiple(int n) {
        Activity a;
        for(int i=stack.size()-1; i>=0&&n>0; i--) {
            a = stack.get(i);
            a.finish();
            n--;
        }
    }

    /**
     * 完全退出APP
     *
     */
    public void exitApp()
    {
        finishAll();
        System.exit(0);
    }

    /**
     * 判断当前APP是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground()
    {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = this.getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses)
        {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
            {
                return true;
            }
        }
        return false;
    }


    /*
     *
     *主要是利用此类捕获APP的异常
     *非UI线程类异常时，跳转到报错界面
     *
     */
    private class UEHandler implements Thread.UncaughtExceptionHandler {
        private Application softApp;
        UEHandler(Application app) {
            softApp = app;
        }

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            String info = this.toErrorLog(softApp, ex);
            long threadId = thread.getId();
            if (threadId != 1) {
                //非UI线程异常时，跳转到汇报异常页面
                Intent intent = new Intent(softApp, ExceptionReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("error", info);
                intent.putExtra("by", "uehandler");
                softApp.startActivity(intent);
            } else {
                //UI线程异常时，重新启动应用并进入汇报异常页面
                Intent intent = new Intent(softApp, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                softApp.startActivity(intent);

                write2ErrorLog(info);
                //关闭所有的Activity
                finishAll();
                //强制结束当前APP的进程
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }

        /*
         *获取该APP的包信息  以及手机的硬件信息和系统软件信息
         *
         * @param softApp
         * @param ex
         * @return
         */
        private String toErrorLog(Application softApp, Throwable ex) {
            String info = null;
            ByteArrayOutputStream baos = null;
            PrintStream printStream = null;
            try {
                baos = new ByteArrayOutputStream();
                printStream = new PrintStream(baos);
                ex.printStackTrace(printStream);
                byte[] data = baos.toByteArray();
                info = new String(data);
                TelephonyManager telephonyManager=(TelephonyManager)softApp.getSystemService(Context.TELEPHONY_SERVICE);
                PackageManager manager = softApp.getPackageManager();
                PackageInfo pinfo = manager.getPackageInfo(softApp.getPackageName(), 0);
                info += ""+(char)(13)+(char)(10)+"Version:"+pinfo.versionName;
                info += ""+(char)(13)+(char)(10)+"VersionCode:"+pinfo.versionCode;
                info += ""+(char)(13)+(char)(10)+"Android:" + android.os.Build.MODEL + ","
                        + android.os.Build.VERSION.RELEASE;
                info += ""+(char)(13)+(char)(10)+"Hardware:"
                        + android.os.Build.BOARD + ","
                        + android.os.Build.BRAND + ","//手机品牌
                        + android.os.Build.CPU_ABI + ","
                        + android.os.Build.DEVICE + ","
                        + android.os.Build.DISPLAY + ","
                        + android.os.Build.FINGERPRINT + ","
                        + android.os.Build.HOST + ","
                        + android.os.Build.ID + ","
                        + android.os.Build.MANUFACTURER + ","
                        + android.os.Build.MODEL + ","//手机型号
                        + android.os.Build.PRODUCT + ","
                        + android.os.Build.TAGS + ","
                        + android.os.Build.TIME + ","
                        + android.os.Build.TYPE + ","
                        + android.os.Build.USER+","
                +telephonyManager.getDeviceId()+","//imei号全球唯一的
                +telephonyManager.getSubscriberId()+","
                +telephonyManager.getLine1Number()+","//手机号码
                +telephonyManager.getSimOperatorName();//SIM卡运营商
                data = null;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (printStream != null) {
                        printStream.close();
                    }
                    if (baos != null) {
                        baos.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return info;
        }

        private void write2ErrorLog(String content) {
            FileOutputStream fos = null;
            try {
                fos = softApp.openFileOutput(BaseApplication.PATH_ERROR_LOG, Context.MODE_PRIVATE);
                fos.write(content.getBytes());
                Log.e("Framework",content.toString());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

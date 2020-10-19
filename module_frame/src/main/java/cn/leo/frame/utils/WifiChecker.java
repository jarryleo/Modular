package cn.leo.frame.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

/**
 * @author : ling luo
 * @date : 2020/8/31
 * @description :Wi-Fi检测工具
 */
public class WifiChecker {

    /**
     * 判断Wi-Fi开关是否打开
     *
     * @param context 上下文
     * @return true 打开，false 关闭
     */
    public static boolean isWifiOpen(Context context) {
        WifiManager wifiManager =
                (WifiManager) context.getApplicationContext()
                        .getSystemService(Context.WIFI_SERVICE);
        return wifiManager != null && wifiManager.isWifiEnabled();
    }

    /**
     * 判断Wi-Fi是否连接网络
     *
     * @param context 上下文
     * @return true 已连接，false 未连接
     */

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager != null &&
                connectivityManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }


}

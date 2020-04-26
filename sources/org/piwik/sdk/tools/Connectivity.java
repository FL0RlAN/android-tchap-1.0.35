package org.piwik.sdk.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connectivity {
    private final ConnectivityManager mConnectivityManager;

    public enum Type {
        NONE,
        MOBILE,
        WIFI
    }

    public Connectivity(Context context) {
        this.mConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
    }

    public boolean isConnected() {
        NetworkInfo activeNetworkInfo = this.mConnectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public Type getType() {
        NetworkInfo activeNetworkInfo = this.mConnectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return Type.NONE;
        }
        if (activeNetworkInfo.getType() == 1) {
            return Type.WIFI;
        }
        return Type.MOBILE;
    }
}

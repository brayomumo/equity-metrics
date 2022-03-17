package com.metrics;

import android.annotation.TargetApi;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;


@TargetApi(Build.VERSION_CODES.M)
public class NetworkManager extends ReactContextBaseJavaModule {

    NetworkStatsManager networkStatsManager;
    int packageUid;

    public NetworkManager(NetworkStatsManager networkStatsManager, ReactApplicationContext context) {
        super(context);
        this.networkStatsManager = networkStatsManager;
    }

    public NetworkManager(NetworkStatsManager networkStatsManager, int packageUid) {
        this.networkStatsManager = networkStatsManager;
        this.packageUid = packageUid;
    }

    public long getAllRxBytesMobile(Context context) {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    0,
                    System.currentTimeMillis());
        } catch (java.rmi.RemoteException e) {
            Log.d("NetworkManager", "NetworkManager.getAllRxBytesMobile Exception: "+ e.getMessage());
            return -1;
        }
        return bucket.getRxBytes();
    }

    public long getAllTxBytesMobile(Context context) {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    0,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            Log.d("NetworkManager", "NetworkManager.getAllTxBytesMobile Exception: "+ e.getMessage());

            return -1;
        }
        return bucket.getTxBytes();
    }

    public long getAllRxBytesWifi() {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI,
                    "",
                    0,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            Log.d("NetworkManager", "NetworkManager.getAllRxBytesWifi Exception: "+ e.getMessage());

            return -1;
        }
        return bucket.getRxBytes();
    }

    public long getAllTxBytesWifi() {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI,
                    "",
                    0,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            Log.d("NetworkManager", "NetworkManager.getAllTxBytesWifi Exception: "+ e.getMessage());

            return -1;
        }
        return bucket.getTxBytes();
    }

    public long getPackageRxBytesMobile(Context context) {
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(
                    ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    0,
                    System.currentTimeMillis(),
                    packageUid);
        } catch (RemoteException e) {
            Log.d("NetworkManager", "NetworkManager.getPackageRxBytesMobile Exception: "+ e.getMessage());

            return -1;
        }

        long rxBytes = 0L;
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        while (networkStats.hasNextBucket()) {
            networkStats.getNextBucket(bucket);
            rxBytes += bucket.getRxBytes();
        }
        networkStats.close();
        return rxBytes;
    }

    public long getPackageTxBytesMobile(Context context) {
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(
                    ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    0,
                    System.currentTimeMillis(),
                    packageUid);
        } catch (RemoteException e) {
            Log.d("NetworkManager", "NetworkManager.getPackageTxBytesMobile Exception: "+ e.getMessage());

            return -1;
        }

        long txBytes = 0L;
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        while (networkStats.hasNextBucket()) {
            networkStats.getNextBucket(bucket);
            txBytes += bucket.getTxBytes();
        }
        networkStats.close();
        return txBytes;
    }

    public long getPackageRxBytesWifi() {
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(
                    ConnectivityManager.TYPE_WIFI,
                    "",
                    0,
                    System.currentTimeMillis(),
                    packageUid);
        } catch (RemoteException e) {
            Log.d("NetworkManager", "NetworkManager.getPackageRxBytesWifi Exception: "+ e.getMessage());

            return -1;
        }

        long rxBytes = 0L;
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        while (networkStats.hasNextBucket()) {
            networkStats.getNextBucket(bucket);
            rxBytes += bucket.getRxBytes();
        }
        networkStats.close();
        return rxBytes;
    }

    public long getPackageTxBytesWifi() {
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(
                    ConnectivityManager.TYPE_WIFI,
                    "",
                    0,
                    System.currentTimeMillis(),
                    packageUid);
        } catch (RemoteException e) {
            Log.d("NetworkManager", "NetworkManager.getPackageTxBytesWifi Exception: "+ e.getMessage());

            return -1;
        }

        long txBytes = 0L;
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        while (networkStats.hasNextBucket()) {
            networkStats.getNextBucket(bucket);
            txBytes += bucket.getTxBytes();
        }
        networkStats.close();
        return txBytes;
    }

    private String getSubscriberId(Context context, int networkType) {
        if (ConnectivityManager.TYPE_MOBILE == networkType) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSubscriberId();
        }
        return "";
    }

    @Override
    public String getName(){
        return "NetworkManager";
    }
}
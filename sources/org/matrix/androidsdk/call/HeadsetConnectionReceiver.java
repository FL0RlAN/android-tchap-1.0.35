package org.matrix.androidsdk.call;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import java.util.HashSet;
import java.util.Set;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OutgoingRoomKeyRequestEntityFields;

public class HeadsetConnectionReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = HeadsetConnectionReceiver.class.getSimpleName();
    private static AudioManager mAudioManager = null;
    /* access modifiers changed from: private */
    public static Boolean mIsHeadsetPlugged = null;
    private static HeadsetConnectionReceiver mSharedInstance = null;
    private final Set<OnHeadsetStatusUpdateListener> mListeners = new HashSet();

    public interface OnHeadsetStatusUpdateListener {
        void onBluetoothHeadsetUpdate(boolean z);

        void onWiredHeadsetUpdate(boolean z);
    }

    public static HeadsetConnectionReceiver getSharedInstance(Context context) {
        if (mSharedInstance == null) {
            mSharedInstance = new HeadsetConnectionReceiver();
            context.registerReceiver(mSharedInstance, new IntentFilter("android.intent.action.HEADSET_PLUG"));
            context.registerReceiver(mSharedInstance, new IntentFilter("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED"));
            context.registerReceiver(mSharedInstance, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
            context.registerReceiver(mSharedInstance, new IntentFilter("android.bluetooth.device.action.ACL_CONNECTED"));
            context.registerReceiver(mSharedInstance, new IntentFilter("android.bluetooth.device.action.ACL_DISCONNECTED"));
        }
        return mSharedInstance;
    }

    public void addListener(OnHeadsetStatusUpdateListener onHeadsetStatusUpdateListener) {
        synchronized (LOG_TAG) {
            this.mListeners.add(onHeadsetStatusUpdateListener);
        }
    }

    public void removeListener(OnHeadsetStatusUpdateListener onHeadsetStatusUpdateListener) {
        synchronized (LOG_TAG) {
            this.mListeners.remove(onHeadsetStatusUpdateListener);
        }
    }

    /* access modifiers changed from: private */
    public void onBluetoothHeadsetUpdate(boolean z) {
        synchronized (LOG_TAG) {
            for (OnHeadsetStatusUpdateListener onBluetoothHeadsetUpdate : this.mListeners) {
                try {
                    onBluetoothHeadsetUpdate.onBluetoothHeadsetUpdate(z);
                } catch (Exception e) {
                    String str = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## onBluetoothHeadsetUpdate()) failed ");
                    sb.append(e.getMessage());
                    Log.e(str, sb.toString(), e);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onWiredHeadsetUpdate(boolean z) {
        synchronized (LOG_TAG) {
            for (OnHeadsetStatusUpdateListener onWiredHeadsetUpdate : this.mListeners) {
                try {
                    onWiredHeadsetUpdate.onWiredHeadsetUpdate(z);
                } catch (Exception e) {
                    String str = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## onWiredHeadsetUpdate()) failed ");
                    sb.append(e.getMessage());
                    Log.e(str, sb.toString(), e);
                }
            }
        }
    }

    public void onReceive(Context context, Intent intent) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## onReceive() : ");
        sb.append(intent.getExtras());
        Log.d(str, sb.toString());
        String action = intent.getAction();
        String str2 = "android.intent.action.HEADSET_PLUG";
        if (TextUtils.equals(action, str2) || TextUtils.equals(action, "android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED") || TextUtils.equals(action, "android.bluetooth.adapter.action.STATE_CHANGED") || TextUtils.equals(action, "android.bluetooth.device.action.ACL_CONNECTED") || TextUtils.equals(action, "android.bluetooth.device.action.ACL_DISCONNECTED")) {
            Boolean bool = null;
            boolean equals = TextUtils.equals(action, str2);
            final boolean z = false;
            if (equals) {
                int intExtra = intent.getIntExtra(OutgoingRoomKeyRequestEntityFields.STATE, -1);
                if (intExtra == 0) {
                    Log.d(LOG_TAG, "Headset is unplugged");
                    bool = Boolean.valueOf(false);
                } else if (intExtra != 1) {
                    Log.d(LOG_TAG, "undefined state");
                } else {
                    Log.d(LOG_TAG, "Headset is plugged");
                    bool = Boolean.valueOf(true);
                }
            } else {
                int profileConnectionState = BluetoothAdapter.getDefaultAdapter().getProfileConnectionState(1);
                String str3 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("bluetooth headset state ");
                sb2.append(profileConnectionState);
                Log.d(str3, sb2.toString());
                bool = Boolean.valueOf(2 == profileConnectionState);
                if (mIsHeadsetPlugged != bool) {
                    z = true;
                }
            }
            if (bool != mIsHeadsetPlugged) {
                mIsHeadsetPlugged = bool;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    public void run() {
                        if (z) {
                            HeadsetConnectionReceiver.this.onBluetoothHeadsetUpdate(HeadsetConnectionReceiver.mIsHeadsetPlugged.booleanValue());
                        } else {
                            HeadsetConnectionReceiver.this.onWiredHeadsetUpdate(HeadsetConnectionReceiver.mIsHeadsetPlugged.booleanValue());
                        }
                    }
                }, 1000);
            }
        }
    }

    private static AudioManager getAudioManager(Context context) {
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) context.getSystemService("audio");
        }
        return mAudioManager;
    }

    public static boolean isHeadsetPlugged(Context context) {
        if (mIsHeadsetPlugged == null) {
            mIsHeadsetPlugged = Boolean.valueOf(isBTHeadsetPlugged() || getAudioManager(context).isWiredHeadsetOn());
        }
        return mIsHeadsetPlugged.booleanValue();
    }

    public static boolean isBTHeadsetPlugged() {
        return 2 == BluetoothAdapter.getDefaultAdapter().getProfileConnectionState(1);
    }
}

package org.matrix.androidsdk.crypto.algorithms.megolm;

import java.util.Iterator;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;

public class MXOutboundSessionInfo {
    private static final String LOG_TAG = MXOutboundSessionInfo.class.getSimpleName();
    private final long mCreationTime = System.currentTimeMillis();
    public final String mSessionId;
    public final MXUsersDevicesMap<Integer> mSharedWithDevices = new MXUsersDevicesMap<>();
    public int mUseCount = 0;

    public MXOutboundSessionInfo(String str) {
        this.mSessionId = str;
    }

    public boolean needsRotation(int i, int i2) {
        long currentTimeMillis = System.currentTimeMillis() - this.mCreationTime;
        if (this.mUseCount < i && currentTimeMillis < ((long) i2)) {
            return false;
        }
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## needsRotation() : Rotating megolm session after ");
        sb.append(this.mUseCount);
        sb.append(", ");
        sb.append(currentTimeMillis);
        sb.append("ms");
        Log.d(str, sb.toString());
        return true;
    }

    public boolean sharedWithTooManyDevices(MXUsersDevicesMap<MXDeviceInfo> mXUsersDevicesMap) {
        for (String str : this.mSharedWithDevices.getUserIds()) {
            String str2 = "## sharedWithTooManyDevices() : Starting new session because we shared with ";
            if (mXUsersDevicesMap.getUserDeviceIds(str) == null) {
                String str3 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                sb.append(str);
                Log.d(str3, sb.toString());
                return true;
            }
            Iterator it = this.mSharedWithDevices.getUserDeviceIds(str).iterator();
            while (true) {
                if (it.hasNext()) {
                    String str4 = (String) it.next();
                    if (mXUsersDevicesMap.getObject(str4, str) == null) {
                        String str5 = LOG_TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str2);
                        sb2.append(str);
                        sb2.append(":");
                        sb2.append(str4);
                        Log.d(str5, sb2.toString());
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

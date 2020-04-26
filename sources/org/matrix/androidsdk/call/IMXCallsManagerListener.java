package org.matrix.androidsdk.call;

import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;

public interface IMXCallsManagerListener {
    void onCallHangUp(IMXCall iMXCall);

    void onIncomingCall(IMXCall iMXCall, MXUsersDevicesMap<MXDeviceInfo> mXUsersDevicesMap);

    void onOutgoingCall(IMXCall iMXCall);

    void onVoipConferenceFinished(String str);

    void onVoipConferenceStarted(String str);
}

package org.matrix.androidsdk.crypto.keysbackup;

import kotlin.Metadata;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0010X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014¨\u0006\u0015"}, d2 = {"Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupVersionTrustSignature;", "", "()V", "device", "Lorg/matrix/androidsdk/crypto/data/MXDeviceInfo;", "getDevice", "()Lorg/matrix/androidsdk/crypto/data/MXDeviceInfo;", "setDevice", "(Lorg/matrix/androidsdk/crypto/data/MXDeviceInfo;)V", "deviceId", "", "getDeviceId", "()Ljava/lang/String;", "setDeviceId", "(Ljava/lang/String;)V", "valid", "", "getValid", "()Z", "setValid", "(Z)V", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: KeysBackupVersionTrustSignature.kt */
public final class KeysBackupVersionTrustSignature {
    private MXDeviceInfo device;
    private String deviceId;
    private boolean valid;

    public final String getDeviceId() {
        return this.deviceId;
    }

    public final void setDeviceId(String str) {
        this.deviceId = str;
    }

    public final MXDeviceInfo getDevice() {
        return this.device;
    }

    public final void setDevice(MXDeviceInfo mXDeviceInfo) {
        this.device = mXDeviceInfo;
    }

    public final boolean getValid() {
        return this.valid;
    }

    public final void setValid(boolean z) {
        this.valid = z;
    }
}

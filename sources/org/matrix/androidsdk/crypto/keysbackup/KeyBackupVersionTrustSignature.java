package org.matrix.androidsdk.crypto.keysbackup;

import kotlin.Metadata;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e¨\u0006\u000f"}, d2 = {"Lorg/matrix/androidsdk/crypto/keysbackup/KeyBackupVersionTrustSignature;", "", "()V", "device", "Lorg/matrix/androidsdk/crypto/data/MXDeviceInfo;", "getDevice", "()Lorg/matrix/androidsdk/crypto/data/MXDeviceInfo;", "setDevice", "(Lorg/matrix/androidsdk/crypto/data/MXDeviceInfo;)V", "valid", "", "getValid", "()Z", "setValid", "(Z)V", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: KeyBackupVersionTrustSignature.kt */
public final class KeyBackupVersionTrustSignature {
    private MXDeviceInfo device;
    private boolean valid;

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

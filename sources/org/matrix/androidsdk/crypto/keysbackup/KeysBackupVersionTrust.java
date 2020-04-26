package org.matrix.androidsdk.crypto.keysbackup;

import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R \u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001a\u0010\n\u001a\u00020\u000bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000f¨\u0006\u0010"}, d2 = {"Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupVersionTrust;", "", "()V", "signatures", "", "Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupVersionTrustSignature;", "getSignatures", "()Ljava/util/List;", "setSignatures", "(Ljava/util/List;)V", "usable", "", "getUsable", "()Z", "setUsable", "(Z)V", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: KeysBackupVersionTrust.kt */
public final class KeysBackupVersionTrust {
    private List<KeysBackupVersionTrustSignature> signatures = new ArrayList();
    private boolean usable;

    public final boolean getUsable() {
        return this.usable;
    }

    public final void setUsable(boolean z) {
        this.usable = z;
    }

    public final List<KeysBackupVersionTrustSignature> getSignatures() {
        return this.signatures;
    }

    public final void setSignatures(List<KeysBackupVersionTrustSignature> list) {
        Intrinsics.checkParameterIsNotNull(list, "<set-?>");
        this.signatures = list;
    }
}

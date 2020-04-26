package com.google.firebase.iid;

import android.util.Base64;
import com.google.android.gms.common.internal.Objects;
import java.security.KeyPair;

final class zzz {
    private final KeyPair zzbs;
    /* access modifiers changed from: private */
    public final long zzbt;

    zzz(KeyPair keyPair, long j) {
        this.zzbs = keyPair;
        this.zzbt = j;
    }

    /* access modifiers changed from: 0000 */
    public final KeyPair getKeyPair() {
        return this.zzbs;
    }

    /* access modifiers changed from: 0000 */
    public final long getCreationTime() {
        return this.zzbt;
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof zzz)) {
            return false;
        }
        zzz zzz = (zzz) obj;
        if (this.zzbt != zzz.zzbt || !this.zzbs.getPublic().equals(zzz.zzbs.getPublic()) || !this.zzbs.getPrivate().equals(zzz.zzbs.getPrivate())) {
            return false;
        }
        return true;
    }

    public final int hashCode() {
        return Objects.hashCode(this.zzbs.getPublic(), this.zzbs.getPrivate(), Long.valueOf(this.zzbt));
    }

    /* access modifiers changed from: private */
    public final String zzv() {
        return Base64.encodeToString(this.zzbs.getPublic().getEncoded(), 11);
    }

    /* access modifiers changed from: private */
    public final String zzw() {
        return Base64.encodeToString(this.zzbs.getPrivate().getEncoded(), 11);
    }
}

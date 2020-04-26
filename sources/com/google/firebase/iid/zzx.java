package com.google.firebase.iid;

final class zzx implements InstanceIdResult {
    private final String zzbq;
    private final String zzbr;

    zzx(String str, String str2) {
        this.zzbq = str;
        this.zzbr = str2;
    }

    public final String getId() {
        return this.zzbq;
    }

    public final String getToken() {
        return this.zzbr;
    }
}

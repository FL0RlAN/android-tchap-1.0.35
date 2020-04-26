package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import android.os.DeadObjectException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.Api.Client;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.SimpleClientAdapter;

public final class zaah implements zabd {
    /* access modifiers changed from: private */
    public final zabe zafs;
    private boolean zaft = false;

    public zaah(zabe zabe) {
        this.zafs = zabe;
    }

    public final void begin() {
    }

    public final void onConnected(Bundle bundle) {
    }

    public final void zaa(ConnectionResult connectionResult, Api<?> api, boolean z) {
    }

    public final <A extends AnyClient, R extends Result, T extends ApiMethodImpl<R, A>> T enqueue(T t) {
        return execute(t);
    }

    /* JADX WARNING: type inference failed for: r0v11, types: [com.google.android.gms.common.api.Api$SimpleClient] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    public final <A extends AnyClient, T extends ApiMethodImpl<? extends Result, A>> T execute(T t) {
        try {
            this.zafs.zaed.zahe.zab(t);
            zaaw zaaw = this.zafs.zaed;
            Client client = (Client) zaaw.zagy.get(t.getClientKey());
            Preconditions.checkNotNull(client, "Appropriate Api was not requested.");
            if (client.isConnected() || !this.zafs.zaho.containsKey(t.getClientKey())) {
                if (client instanceof SimpleClientAdapter) {
                    client = ((SimpleClientAdapter) client).getClient();
                }
                t.run(client);
                return t;
            }
            t.setFailedResult(new Status(17));
            return t;
        } catch (DeadObjectException unused) {
            this.zafs.zaa((zabf) new zaai(this, this));
        }
    }

    public final boolean disconnect() {
        if (this.zaft) {
            return false;
        }
        if (this.zafs.zaed.zaax()) {
            this.zaft = true;
            for (zacm zabv : this.zafs.zaed.zahd) {
                zabv.zabv();
            }
            return false;
        }
        this.zafs.zaf(null);
        return true;
    }

    public final void connect() {
        if (this.zaft) {
            this.zaft = false;
            this.zafs.zaa((zabf) new zaaj(this, this));
        }
    }

    public final void onConnectionSuspended(int i) {
        this.zafs.zaf(null);
        this.zafs.zahs.zab(i, this.zaft);
    }

    /* access modifiers changed from: 0000 */
    public final void zaam() {
        if (this.zaft) {
            this.zaft = false;
            this.zafs.zaed.zahe.release();
            disconnect();
        }
    }
}

package com.google.android.gms.common.api.internal;

import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClientKey;
import com.google.android.gms.common.api.Api.Client;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public final class zacp {
    public static final Status zakw = new Status(8, "The connection to Google Play services was lost");
    private static final BasePendingResult<?>[] zakx = new BasePendingResult[0];
    private final Map<AnyClientKey<?>, Client> zagy;
    final Set<BasePendingResult<?>> zaky = Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap()));
    private final zacs zakz = new zacq(this);

    public zacp(Map<AnyClientKey<?>, Client> map) {
        this.zagy = map;
    }

    /* access modifiers changed from: 0000 */
    public final void zab(BasePendingResult<? extends Result> basePendingResult) {
        this.zaky.add(basePendingResult);
        basePendingResult.zaa(this.zakz);
    }

    /* JADX WARNING: type inference failed for: r5v0, types: [com.google.android.gms.common.api.ResultCallback, com.google.android.gms.common.api.internal.zacs, com.google.android.gms.common.api.zac, com.google.android.gms.common.api.internal.zacq] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r5v0, types: [com.google.android.gms.common.api.ResultCallback, com.google.android.gms.common.api.internal.zacs, com.google.android.gms.common.api.zac, com.google.android.gms.common.api.internal.zacq]
  assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY]]
  uses: [com.google.android.gms.common.api.internal.zacs, com.google.android.gms.common.api.ResultCallback, com.google.android.gms.common.api.zac, com.google.android.gms.common.api.internal.zacq]
  mth insns count: 47
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 1 */
    public final void release() {
        BasePendingResult[] basePendingResultArr;
        for (BasePendingResult basePendingResult : (BasePendingResult[]) this.zaky.toArray(zakx)) {
            ? r5 = 0;
            basePendingResult.zaa((zacs) r5);
            if (basePendingResult.zam() != null) {
                basePendingResult.setResultCallback(r5);
                IBinder serviceBrokerBinder = ((Client) this.zagy.get(((ApiMethodImpl) basePendingResult).getClientKey())).getServiceBrokerBinder();
                if (basePendingResult.isReady()) {
                    basePendingResult.zaa((zacs) new zacr(basePendingResult, r5, serviceBrokerBinder, r5));
                } else if (serviceBrokerBinder == null || !serviceBrokerBinder.isBinderAlive()) {
                    basePendingResult.zaa((zacs) r5);
                    basePendingResult.cancel();
                    r5.remove(basePendingResult.zam().intValue());
                } else {
                    zacr zacr = new zacr(basePendingResult, r5, serviceBrokerBinder, r5);
                    basePendingResult.zaa((zacs) zacr);
                    try {
                        serviceBrokerBinder.linkToDeath(zacr, 0);
                    } catch (RemoteException unused) {
                        basePendingResult.cancel();
                        r5.remove(basePendingResult.zam().intValue());
                    }
                }
                this.zaky.remove(basePendingResult);
            } else if (basePendingResult.zat()) {
                this.zaky.remove(basePendingResult);
            }
        }
    }

    public final void zabx() {
        for (BasePendingResult zab : (BasePendingResult[]) this.zaky.toArray(zakx)) {
            zab.zab(zakw);
        }
    }
}

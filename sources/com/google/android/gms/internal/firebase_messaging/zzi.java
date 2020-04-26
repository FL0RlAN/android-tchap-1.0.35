package com.google.android.gms.internal.firebase_messaging;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

final class zzi {
    private final ConcurrentHashMap<zzj, List<Throwable>> zzg = new ConcurrentHashMap<>(16, 0.75f, 10);
    private final ReferenceQueue<Throwable> zzh = new ReferenceQueue<>();

    zzi() {
    }

    public final List<Throwable> zza(Throwable th, boolean z) {
        Reference poll = this.zzh.poll();
        while (poll != null) {
            this.zzg.remove(poll);
            poll = this.zzh.poll();
        }
        List<Throwable> list = (List) this.zzg.get(new zzj(th, null));
        if (list != null) {
            return list;
        }
        Vector vector = new Vector(2);
        List<Throwable> list2 = (List) this.zzg.putIfAbsent(new zzj(th, this.zzh), vector);
        return list2 == null ? vector : list2;
    }
}

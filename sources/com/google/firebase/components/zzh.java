package com.google.firebase.components;

import com.google.android.gms.common.internal.Preconditions;
import com.google.firebase.events.Event;
import com.google.firebase.events.EventHandler;
import com.google.firebase.events.Publisher;
import com.google.firebase.events.Subscriber;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

/* compiled from: com.google.firebase:firebase-common@@16.0.2 */
class zzh implements Publisher, Subscriber {
    private final Map<Class<?>, ConcurrentHashMap<EventHandler<Object>, Executor>> zza = new HashMap();
    private Queue<Event<?>> zzb = new ArrayDeque();
    private final Executor zzc;

    zzh(Executor executor) {
        this.zzc = executor;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001c, code lost:
        if (r0.hasNext() == false) goto L_0x0032;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001e, code lost:
        r1 = (java.util.Map.Entry) r0.next();
        ((java.util.concurrent.Executor) r1.getValue()).execute(com.google.firebase.components.zzi.zza(r1, r4));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0032, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0010, code lost:
        r0 = zza(r4).iterator();
     */
    public void publish(Event<?> event) {
        Preconditions.checkNotNull(event);
        synchronized (this) {
            if (this.zzb != null) {
                this.zzb.add(event);
            }
        }
    }

    private synchronized Set<Entry<EventHandler<Object>, Executor>> zza(Event<?> event) {
        Map map = (Map) this.zza.get(event.getType());
        if (map == null) {
            return Collections.emptySet();
        }
        return map.entrySet();
    }

    public synchronized <T> void subscribe(Class<T> cls, Executor executor, EventHandler<? super T> eventHandler) {
        Preconditions.checkNotNull(cls);
        Preconditions.checkNotNull(eventHandler);
        Preconditions.checkNotNull(executor);
        if (!this.zza.containsKey(cls)) {
            this.zza.put(cls, new ConcurrentHashMap());
        }
        ((ConcurrentHashMap) this.zza.get(cls)).put(eventHandler, executor);
    }

    public <T> void subscribe(Class<T> cls, EventHandler<? super T> eventHandler) {
        subscribe(cls, this.zzc, eventHandler);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0028, code lost:
        return;
     */
    public synchronized <T> void unsubscribe(Class<T> cls, EventHandler<? super T> eventHandler) {
        Preconditions.checkNotNull(cls);
        Preconditions.checkNotNull(eventHandler);
        if (this.zza.containsKey(cls)) {
            ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) this.zza.get(cls);
            concurrentHashMap.remove(eventHandler);
            if (concurrentHashMap.isEmpty()) {
                this.zza.remove(cls);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public final void zza() {
        Queue<Event> queue;
        synchronized (this) {
            if (this.zzb != null) {
                queue = this.zzb;
                this.zzb = null;
            } else {
                queue = null;
            }
        }
        if (queue != null) {
            for (Event publish : queue) {
                publish(publish);
            }
        }
    }
}

package io.realm.internal;

import io.realm.RealmChangeListener;
import io.realm.internal.ObserverPairList.Callback;
import io.realm.internal.ObserverPairList.ObserverPair;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

public abstract class RealmNotifier implements Closeable {
    private List<Runnable> finishedSendingNotificationsCallbacks = new ArrayList();
    private final Callback<RealmObserverPair> onChangeCallBack = new Callback<RealmObserverPair>() {
        public void onCalled(RealmObserverPair realmObserverPair, Object obj) {
            if (RealmNotifier.this.sharedRealm != null && !RealmNotifier.this.sharedRealm.isClosed()) {
                realmObserverPair.onChange(obj);
            }
        }
    };
    private ObserverPairList<RealmObserverPair> realmObserverPairs = new ObserverPairList<>();
    /* access modifiers changed from: private */
    public OsSharedRealm sharedRealm;
    private List<Runnable> startSendingNotificationsCallbacks = new ArrayList();
    private List<Runnable> transactionCallbacks = new ArrayList();

    private static class RealmObserverPair<T> extends ObserverPair<T, RealmChangeListener<T>> {
        public RealmObserverPair(T t, RealmChangeListener<T> realmChangeListener) {
            super(t, realmChangeListener);
        }

        /* access modifiers changed from: private */
        public void onChange(T t) {
            if (t != null) {
                ((RealmChangeListener) this.listener).onChange(t);
            }
        }
    }

    public abstract boolean post(Runnable runnable);

    protected RealmNotifier(@Nullable OsSharedRealm osSharedRealm) {
        this.sharedRealm = osSharedRealm;
    }

    /* access modifiers changed from: 0000 */
    public void didChange() {
        this.realmObserverPairs.foreach(this.onChangeCallBack);
        if (!this.transactionCallbacks.isEmpty()) {
            List<Runnable> list = this.transactionCallbacks;
            this.transactionCallbacks = new ArrayList();
            for (Runnable run : list) {
                run.run();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void beforeNotify() {
        this.sharedRealm.invalidateIterators();
    }

    /* access modifiers changed from: 0000 */
    public void willSendNotifications() {
        for (int i = 0; i < this.startSendingNotificationsCallbacks.size(); i++) {
            ((Runnable) this.startSendingNotificationsCallbacks.get(i)).run();
        }
    }

    /* access modifiers changed from: 0000 */
    public void didSendNotifications() {
        for (int i = 0; i < this.startSendingNotificationsCallbacks.size(); i++) {
            ((Runnable) this.finishedSendingNotificationsCallbacks.get(i)).run();
        }
    }

    public void close() {
        removeAllChangeListeners();
        this.startSendingNotificationsCallbacks.clear();
        this.finishedSendingNotificationsCallbacks.clear();
    }

    public <T> void addChangeListener(T t, RealmChangeListener<T> realmChangeListener) {
        this.realmObserverPairs.add(new RealmObserverPair(t, realmChangeListener));
    }

    public <E> void removeChangeListener(E e, RealmChangeListener<E> realmChangeListener) {
        this.realmObserverPairs.remove(e, realmChangeListener);
    }

    public <E> void removeChangeListeners(E e) {
        this.realmObserverPairs.removeByObserver(e);
    }

    private void removeAllChangeListeners() {
        this.realmObserverPairs.clear();
    }

    public void addTransactionCallback(Runnable runnable) {
        this.transactionCallbacks.add(runnable);
    }

    public int getListenersListSize() {
        return this.realmObserverPairs.size();
    }

    public void addBeginSendingNotificationsCallback(Runnable runnable) {
        this.startSendingNotificationsCallbacks.add(runnable);
    }

    public void addFinishedSendingNotificationsCallback(Runnable runnable) {
        this.finishedSendingNotificationsCallbacks.add(runnable);
    }
}

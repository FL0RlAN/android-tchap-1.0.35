package io.realm.internal.sync;

import io.realm.RealmChangeListener;
import io.realm.internal.NativeObject;
import io.realm.internal.ObserverPairList;
import io.realm.internal.ObserverPairList.ObserverPair;
import io.realm.internal.OsResults;
import javax.annotation.Nullable;

public class OsSubscription implements NativeObject {
    private static final long nativeFinalizerPtr = nativeGetFinalizerPtr();
    private final long nativePtr;
    protected final ObserverPairList<SubscriptionObserverPair> observerPairs = new ObserverPairList<>();

    private static class Callback implements io.realm.internal.ObserverPairList.Callback<SubscriptionObserverPair> {
        private Callback() {
        }

        public void onCalled(SubscriptionObserverPair subscriptionObserverPair, Object obj) {
            subscriptionObserverPair.onChange((OsSubscription) obj);
        }
    }

    private static class SubscriptionObserverPair extends ObserverPair<OsSubscription, RealmChangeListener<OsSubscription>> {
        public SubscriptionObserverPair(OsSubscription osSubscription, RealmChangeListener<OsSubscription> realmChangeListener) {
            super(osSubscription, realmChangeListener);
        }

        public void onChange(OsSubscription osSubscription) {
            ((RealmChangeListener) this.listener).onChange(osSubscription);
        }
    }

    public enum SubscriptionState {
        ERROR(-1),
        CREATING(2),
        PENDING(0),
        COMPLETE(1),
        INVALIDATED(3);
        
        private final int val;

        private SubscriptionState(int i) {
            this.val = i;
        }

        public static SubscriptionState fromInternalValue(int i) {
            SubscriptionState[] values;
            for (SubscriptionState subscriptionState : values()) {
                if (subscriptionState.val == i) {
                    return subscriptionState;
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Unknown value: ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    private static native long nativeCreateOrUpdate(long j, String str, long j2, boolean z);

    private static native Object nativeGetError(long j);

    private static native long nativeGetFinalizerPtr();

    private static native int nativeGetState(long j);

    private native void nativeStartListening(long j);

    private native void nativeStopListening(long j);

    public OsSubscription(OsResults osResults, SubscriptionAction subscriptionAction) {
        this.nativePtr = nativeCreateOrUpdate(osResults.getNativePtr(), subscriptionAction.getName(), subscriptionAction.getTimeToLiveMs(), subscriptionAction.isUpdate());
    }

    public long getNativePtr() {
        return this.nativePtr;
    }

    public long getNativeFinalizerPtr() {
        return nativeFinalizerPtr;
    }

    public SubscriptionState getState() {
        return SubscriptionState.fromInternalValue(nativeGetState(this.nativePtr));
    }

    @Nullable
    public Throwable getError() {
        return (Throwable) nativeGetError(this.nativePtr);
    }

    public void addChangeListener(RealmChangeListener<OsSubscription> realmChangeListener) {
        if (this.observerPairs.isEmpty()) {
            nativeStartListening(this.nativePtr);
        }
        this.observerPairs.add(new SubscriptionObserverPair(this, realmChangeListener));
    }

    public void removeChangeListener(RealmChangeListener<OsSubscription> realmChangeListener) {
        this.observerPairs.remove(this, realmChangeListener);
        if (this.observerPairs.isEmpty()) {
            nativeStopListening(this.nativePtr);
        }
    }

    private void notifyChangeListeners() {
        this.observerPairs.foreach(new Callback());
    }
}

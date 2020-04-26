package io.realm.internal;

import io.realm.OrderedCollectionChangeSet.Range;
import io.realm.OrderedCollectionChangeSet.State;
import io.realm.internal.sync.OsSubscription;
import io.realm.internal.sync.OsSubscription.SubscriptionState;
import javax.annotation.Nullable;

public class EmptyLoadChangeSet extends OsCollectionChangeSet {
    private static final int[] NO_INDEX_CHANGES = new int[0];
    private static final Range[] NO_RANGE_CHANGES = new Range[0];

    public EmptyLoadChangeSet(@Nullable OsSubscription osSubscription, boolean z, boolean z2) {
        super(0, z, osSubscription, z2);
    }

    public EmptyLoadChangeSet(@Nullable OsSubscription osSubscription, boolean z) {
        super(0, true, osSubscription, z);
    }

    public State getState() {
        return State.INITIAL;
    }

    public int[] getDeletions() {
        return NO_INDEX_CHANGES;
    }

    public int[] getInsertions() {
        return NO_INDEX_CHANGES;
    }

    public int[] getChanges() {
        return NO_INDEX_CHANGES;
    }

    public Range[] getDeletionRanges() {
        return NO_RANGE_CHANGES;
    }

    public Range[] getInsertionRanges() {
        return NO_RANGE_CHANGES;
    }

    public Range[] getChangeRanges() {
        return NO_RANGE_CHANGES;
    }

    public Throwable getError() {
        if (this.subscription == null || this.subscription.getState() != SubscriptionState.ERROR) {
            return null;
        }
        return this.subscription.getError();
    }

    public boolean isRemoteDataLoaded() {
        return super.isRemoteDataLoaded();
    }

    public boolean isCompleteResult() {
        return isRemoteDataLoaded();
    }

    public boolean isFirstAsyncCallback() {
        return super.isFirstAsyncCallback();
    }

    public boolean isEmpty() {
        return this.subscription == null;
    }

    public String toString() {
        return super.toString();
    }

    public long getNativePtr() {
        return super.getNativePtr();
    }

    public long getNativeFinalizerPtr() {
        return super.getNativeFinalizerPtr();
    }
}

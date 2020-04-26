package io.realm.internal;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedCollectionChangeSet.Range;
import io.realm.OrderedCollectionChangeSet.State;
import io.realm.internal.sync.OsSubscription;
import io.realm.internal.sync.OsSubscription.SubscriptionState;
import java.util.Arrays;
import javax.annotation.Nullable;

public class OsCollectionChangeSet implements OrderedCollectionChangeSet, NativeObject {
    public static final int MAX_ARRAY_LENGTH = 2147483639;
    public static final int TYPE_DELETION = 0;
    public static final int TYPE_INSERTION = 1;
    public static final int TYPE_MODIFICATION = 2;
    private static long finalizerPtr = nativeGetFinalizerPtr();
    private final boolean firstAsyncCallback;
    protected final boolean isPartialRealm;
    private final long nativePtr;
    protected final OsSubscription subscription;

    private static native long nativeGetFinalizerPtr();

    private static native int[] nativeGetIndices(long j, int i);

    private static native int[] nativeGetRanges(long j, int i);

    public OsCollectionChangeSet(long j, boolean z) {
        this(j, z, null, false);
    }

    public OsCollectionChangeSet(long j, boolean z, @Nullable OsSubscription osSubscription, boolean z2) {
        this.nativePtr = j;
        this.firstAsyncCallback = z;
        this.subscription = osSubscription;
        this.isPartialRealm = z2;
        NativeContext.dummyContext.addReference(this);
    }

    public State getState() {
        throw new UnsupportedOperationException("This method should be overridden in a subclass");
    }

    public int[] getDeletions() {
        return nativeGetIndices(this.nativePtr, 0);
    }

    public int[] getInsertions() {
        return nativeGetIndices(this.nativePtr, 1);
    }

    public int[] getChanges() {
        return nativeGetIndices(this.nativePtr, 2);
    }

    public Range[] getDeletionRanges() {
        return longArrayToRangeArray(nativeGetRanges(this.nativePtr, 0));
    }

    public Range[] getInsertionRanges() {
        return longArrayToRangeArray(nativeGetRanges(this.nativePtr, 1));
    }

    public Range[] getChangeRanges() {
        return longArrayToRangeArray(nativeGetRanges(this.nativePtr, 2));
    }

    public Throwable getError() {
        OsSubscription osSubscription = this.subscription;
        if (osSubscription == null || osSubscription.getState() != SubscriptionState.ERROR) {
            return null;
        }
        return this.subscription.getError();
    }

    public boolean isCompleteResult() {
        throw new UnsupportedOperationException("This method should be overridden in a subclass");
    }

    public boolean isRemoteDataLoaded() {
        boolean z = true;
        if (!this.isPartialRealm) {
            return true;
        }
        OsSubscription osSubscription = this.subscription;
        if (osSubscription == null) {
            return false;
        }
        if (osSubscription.getState() != SubscriptionState.COMPLETE) {
            z = false;
        }
        return z;
    }

    public boolean isFirstAsyncCallback() {
        return this.firstAsyncCallback;
    }

    public boolean isEmpty() {
        return this.nativePtr == 0;
    }

    private Range[] longArrayToRangeArray(int[] iArr) {
        if (iArr == null) {
            return new Range[0];
        }
        Range[] rangeArr = new Range[(iArr.length / 2)];
        for (int i = 0; i < rangeArr.length; i++) {
            int i2 = i * 2;
            rangeArr[i] = new Range(iArr[i2], iArr[i2 + 1]);
        }
        return rangeArr;
    }

    public String toString() {
        if (this.nativePtr == 0) {
            return "Change set is empty.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Deletion Ranges: ");
        sb.append(Arrays.toString(getDeletionRanges()));
        sb.append("\nInsertion Ranges: ");
        sb.append(Arrays.toString(getInsertionRanges()));
        sb.append("\nChange Ranges: ");
        sb.append(Arrays.toString(getChangeRanges()));
        return sb.toString();
    }

    public long getNativePtr() {
        return this.nativePtr;
    }

    public long getNativeFinalizerPtr() {
        return finalizerPtr;
    }
}

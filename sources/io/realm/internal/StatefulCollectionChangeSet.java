package io.realm.internal;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedCollectionChangeSet.Range;
import io.realm.OrderedCollectionChangeSet.State;
import javax.annotation.Nullable;

public class StatefulCollectionChangeSet implements OrderedCollectionChangeSet {
    private final OrderedCollectionChangeSet changeset;
    private final Throwable error;
    private final boolean remoteDataSynchronized;
    private final State state;

    public StatefulCollectionChangeSet(OsCollectionChangeSet osCollectionChangeSet) {
        this.changeset = osCollectionChangeSet;
        boolean isFirstAsyncCallback = osCollectionChangeSet.isFirstAsyncCallback();
        this.remoteDataSynchronized = osCollectionChangeSet.isRemoteDataLoaded();
        this.error = osCollectionChangeSet.getError();
        if (this.error != null) {
            this.state = State.ERROR;
        } else {
            this.state = isFirstAsyncCallback ? State.INITIAL : State.UPDATE;
        }
    }

    public State getState() {
        return this.state;
    }

    public int[] getDeletions() {
        return this.changeset.getDeletions();
    }

    public int[] getInsertions() {
        return this.changeset.getInsertions();
    }

    public int[] getChanges() {
        return this.changeset.getChanges();
    }

    public Range[] getDeletionRanges() {
        return this.changeset.getDeletionRanges();
    }

    public Range[] getInsertionRanges() {
        return this.changeset.getInsertionRanges();
    }

    public Range[] getChangeRanges() {
        return this.changeset.getChangeRanges();
    }

    @Nullable
    public Throwable getError() {
        return this.error;
    }

    public boolean isCompleteResult() {
        return this.remoteDataSynchronized;
    }
}

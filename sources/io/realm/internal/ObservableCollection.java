package io.realm.internal;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmChangeListener;
import io.realm.internal.ObserverPairList.ObserverPair;
import javax.annotation.Nullable;

interface ObservableCollection {

    public static class Callback implements io.realm.internal.ObserverPairList.Callback<CollectionObserverPair> {
        private final OsCollectionChangeSet changeSet;

        Callback(OsCollectionChangeSet osCollectionChangeSet) {
            this.changeSet = osCollectionChangeSet;
        }

        public void onCalled(CollectionObserverPair collectionObserverPair, Object obj) {
            collectionObserverPair.onChange(obj, this.changeSet);
        }
    }

    public static class CollectionObserverPair<T> extends ObserverPair<T, Object> {
        public CollectionObserverPair(T t, Object obj) {
            super(t, obj);
        }

        public void onChange(T t, OsCollectionChangeSet osCollectionChangeSet) {
            if (this.listener instanceof OrderedRealmCollectionChangeListener) {
                ((OrderedRealmCollectionChangeListener) this.listener).onChange(t, new StatefulCollectionChangeSet(osCollectionChangeSet));
            } else if (this.listener instanceof RealmChangeListener) {
                ((RealmChangeListener) this.listener).onChange(t);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Unsupported listener type: ");
                sb.append(this.listener);
                throw new RuntimeException(sb.toString());
            }
        }
    }

    public static class RealmChangeListenerWrapper<T> implements OrderedRealmCollectionChangeListener<T> {
        private final RealmChangeListener<T> listener;

        RealmChangeListenerWrapper(RealmChangeListener<T> realmChangeListener) {
            this.listener = realmChangeListener;
        }

        public void onChange(T t, @Nullable OrderedCollectionChangeSet orderedCollectionChangeSet) {
            this.listener.onChange(t);
        }

        public boolean equals(Object obj) {
            return (obj instanceof RealmChangeListenerWrapper) && this.listener == ((RealmChangeListenerWrapper) obj).listener;
        }

        public int hashCode() {
            return this.listener.hashCode();
        }
    }

    void notifyChangeListeners(long j);
}

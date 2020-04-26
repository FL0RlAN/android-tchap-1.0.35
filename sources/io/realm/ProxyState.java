package io.realm;

import io.realm.RealmModel;
import io.realm.internal.ObserverPairList;
import io.realm.internal.ObserverPairList.Callback;
import io.realm.internal.OsObject;
import io.realm.internal.OsObject.ObjectObserverPair;
import io.realm.internal.PendingRow;
import io.realm.internal.PendingRow.FrontEnd;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.Row;
import io.realm.internal.UncheckedRow;
import java.util.List;
import javax.annotation.Nullable;

public final class ProxyState<E extends RealmModel> implements FrontEnd {
    private static QueryCallback queryCallback = new QueryCallback();
    private boolean acceptDefaultValue;
    private List<String> excludeFields;
    private E model;
    private ObserverPairList<ObjectObserverPair> observerPairs = new ObserverPairList<>();
    private OsObject osObject;
    private BaseRealm realm;
    private Row row;
    private boolean underConstruction = true;

    private static class QueryCallback implements Callback<ObjectObserverPair> {
        private QueryCallback() {
        }

        public void onCalled(ObjectObserverPair objectObserverPair, Object obj) {
            objectObserverPair.onChange((RealmModel) obj, null);
        }
    }

    static class RealmChangeListenerWrapper<T extends RealmModel> implements RealmObjectChangeListener<T> {
        private final RealmChangeListener<T> listener;

        RealmChangeListenerWrapper(RealmChangeListener<T> realmChangeListener) {
            if (realmChangeListener != null) {
                this.listener = realmChangeListener;
                return;
            }
            throw new IllegalArgumentException("Listener should not be null");
        }

        public void onChange(T t, @Nullable ObjectChangeSet objectChangeSet) {
            this.listener.onChange(t);
        }

        public boolean equals(Object obj) {
            return (obj instanceof RealmChangeListenerWrapper) && this.listener == ((RealmChangeListenerWrapper) obj).listener;
        }

        public int hashCode() {
            return this.listener.hashCode();
        }
    }

    public ProxyState() {
    }

    public ProxyState(E e) {
        this.model = e;
    }

    public BaseRealm getRealm$realm() {
        return this.realm;
    }

    public void setRealm$realm(BaseRealm baseRealm) {
        this.realm = baseRealm;
    }

    public Row getRow$realm() {
        return this.row;
    }

    public void setRow$realm(Row row2) {
        this.row = row2;
    }

    public boolean getAcceptDefaultValue$realm() {
        return this.acceptDefaultValue;
    }

    public void setAcceptDefaultValue$realm(boolean z) {
        this.acceptDefaultValue = z;
    }

    public List<String> getExcludeFields$realm() {
        return this.excludeFields;
    }

    public void setExcludeFields$realm(List<String> list) {
        this.excludeFields = list;
    }

    private void notifyQueryFinished() {
        this.observerPairs.foreach(queryCallback);
    }

    public void addChangeListener(RealmObjectChangeListener<E> realmObjectChangeListener) {
        Row row2 = this.row;
        if (row2 instanceof PendingRow) {
            this.observerPairs.add(new ObjectObserverPair(this.model, realmObjectChangeListener));
        } else if (row2 instanceof UncheckedRow) {
            registerToObjectNotifier();
            OsObject osObject2 = this.osObject;
            if (osObject2 != null) {
                osObject2.addListener(this.model, realmObjectChangeListener);
            }
        }
    }

    public void removeChangeListener(RealmObjectChangeListener<E> realmObjectChangeListener) {
        OsObject osObject2 = this.osObject;
        if (osObject2 != null) {
            osObject2.removeListener(this.model, realmObjectChangeListener);
        } else {
            this.observerPairs.remove(this.model, realmObjectChangeListener);
        }
    }

    public void removeAllChangeListeners() {
        OsObject osObject2 = this.osObject;
        if (osObject2 != null) {
            osObject2.removeListener(this.model);
        } else {
            this.observerPairs.clear();
        }
    }

    public boolean isUnderConstruction() {
        return this.underConstruction;
    }

    public void setConstructionFinished() {
        this.underConstruction = false;
        this.excludeFields = null;
    }

    private void registerToObjectNotifier() {
        if (this.realm.sharedRealm != null && !this.realm.sharedRealm.isClosed() && this.row.isAttached() && this.osObject == null) {
            this.osObject = new OsObject(this.realm.sharedRealm, (UncheckedRow) this.row);
            this.osObject.setObserverPairs(this.observerPairs);
            this.observerPairs = null;
        }
    }

    public boolean isLoaded() {
        return !(this.row instanceof PendingRow);
    }

    public void load() {
        Row row2 = this.row;
        if (row2 instanceof PendingRow) {
            ((PendingRow) row2).executeQuery();
        }
    }

    public void onQueryFinished(Row row2) {
        this.row = row2;
        notifyQueryFinished();
        if (row2.isAttached()) {
            registerToObjectNotifier();
        }
    }

    public void checkValidObject(RealmModel realmModel) {
        if (!RealmObject.isValid(realmModel) || !RealmObject.isManaged(realmModel)) {
            throw new IllegalArgumentException("'value' is not a valid managed object.");
        } else if (((RealmObjectProxy) realmModel).realmGet$proxyState().getRealm$realm() != getRealm$realm()) {
            throw new IllegalArgumentException("'value' belongs to a different Realm.");
        }
    }
}

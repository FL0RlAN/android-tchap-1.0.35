package io.realm;

import io.realm.internal.ManagableObject;
import io.realm.internal.Row;
import io.realm.internal.Table;
import javax.annotation.Nullable;

public abstract class MutableRealmInteger implements Comparable<MutableRealmInteger>, ManagableObject {

    static abstract class Managed<T extends RealmModel> extends MutableRealmInteger {
        /* access modifiers changed from: protected */
        public abstract long getColumnIndex();

        /* access modifiers changed from: protected */
        public abstract ProxyState<T> getProxyState();

        public final boolean isManaged() {
            return true;
        }

        public /* bridge */ /* synthetic */ int compareTo(Object obj) {
            return MutableRealmInteger.super.compareTo((MutableRealmInteger) obj);
        }

        Managed() {
        }

        public final boolean isValid() {
            return !getRealm().isClosed() && getRow().isAttached();
        }

        public final Long get() {
            Row row = getRow();
            row.checkIfAttached();
            long columnIndex = getColumnIndex();
            if (row.isNull(columnIndex)) {
                return null;
            }
            return Long.valueOf(row.getLong(columnIndex));
        }

        public final void set(@Nullable Long l) {
            ProxyState proxyState = getProxyState();
            proxyState.getRealm$realm().checkIfValidAndInTransaction();
            if (!proxyState.isUnderConstruction()) {
                setValue(l, false);
            } else if (proxyState.getAcceptDefaultValue$realm()) {
                setValue(l, true);
            }
        }

        public final void increment(long j) {
            getRealm().checkIfValidAndInTransaction();
            Row row = getRow();
            row.getTable().incrementLong(getColumnIndex(), row.getIndex(), j);
        }

        public final void decrement(long j) {
            increment(-j);
        }

        private BaseRealm getRealm() {
            return getProxyState().getRealm$realm();
        }

        private Row getRow() {
            return getProxyState().getRow$realm();
        }

        private void setValue(@Nullable Long l, boolean z) {
            Row row = getRow();
            Table table = row.getTable();
            long index = row.getIndex();
            long columnIndex = getColumnIndex();
            if (l == null) {
                table.setNull(columnIndex, index, z);
            } else {
                table.setLong(columnIndex, index, l.longValue(), z);
            }
        }
    }

    private static final class Unmanaged extends MutableRealmInteger {
        @Nullable
        private Long value;

        public boolean isManaged() {
            return false;
        }

        public boolean isValid() {
            return true;
        }

        public /* bridge */ /* synthetic */ int compareTo(Object obj) {
            return MutableRealmInteger.super.compareTo((MutableRealmInteger) obj);
        }

        Unmanaged(@Nullable Long l) {
            this.value = l;
        }

        public void set(@Nullable Long l) {
            this.value = l;
        }

        @Nullable
        public Long get() {
            return this.value;
        }

        public void increment(long j) {
            Long l = this.value;
            if (l != null) {
                this.value = Long.valueOf(l.longValue() + j);
                return;
            }
            throw new IllegalStateException("Cannot increment a MutableRealmInteger whose value is null. Set its value first.");
        }

        public void decrement(long j) {
            increment(-j);
        }
    }

    public abstract void decrement(long j);

    @Nullable
    public abstract Long get();

    public abstract void increment(long j);

    public abstract void set(@Nullable Long l);

    public static MutableRealmInteger valueOf(Long l) {
        return new Unmanaged(l);
    }

    public static MutableRealmInteger ofNull() {
        return new Unmanaged(null);
    }

    public static MutableRealmInteger valueOf(long j) {
        return valueOf(Long.valueOf(j));
    }

    public static MutableRealmInteger valueOf(String str) {
        return valueOf(Long.parseLong(str));
    }

    MutableRealmInteger() {
    }

    public final void set(long j) {
        set(Long.valueOf(j));
    }

    public final boolean isNull() {
        return get() == null;
    }

    public final int compareTo(MutableRealmInteger mutableRealmInteger) {
        Long l = get();
        Long l2 = mutableRealmInteger.get();
        if (l == null) {
            return l2 == null ? 0 : -1;
        }
        if (l2 == null) {
            return 1;
        }
        return l.compareTo(l2);
    }

    public final int hashCode() {
        Long l = get();
        if (l == null) {
            return 0;
        }
        return l.hashCode();
    }

    public final boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MutableRealmInteger)) {
            return false;
        }
        Long l = get();
        Long l2 = ((MutableRealmInteger) obj).get();
        if (l != null) {
            z = l.equals(l2);
        } else if (l2 != null) {
            z = false;
        }
        return z;
    }
}

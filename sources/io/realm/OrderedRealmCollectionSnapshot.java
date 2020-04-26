package io.realm;

import io.realm.internal.OsResults;
import io.realm.internal.UncheckedRow;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Locale;
import javax.annotation.Nullable;

public class OrderedRealmCollectionSnapshot<E> extends OrderedRealmCollectionImpl<E> {
    private int size = -1;

    public boolean isLoaded() {
        return true;
    }

    public boolean load() {
        return true;
    }

    @Deprecated
    public /* bridge */ /* synthetic */ void add(int i, Object obj) {
        super.add(i, obj);
    }

    @Deprecated
    public /* bridge */ /* synthetic */ boolean add(Object obj) {
        return super.add(obj);
    }

    @Deprecated
    public /* bridge */ /* synthetic */ boolean addAll(int i, Collection collection) {
        return super.addAll(i, collection);
    }

    @Deprecated
    public /* bridge */ /* synthetic */ boolean addAll(Collection collection) {
        return super.addAll(collection);
    }

    public /* bridge */ /* synthetic */ double average(String str) {
        return super.average(str);
    }

    @Deprecated
    public /* bridge */ /* synthetic */ void clear() {
        super.clear();
    }

    public /* bridge */ /* synthetic */ boolean contains(@Nullable Object obj) {
        return super.contains(obj);
    }

    @Nullable
    public /* bridge */ /* synthetic */ Object first() {
        return super.first();
    }

    @Nullable
    public /* bridge */ /* synthetic */ Object first(@Nullable Object obj) {
        return super.first(obj);
    }

    @Nullable
    public /* bridge */ /* synthetic */ Object get(int i) {
        return super.get(i);
    }

    public /* bridge */ /* synthetic */ Realm getRealm() {
        return super.getRealm();
    }

    public /* bridge */ /* synthetic */ boolean isManaged() {
        return super.isManaged();
    }

    public /* bridge */ /* synthetic */ boolean isValid() {
        return super.isValid();
    }

    public /* bridge */ /* synthetic */ Iterator iterator() {
        return super.iterator();
    }

    @Nullable
    public /* bridge */ /* synthetic */ Object last() {
        return super.last();
    }

    @Nullable
    public /* bridge */ /* synthetic */ Object last(@Nullable Object obj) {
        return super.last(obj);
    }

    public /* bridge */ /* synthetic */ ListIterator listIterator() {
        return super.listIterator();
    }

    public /* bridge */ /* synthetic */ ListIterator listIterator(int i) {
        return super.listIterator(i);
    }

    public /* bridge */ /* synthetic */ Number max(String str) {
        return super.max(str);
    }

    @Nullable
    public /* bridge */ /* synthetic */ Date maxDate(String str) {
        return super.maxDate(str);
    }

    public /* bridge */ /* synthetic */ Number min(String str) {
        return super.min(str);
    }

    public /* bridge */ /* synthetic */ Date minDate(String str) {
        return super.minDate(str);
    }

    @Deprecated
    public /* bridge */ /* synthetic */ Object remove(int i) {
        return super.remove(i);
    }

    @Deprecated
    public /* bridge */ /* synthetic */ boolean remove(Object obj) {
        return super.remove(obj);
    }

    @Deprecated
    public /* bridge */ /* synthetic */ boolean removeAll(Collection collection) {
        return super.removeAll(collection);
    }

    @Deprecated
    public /* bridge */ /* synthetic */ boolean retainAll(Collection collection) {
        return super.retainAll(collection);
    }

    @Deprecated
    public /* bridge */ /* synthetic */ Object set(int i, Object obj) {
        return super.set(i, obj);
    }

    public /* bridge */ /* synthetic */ Number sum(String str) {
        return super.sum(str);
    }

    OrderedRealmCollectionSnapshot(BaseRealm baseRealm, OsResults osResults, Class<E> cls) {
        super(baseRealm, osResults.createSnapshot(), cls);
    }

    OrderedRealmCollectionSnapshot(BaseRealm baseRealm, OsResults osResults, String str) {
        super(baseRealm, osResults.createSnapshot(), str);
    }

    public int size() {
        if (this.size == -1) {
            this.size = super.size();
        }
        return this.size;
    }

    public RealmResults<E> sort(String str) {
        throw getUnsupportedException("sort");
    }

    public RealmResults<E> sort(String str, Sort sort) {
        throw getUnsupportedException("sort");
    }

    public RealmResults<E> sort(String str, Sort sort, String str2, Sort sort2) {
        throw getUnsupportedException("sort");
    }

    public RealmResults<E> sort(String[] strArr, Sort[] sortArr) {
        throw getUnsupportedException("sort");
    }

    @Deprecated
    public RealmQuery<E> where() {
        throw getUnsupportedException("where");
    }

    private UnsupportedOperationException getUnsupportedException(String str) {
        return new UnsupportedOperationException(String.format(Locale.US, "'%s()' is not supported by OrderedRealmCollectionSnapshot. Call '%s()' on the original 'RealmCollection' instead.", new Object[]{str, str}));
    }

    public OrderedRealmCollectionSnapshot<E> createSnapshot() {
        this.realm.checkIfValid();
        return this;
    }

    public void deleteFromRealm(int i) {
        this.realm.checkIfValidAndInTransaction();
        if (this.osResults.getUncheckedRow(i).isAttached()) {
            this.osResults.delete((long) i);
        }
    }

    public boolean deleteFirstFromRealm() {
        this.realm.checkIfValidAndInTransaction();
        UncheckedRow firstUncheckedRow = this.osResults.firstUncheckedRow();
        return firstUncheckedRow != null && firstUncheckedRow.isAttached() && this.osResults.deleteFirst();
    }

    public boolean deleteLastFromRealm() {
        this.realm.checkIfValidAndInTransaction();
        UncheckedRow lastUncheckedRow = this.osResults.lastUncheckedRow();
        return lastUncheckedRow != null && lastUncheckedRow.isAttached() && this.osResults.deleteLast();
    }

    public boolean deleteAllFromRealm() {
        return super.deleteAllFromRealm();
    }
}

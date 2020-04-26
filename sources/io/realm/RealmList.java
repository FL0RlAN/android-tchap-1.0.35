package io.realm;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.realm.internal.InvalidRow;
import io.realm.internal.OsList;
import io.realm.internal.OsResults;
import io.realm.internal.RealmObjectProxy;
import io.realm.rx.CollectionChange;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RealmList<E> extends AbstractList<E> implements OrderedRealmCollection<E> {
    static final String ALLOWED_ONLY_FOR_REALM_MODEL_ELEMENT_MESSAGE = "This feature is available only when the element type is implementing RealmModel.";
    private static final String ONLY_IN_MANAGED_MODE_MESSAGE = "This method is only available in managed mode.";
    private static final String REMOVE_OUTSIDE_TRANSACTION_ERROR = "Objects can only be removed from inside a write transaction.";
    @Nullable
    protected String className;
    @Nullable
    protected Class<E> clazz;
    private final ManagedListOperator<E> osListOperator;
    protected final BaseRealm realm;
    private List<E> unmanagedList;

    private class RealmItr implements Iterator<E> {
        int cursor;
        int expectedModCount;
        int lastRet;

        private RealmItr() {
            this.cursor = 0;
            this.lastRet = -1;
            this.expectedModCount = RealmList.this.modCount;
        }

        public boolean hasNext() {
            RealmList.this.checkValidRealm();
            checkConcurrentModification();
            return this.cursor != RealmList.this.size();
        }

        @Nullable
        public E next() {
            RealmList.this.checkValidRealm();
            checkConcurrentModification();
            int i = this.cursor;
            try {
                E e = RealmList.this.get(i);
                this.lastRet = i;
                this.cursor = i + 1;
                return e;
            } catch (IndexOutOfBoundsException unused) {
                checkConcurrentModification();
                StringBuilder sb = new StringBuilder();
                sb.append("Cannot access index ");
                sb.append(i);
                sb.append(" when size is ");
                sb.append(RealmList.this.size());
                sb.append(". Remember to check hasNext() before using next().");
                throw new NoSuchElementException(sb.toString());
            }
        }

        public void remove() {
            RealmList.this.checkValidRealm();
            if (this.lastRet >= 0) {
                checkConcurrentModification();
                try {
                    RealmList.this.remove(this.lastRet);
                    if (this.lastRet < this.cursor) {
                        this.cursor--;
                    }
                    this.lastRet = -1;
                    this.expectedModCount = RealmList.this.modCount;
                } catch (IndexOutOfBoundsException unused) {
                    throw new ConcurrentModificationException();
                }
            } else {
                throw new IllegalStateException("Cannot call remove() twice. Must call next() in between.");
            }
        }

        /* access modifiers changed from: 0000 */
        public final void checkConcurrentModification() {
            if (RealmList.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    private class RealmListItr extends RealmItr implements ListIterator<E> {
        RealmListItr(int i) {
            super();
            if (i < 0 || i > RealmList.this.size()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Starting location must be a valid index: [0, ");
                sb.append(RealmList.this.size() - 1);
                sb.append("]. Index was ");
                sb.append(i);
                throw new IndexOutOfBoundsException(sb.toString());
            }
            this.cursor = i;
        }

        public boolean hasPrevious() {
            return this.cursor != 0;
        }

        @Nullable
        public E previous() {
            checkConcurrentModification();
            int i = this.cursor - 1;
            try {
                E e = RealmList.this.get(i);
                this.cursor = i;
                this.lastRet = i;
                return e;
            } catch (IndexOutOfBoundsException unused) {
                checkConcurrentModification();
                StringBuilder sb = new StringBuilder();
                sb.append("Cannot access index less than zero. This was ");
                sb.append(i);
                sb.append(". Remember to check hasPrevious() before using previous().");
                throw new NoSuchElementException(sb.toString());
            }
        }

        public int nextIndex() {
            return this.cursor;
        }

        public int previousIndex() {
            return this.cursor - 1;
        }

        public void set(@Nullable E e) {
            RealmList.this.realm.checkIfValid();
            if (this.lastRet >= 0) {
                checkConcurrentModification();
                try {
                    RealmList.this.set(this.lastRet, e);
                    this.expectedModCount = RealmList.this.modCount;
                } catch (IndexOutOfBoundsException unused) {
                    throw new ConcurrentModificationException();
                }
            } else {
                throw new IllegalStateException();
            }
        }

        public void add(@Nullable E e) {
            RealmList.this.realm.checkIfValid();
            checkConcurrentModification();
            try {
                int i = this.cursor;
                RealmList.this.add(i, e);
                this.lastRet = -1;
                this.cursor = i + 1;
                this.expectedModCount = RealmList.this.modCount;
            } catch (IndexOutOfBoundsException unused) {
                throw new ConcurrentModificationException();
            }
        }
    }

    public boolean isLoaded() {
        return true;
    }

    public boolean load() {
        return true;
    }

    public RealmList() {
        this.realm = null;
        this.osListOperator = null;
        this.unmanagedList = new ArrayList();
    }

    public RealmList(E... eArr) {
        if (eArr != null) {
            this.realm = null;
            this.osListOperator = null;
            this.unmanagedList = new ArrayList(eArr.length);
            Collections.addAll(this.unmanagedList, eArr);
            return;
        }
        throw new IllegalArgumentException("The objects argument cannot be null");
    }

    RealmList(Class<E> cls, OsList osList, BaseRealm baseRealm) {
        this.clazz = cls;
        this.osListOperator = getOperator(baseRealm, osList, cls, null);
        this.realm = baseRealm;
    }

    RealmList(String str, OsList osList, BaseRealm baseRealm) {
        this.realm = baseRealm;
        this.className = str;
        this.osListOperator = getOperator(baseRealm, osList, null, str);
    }

    /* access modifiers changed from: 0000 */
    public OsList getOsList() {
        return this.osListOperator.getOsList();
    }

    public boolean isValid() {
        BaseRealm baseRealm = this.realm;
        if (baseRealm == null) {
            return true;
        }
        if (baseRealm.isClosed()) {
            return false;
        }
        return isAttached();
    }

    public boolean isManaged() {
        return this.realm != null;
    }

    private boolean isAttached() {
        ManagedListOperator<E> managedListOperator = this.osListOperator;
        return managedListOperator != null && managedListOperator.isValid();
    }

    public void add(int i, @Nullable E e) {
        if (isManaged()) {
            checkValidRealm();
            this.osListOperator.insert(i, e);
        } else {
            this.unmanagedList.add(i, e);
        }
        this.modCount++;
    }

    public boolean add(@Nullable E e) {
        if (isManaged()) {
            checkValidRealm();
            this.osListOperator.append(e);
        } else {
            this.unmanagedList.add(e);
        }
        this.modCount++;
        return true;
    }

    public E set(int i, @Nullable E e) {
        if (!isManaged()) {
            return this.unmanagedList.set(i, e);
        }
        checkValidRealm();
        return this.osListOperator.set(i, e);
    }

    public void move(int i, int i2) {
        if (isManaged()) {
            checkValidRealm();
            this.osListOperator.move(i, i2);
            return;
        }
        int size = this.unmanagedList.size();
        String str = ", size is ";
        String str2 = "Invalid index ";
        if (i < 0 || size <= i) {
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(i);
            sb.append(str);
            sb.append(size);
            throw new IndexOutOfBoundsException(sb.toString());
        } else if (i2 < 0 || size <= i2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            sb2.append(i2);
            sb2.append(str);
            sb2.append(size);
            throw new IndexOutOfBoundsException(sb2.toString());
        } else {
            this.unmanagedList.add(i2, this.unmanagedList.remove(i));
        }
    }

    public void clear() {
        if (isManaged()) {
            checkValidRealm();
            this.osListOperator.removeAll();
        } else {
            this.unmanagedList.clear();
        }
        this.modCount++;
    }

    public E remove(int i) {
        E e;
        if (isManaged()) {
            checkValidRealm();
            e = get(i);
            this.osListOperator.remove(i);
        } else {
            e = this.unmanagedList.remove(i);
        }
        this.modCount++;
        return e;
    }

    public boolean remove(@Nullable Object obj) {
        if (!isManaged() || this.realm.isInTransaction()) {
            return super.remove(obj);
        }
        throw new IllegalStateException(REMOVE_OUTSIDE_TRANSACTION_ERROR);
    }

    public boolean removeAll(Collection<?> collection) {
        if (!isManaged() || this.realm.isInTransaction()) {
            return super.removeAll(collection);
        }
        throw new IllegalStateException(REMOVE_OUTSIDE_TRANSACTION_ERROR);
    }

    public boolean deleteFirstFromRealm() {
        if (!isManaged()) {
            throw new UnsupportedOperationException(ONLY_IN_MANAGED_MODE_MESSAGE);
        } else if (this.osListOperator.isEmpty()) {
            return false;
        } else {
            deleteFromRealm(0);
            this.modCount++;
            return true;
        }
    }

    public boolean deleteLastFromRealm() {
        if (!isManaged()) {
            throw new UnsupportedOperationException(ONLY_IN_MANAGED_MODE_MESSAGE);
        } else if (this.osListOperator.isEmpty()) {
            return false;
        } else {
            this.osListOperator.deleteLast();
            this.modCount++;
            return true;
        }
    }

    @Nullable
    public E get(int i) {
        if (!isManaged()) {
            return this.unmanagedList.get(i);
        }
        checkValidRealm();
        return this.osListOperator.get(i);
    }

    @Nullable
    public E first() {
        return firstImpl(true, null);
    }

    @Nullable
    public E first(@Nullable E e) {
        return firstImpl(false, e);
    }

    @Nullable
    private E firstImpl(boolean z, @Nullable E e) {
        if (isManaged()) {
            checkValidRealm();
            if (!this.osListOperator.isEmpty()) {
                return get(0);
            }
        } else {
            List<E> list = this.unmanagedList;
            if (list != null && !list.isEmpty()) {
                return this.unmanagedList.get(0);
            }
        }
        if (!z) {
            return e;
        }
        throw new IndexOutOfBoundsException("The list is empty.");
    }

    @Nullable
    public E last() {
        return lastImpl(true, null);
    }

    @Nullable
    public E last(@Nullable E e) {
        return lastImpl(false, e);
    }

    @Nullable
    private E lastImpl(boolean z, @Nullable E e) {
        if (isManaged()) {
            checkValidRealm();
            if (!this.osListOperator.isEmpty()) {
                return get(this.osListOperator.size() - 1);
            }
        } else {
            List<E> list = this.unmanagedList;
            if (list != null && !list.isEmpty()) {
                List<E> list2 = this.unmanagedList;
                return list2.get(list2.size() - 1);
            }
        }
        if (!z) {
            return e;
        }
        throw new IndexOutOfBoundsException("The list is empty.");
    }

    public RealmResults<E> sort(String str) {
        return sort(str, Sort.ASCENDING);
    }

    public RealmResults<E> sort(String str, Sort sort) {
        if (isManaged()) {
            return where().sort(str, sort).findAll();
        }
        throw new UnsupportedOperationException(ONLY_IN_MANAGED_MODE_MESSAGE);
    }

    public RealmResults<E> sort(String str, Sort sort, String str2, Sort sort2) {
        return sort(new String[]{str, str2}, new Sort[]{sort, sort2});
    }

    public RealmResults<E> sort(String[] strArr, Sort[] sortArr) {
        if (isManaged()) {
            return where().sort(strArr, sortArr).findAll();
        }
        throw new UnsupportedOperationException(ONLY_IN_MANAGED_MODE_MESSAGE);
    }

    public void deleteFromRealm(int i) {
        if (isManaged()) {
            checkValidRealm();
            this.osListOperator.delete(i);
            this.modCount++;
            return;
        }
        throw new UnsupportedOperationException(ONLY_IN_MANAGED_MODE_MESSAGE);
    }

    public int size() {
        if (!isManaged()) {
            return this.unmanagedList.size();
        }
        checkValidRealm();
        return this.osListOperator.size();
    }

    public RealmQuery<E> where() {
        if (isManaged()) {
            checkValidRealm();
            if (this.osListOperator.forRealmModel()) {
                return RealmQuery.createQueryFromList(this);
            }
            throw new UnsupportedOperationException(ALLOWED_ONLY_FOR_REALM_MODEL_ELEMENT_MESSAGE);
        }
        throw new UnsupportedOperationException(ONLY_IN_MANAGED_MODE_MESSAGE);
    }

    @Nullable
    public Number min(String str) {
        return where().min(str);
    }

    @Nullable
    public Number max(String str) {
        return where().max(str);
    }

    public Number sum(String str) {
        return where().sum(str);
    }

    public double average(String str) {
        return where().average(str);
    }

    @Nullable
    public Date maxDate(String str) {
        return where().maximumDate(str);
    }

    @Nullable
    public Date minDate(String str) {
        return where().minimumDate(str);
    }

    public boolean deleteAllFromRealm() {
        if (isManaged()) {
            checkValidRealm();
            if (this.osListOperator.isEmpty()) {
                return false;
            }
            this.osListOperator.deleteAll();
            this.modCount++;
            return true;
        }
        throw new UnsupportedOperationException(ONLY_IN_MANAGED_MODE_MESSAGE);
    }

    public boolean contains(@Nullable Object obj) {
        if (!isManaged()) {
            return this.unmanagedList.contains(obj);
        }
        this.realm.checkIfValid();
        if (!(obj instanceof RealmObjectProxy) || ((RealmObjectProxy) obj).realmGet$proxyState().getRow$realm() != InvalidRow.INSTANCE) {
            return super.contains(obj);
        }
        return false;
    }

    @Nonnull
    public Iterator<E> iterator() {
        if (isManaged()) {
            return new RealmItr();
        }
        return super.iterator();
    }

    @Nonnull
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Nonnull
    public ListIterator<E> listIterator(int i) {
        if (isManaged()) {
            return new RealmListItr(i);
        }
        return super.listIterator(i);
    }

    /* access modifiers changed from: private */
    public void checkValidRealm() {
        this.realm.checkIfValid();
    }

    public OrderedRealmCollectionSnapshot<E> createSnapshot() {
        if (isManaged()) {
            checkValidRealm();
            if (!this.osListOperator.forRealmModel()) {
                throw new UnsupportedOperationException(ALLOWED_ONLY_FOR_REALM_MODEL_ELEMENT_MESSAGE);
            } else if (this.className != null) {
                BaseRealm baseRealm = this.realm;
                return new OrderedRealmCollectionSnapshot<>(baseRealm, OsResults.createFromQuery(baseRealm.sharedRealm, this.osListOperator.getOsList().getQuery()), this.className);
            } else {
                BaseRealm baseRealm2 = this.realm;
                return new OrderedRealmCollectionSnapshot<>(baseRealm2, OsResults.createFromQuery(baseRealm2.sharedRealm, this.osListOperator.getOsList().getQuery()), this.clazz);
            }
        } else {
            throw new UnsupportedOperationException(ONLY_IN_MANAGED_MODE_MESSAGE);
        }
    }

    public Realm getRealm() {
        BaseRealm baseRealm = this.realm;
        if (baseRealm == null) {
            return null;
        }
        baseRealm.checkIfValid();
        BaseRealm baseRealm2 = this.realm;
        if (baseRealm2 instanceof Realm) {
            return (Realm) baseRealm2;
        }
        throw new IllegalStateException("This method is only available for typed Realms");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        String str = "byte[";
        String str2 = ",";
        int i = 0;
        String str3 = "]";
        if (!isManaged()) {
            sb.append("RealmList<?>@[");
            int size = size();
            while (i < size) {
                Object obj = get(i);
                if (obj instanceof RealmModel) {
                    sb.append(System.identityHashCode(obj));
                } else if (obj instanceof byte[]) {
                    sb.append(str);
                    sb.append(((byte[]) obj).length);
                    sb.append(str3);
                } else {
                    sb.append(obj);
                }
                sb.append(str2);
                i++;
            }
            if (size() > 0) {
                sb.setLength(sb.length() - 1);
            }
            sb.append(str3);
        } else {
            sb.append("RealmList<");
            String str4 = this.className;
            if (str4 != null) {
                sb.append(str4);
            } else if (isClassForRealmModel(this.clazz)) {
                sb.append(this.realm.getSchema().getSchemaForClass(this.clazz).getClassName());
            } else {
                Class<E> cls = this.clazz;
                if (cls == byte[].class) {
                    sb.append(cls.getSimpleName());
                } else {
                    sb.append(cls.getName());
                }
            }
            sb.append(">@[");
            if (!isAttached()) {
                sb.append("invalid");
            } else if (isClassForRealmModel(this.clazz)) {
                while (i < size()) {
                    sb.append(((RealmObjectProxy) get(i)).realmGet$proxyState().getRow$realm().getIndex());
                    sb.append(str2);
                    i++;
                }
                if (size() > 0) {
                    sb.setLength(sb.length() - 1);
                }
            } else {
                while (i < size()) {
                    Object obj2 = get(i);
                    if (obj2 instanceof byte[]) {
                        sb.append(str);
                        sb.append(((byte[]) obj2).length);
                        sb.append(str3);
                    } else {
                        sb.append(obj2);
                    }
                    sb.append(str2);
                    i++;
                }
                if (size() > 0) {
                    sb.setLength(sb.length() - 1);
                }
            }
            sb.append(str3);
        }
        return sb.toString();
    }

    public Flowable<RealmList<E>> asFlowable() {
        BaseRealm baseRealm = this.realm;
        if (baseRealm instanceof Realm) {
            return baseRealm.configuration.getRxFactory().from((Realm) this.realm, this);
        }
        if (baseRealm instanceof DynamicRealm) {
            return baseRealm.configuration.getRxFactory().from((DynamicRealm) this.realm, this);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.realm.getClass());
        sb.append(" does not support RxJava2.");
        throw new UnsupportedOperationException(sb.toString());
    }

    public Observable<CollectionChange<RealmList<E>>> asChangesetObservable() {
        BaseRealm baseRealm = this.realm;
        if (baseRealm instanceof Realm) {
            return baseRealm.configuration.getRxFactory().changesetsFrom((Realm) this.realm, this);
        }
        if (baseRealm instanceof DynamicRealm) {
            return baseRealm.configuration.getRxFactory().changesetsFrom((DynamicRealm) baseRealm, this);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.realm.getClass());
        sb.append(" does not support RxJava2.");
        throw new UnsupportedOperationException(sb.toString());
    }

    private void checkForAddRemoveListener(@Nullable Object obj, boolean z) {
        if (!z || obj != null) {
            this.realm.checkIfValid();
            this.realm.sharedRealm.capabilities.checkCanDeliverNotification("Listeners cannot be used on current thread.");
            return;
        }
        throw new IllegalArgumentException("Listener should not be null");
    }

    public void addChangeListener(OrderedRealmCollectionChangeListener<RealmList<E>> orderedRealmCollectionChangeListener) {
        checkForAddRemoveListener(orderedRealmCollectionChangeListener, true);
        this.osListOperator.getOsList().addListener(this, orderedRealmCollectionChangeListener);
    }

    public void removeChangeListener(OrderedRealmCollectionChangeListener<RealmList<E>> orderedRealmCollectionChangeListener) {
        checkForAddRemoveListener(orderedRealmCollectionChangeListener, true);
        this.osListOperator.getOsList().removeListener(this, orderedRealmCollectionChangeListener);
    }

    public void addChangeListener(RealmChangeListener<RealmList<E>> realmChangeListener) {
        checkForAddRemoveListener(realmChangeListener, true);
        this.osListOperator.getOsList().addListener(this, realmChangeListener);
    }

    public void removeChangeListener(RealmChangeListener<RealmList<E>> realmChangeListener) {
        checkForAddRemoveListener(realmChangeListener, true);
        this.osListOperator.getOsList().removeListener(this, realmChangeListener);
    }

    public void removeAllChangeListeners() {
        checkForAddRemoveListener(null, false);
        this.osListOperator.getOsList().removeAllListeners();
    }

    private static boolean isClassForRealmModel(Class<?> cls) {
        return RealmModel.class.isAssignableFrom(cls);
    }

    private ManagedListOperator<E> getOperator(BaseRealm baseRealm, OsList osList, @Nullable Class<E> cls, @Nullable String str) {
        if (cls == null || isClassForRealmModel(cls)) {
            return new RealmModelListOperator(baseRealm, osList, cls, str);
        }
        if (cls == String.class) {
            return new StringListOperator(baseRealm, osList, cls);
        }
        if (cls == Long.class || cls == Integer.class || cls == Short.class || cls == Byte.class) {
            return new LongListOperator(baseRealm, osList, cls);
        }
        if (cls == Boolean.class) {
            return new BooleanListOperator(baseRealm, osList, cls);
        }
        if (cls == byte[].class) {
            return new BinaryListOperator(baseRealm, osList, cls);
        }
        if (cls == Double.class) {
            return new DoubleListOperator(baseRealm, osList, cls);
        }
        if (cls == Float.class) {
            return new FloatListOperator(baseRealm, osList, cls);
        }
        if (cls == Date.class) {
            return new DateListOperator(baseRealm, osList, cls);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unexpected value class: ");
        sb.append(cls.getName());
        throw new IllegalArgumentException(sb.toString());
    }
}

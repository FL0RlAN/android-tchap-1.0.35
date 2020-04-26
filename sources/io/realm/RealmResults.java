package io.realm;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.realm.internal.CheckedRow;
import io.realm.internal.OsResults;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.Row;
import io.realm.internal.Table;
import io.realm.internal.UncheckedRow;
import io.realm.internal.Util;
import io.realm.internal.android.JsonUtils;
import io.realm.log.RealmLog;
import io.realm.rx.CollectionChange;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Locale;
import javax.annotation.Nullable;

public class RealmResults<E> extends OrderedRealmCollectionImpl<E> {
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

    public /* bridge */ /* synthetic */ OrderedRealmCollectionSnapshot createSnapshot() {
        return super.createSnapshot();
    }

    public /* bridge */ /* synthetic */ boolean deleteAllFromRealm() {
        return super.deleteAllFromRealm();
    }

    public /* bridge */ /* synthetic */ boolean deleteFirstFromRealm() {
        return super.deleteFirstFromRealm();
    }

    public /* bridge */ /* synthetic */ void deleteFromRealm(int i) {
        super.deleteFromRealm(i);
    }

    public /* bridge */ /* synthetic */ boolean deleteLastFromRealm() {
        return super.deleteLastFromRealm();
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

    public /* bridge */ /* synthetic */ int size() {
        return super.size();
    }

    public /* bridge */ /* synthetic */ RealmResults sort(String str) {
        return super.sort(str);
    }

    public /* bridge */ /* synthetic */ RealmResults sort(String str, Sort sort) {
        return super.sort(str, sort);
    }

    public /* bridge */ /* synthetic */ RealmResults sort(String[] strArr, Sort[] sortArr) {
        return super.sort(strArr, sortArr);
    }

    public /* bridge */ /* synthetic */ Number sum(String str) {
        return super.sum(str);
    }

    static <T extends RealmModel> RealmResults<T> createBacklinkResults(BaseRealm baseRealm, Row row, Class<T> cls, String str) {
        return new RealmResults<>(baseRealm, OsResults.createForBacklinks(baseRealm.sharedRealm, (UncheckedRow) row, baseRealm.getSchema().getTable(cls), str), cls);
    }

    static RealmResults<DynamicRealmObject> createDynamicBacklinkResults(DynamicRealm dynamicRealm, CheckedRow checkedRow, Table table, String str) {
        return new RealmResults<>((BaseRealm) dynamicRealm, OsResults.createForBacklinks(dynamicRealm.sharedRealm, checkedRow, table, str), Table.getClassNameForTable(table.getName()));
    }

    RealmResults(BaseRealm baseRealm, OsResults osResults, Class<E> cls) {
        super(baseRealm, osResults, cls);
    }

    RealmResults(BaseRealm baseRealm, OsResults osResults, String str) {
        super(baseRealm, osResults, str);
    }

    public RealmQuery<E> where() {
        this.realm.checkIfValid();
        return RealmQuery.createQueryFromResult(this);
    }

    public RealmResults<E> sort(String str, Sort sort, String str2, Sort sort2) {
        return sort(new String[]{str, str2}, new Sort[]{sort, sort2});
    }

    public boolean isLoaded() {
        this.realm.checkIfValid();
        return this.osResults.isLoaded();
    }

    public boolean load() {
        this.realm.checkIfValid();
        this.osResults.load();
        return true;
    }

    public void setValue(String str, @Nullable Object obj) {
        checkNonEmptyFieldName(str);
        this.realm.checkIfValidAndInTransaction();
        String mapFieldNameToInternalName = mapFieldNameToInternalName(str);
        boolean z = obj instanceof String;
        String str2 = z ? (String) obj : null;
        String className = this.osResults.getTable().getClassName();
        RealmObjectSchema realmObjectSchema = getRealm().getSchema().get(className);
        if (!realmObjectSchema.hasField(mapFieldNameToInternalName)) {
            throw new IllegalArgumentException(String.format("Field '%s' could not be found in class '%s'", new Object[]{mapFieldNameToInternalName, className}));
        } else if (obj == null) {
            this.osResults.setNull(mapFieldNameToInternalName);
        } else {
            RealmFieldType fieldType = realmObjectSchema.getFieldType(mapFieldNameToInternalName);
            if (z && fieldType != RealmFieldType.STRING) {
                int i = AnonymousClass1.$SwitchMap$io$realm$RealmFieldType[fieldType.ordinal()];
                if (i == 1) {
                    obj = Boolean.valueOf(Boolean.parseBoolean(str2));
                } else if (i == 2) {
                    obj = Long.valueOf(Long.parseLong(str2));
                } else if (i == 3) {
                    obj = Float.valueOf(Float.parseFloat(str2));
                } else if (i == 4) {
                    obj = Double.valueOf(Double.parseDouble(str2));
                } else if (i == 5) {
                    obj = JsonUtils.stringToDate(str2);
                } else {
                    throw new IllegalArgumentException(String.format(Locale.US, "Field %s is not a String field, and the provide value could not be automatically converted: %s. Use a typedsetter instead", new Object[]{mapFieldNameToInternalName, obj}));
                }
            }
            Class<RealmList> cls = obj.getClass();
            if (cls == Boolean.class) {
                setBoolean(mapFieldNameToInternalName, ((Boolean) obj).booleanValue());
            } else if (cls == Short.class) {
                setShort(mapFieldNameToInternalName, ((Short) obj).shortValue());
            } else if (cls == Integer.class) {
                setInt(mapFieldNameToInternalName, ((Integer) obj).intValue());
            } else if (cls == Long.class) {
                setLong(mapFieldNameToInternalName, ((Long) obj).longValue());
            } else if (cls == Byte.class) {
                setByte(mapFieldNameToInternalName, ((Byte) obj).byteValue());
            } else if (cls == Float.class) {
                setFloat(mapFieldNameToInternalName, ((Float) obj).floatValue());
            } else if (cls == Double.class) {
                setDouble(mapFieldNameToInternalName, ((Double) obj).doubleValue());
            } else if (cls == String.class) {
                setString(mapFieldNameToInternalName, (String) obj);
            } else if (obj instanceof Date) {
                setDate(mapFieldNameToInternalName, (Date) obj);
            } else if (obj instanceof byte[]) {
                setBlob(mapFieldNameToInternalName, (byte[]) obj);
            } else if (obj instanceof RealmModel) {
                setObject(mapFieldNameToInternalName, (RealmModel) obj);
            } else if (cls == RealmList.class) {
                setList(mapFieldNameToInternalName, (RealmList) obj);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Value is of a type not supported: ");
                sb.append(obj.getClass());
                throw new IllegalArgumentException(sb.toString());
            }
        }
    }

    public void setNull(String str) {
        checkNonEmptyFieldName(str);
        this.realm.checkIfValidAndInTransaction();
        this.osResults.setNull(str);
    }

    public void setBoolean(String str, boolean z) {
        checkNonEmptyFieldName(str);
        this.realm.checkIfValidAndInTransaction();
        String mapFieldNameToInternalName = mapFieldNameToInternalName(str);
        checkType(mapFieldNameToInternalName, RealmFieldType.BOOLEAN);
        this.osResults.setBoolean(mapFieldNameToInternalName, z);
    }

    public void setByte(String str, byte b) {
        checkNonEmptyFieldName(str);
        this.realm.checkIfValidAndInTransaction();
        String mapFieldNameToInternalName = mapFieldNameToInternalName(str);
        checkType(mapFieldNameToInternalName, RealmFieldType.INTEGER);
        this.osResults.setInt(mapFieldNameToInternalName, (long) b);
    }

    public void setShort(String str, short s) {
        checkNonEmptyFieldName(str);
        this.realm.checkIfValidAndInTransaction();
        String mapFieldNameToInternalName = mapFieldNameToInternalName(str);
        checkType(mapFieldNameToInternalName, RealmFieldType.INTEGER);
        this.osResults.setInt(mapFieldNameToInternalName, (long) s);
    }

    public void setInt(String str, int i) {
        checkNonEmptyFieldName(str);
        String mapFieldNameToInternalName = mapFieldNameToInternalName(str);
        checkType(mapFieldNameToInternalName, RealmFieldType.INTEGER);
        this.realm.checkIfValidAndInTransaction();
        this.osResults.setInt(mapFieldNameToInternalName, (long) i);
    }

    public void setLong(String str, long j) {
        checkNonEmptyFieldName(str);
        this.realm.checkIfValidAndInTransaction();
        String mapFieldNameToInternalName = mapFieldNameToInternalName(str);
        checkType(mapFieldNameToInternalName, RealmFieldType.INTEGER);
        this.osResults.setInt(mapFieldNameToInternalName, j);
    }

    public void setFloat(String str, float f) {
        checkNonEmptyFieldName(str);
        this.realm.checkIfValidAndInTransaction();
        String mapFieldNameToInternalName = mapFieldNameToInternalName(str);
        checkType(mapFieldNameToInternalName, RealmFieldType.FLOAT);
        this.osResults.setFloat(mapFieldNameToInternalName, f);
    }

    public void setDouble(String str, double d) {
        checkNonEmptyFieldName(str);
        this.realm.checkIfValidAndInTransaction();
        String mapFieldNameToInternalName = mapFieldNameToInternalName(str);
        checkType(mapFieldNameToInternalName, RealmFieldType.DOUBLE);
        this.osResults.setDouble(mapFieldNameToInternalName, d);
    }

    public void setString(String str, @Nullable String str2) {
        checkNonEmptyFieldName(str);
        this.realm.checkIfValidAndInTransaction();
        String mapFieldNameToInternalName = mapFieldNameToInternalName(str);
        checkType(mapFieldNameToInternalName, RealmFieldType.STRING);
        this.osResults.setString(mapFieldNameToInternalName, str2);
    }

    public void setBlob(String str, @Nullable byte[] bArr) {
        checkNonEmptyFieldName(str);
        this.realm.checkIfValidAndInTransaction();
        String mapFieldNameToInternalName = mapFieldNameToInternalName(str);
        checkType(mapFieldNameToInternalName, RealmFieldType.BINARY);
        this.osResults.setBlob(mapFieldNameToInternalName, bArr);
    }

    public void setDate(String str, @Nullable Date date) {
        checkNonEmptyFieldName(str);
        this.realm.checkIfValidAndInTransaction();
        String mapFieldNameToInternalName = mapFieldNameToInternalName(str);
        checkType(mapFieldNameToInternalName, RealmFieldType.DATE);
        this.osResults.setDate(mapFieldNameToInternalName, date);
    }

    public void setObject(String str, @Nullable RealmModel realmModel) {
        checkNonEmptyFieldName(str);
        this.realm.checkIfValidAndInTransaction();
        String mapFieldNameToInternalName = mapFieldNameToInternalName(str);
        checkType(mapFieldNameToInternalName, RealmFieldType.OBJECT);
        this.osResults.setObject(mapFieldNameToInternalName, checkRealmObjectConstraints(mapFieldNameToInternalName, realmModel));
    }

    private Row checkRealmObjectConstraints(String str, @Nullable RealmModel realmModel) {
        if (realmModel == null) {
            return null;
        }
        if (!RealmObject.isManaged(realmModel) || !RealmObject.isValid(realmModel)) {
            throw new IllegalArgumentException("'value' is not a valid, managed Realm object.");
        }
        ProxyState realmGet$proxyState = ((RealmObjectProxy) realmModel).realmGet$proxyState();
        if (realmGet$proxyState.getRealm$realm().getPath().equals(this.realm.getPath())) {
            Table table = this.osResults.getTable();
            Table linkTarget = table.getLinkTarget(table.getColumnIndex(str));
            Table table2 = realmGet$proxyState.getRow$realm().getTable();
            if (linkTarget.hasSameSchema(table2)) {
                return realmGet$proxyState.getRow$realm();
            }
            throw new IllegalArgumentException(String.format(Locale.US, "Type of object is wrong. Was '%s', expected '%s'", new Object[]{table2.getClassName(), linkTarget.getClassName()}));
        }
        throw new IllegalArgumentException("'value' does not belong to the same Realm as the RealmResults.");
    }

    public <T> void setList(String str, RealmList<T> realmList) {
        checkNonEmptyFieldName(str);
        String mapFieldNameToInternalName = mapFieldNameToInternalName(str);
        this.realm.checkIfValidAndInTransaction();
        if (realmList != null) {
            RealmFieldType fieldType = this.realm.getSchema().getSchemaForClass(this.osResults.getTable().getClassName()).getFieldType(mapFieldNameToInternalName);
            switch (fieldType) {
                case LIST:
                    checkTypeOfListElements(realmList, RealmModel.class);
                    checkRealmObjectConstraints(mapFieldNameToInternalName, (RealmModel) realmList.first(null));
                    this.osResults.setModelList(mapFieldNameToInternalName, realmList);
                    return;
                case INTEGER_LIST:
                    Class listType = getListType(realmList);
                    if (listType.equals(Integer.class)) {
                        this.osResults.setIntegerList(mapFieldNameToInternalName, realmList);
                        return;
                    } else if (listType.equals(Long.class)) {
                        this.osResults.setLongList(mapFieldNameToInternalName, realmList);
                        return;
                    } else if (listType.equals(Short.class)) {
                        this.osResults.setShortList(mapFieldNameToInternalName, realmList);
                        return;
                    } else if (listType.equals(Byte.class)) {
                        this.osResults.setByteList(mapFieldNameToInternalName, realmList);
                        return;
                    } else {
                        throw new IllegalArgumentException(String.format("List contained the wrong type of elements. Elements that can be mapped to Integers was expected, but the actual type is '%s'", new Object[]{listType}));
                    }
                case BOOLEAN_LIST:
                    checkTypeOfListElements(realmList, Boolean.class);
                    this.osResults.setBooleanList(mapFieldNameToInternalName, realmList);
                    return;
                case STRING_LIST:
                    checkTypeOfListElements(realmList, String.class);
                    this.osResults.setStringList(mapFieldNameToInternalName, realmList);
                    return;
                case BINARY_LIST:
                    checkTypeOfListElements(realmList, byte[].class);
                    this.osResults.setByteArrayList(mapFieldNameToInternalName, realmList);
                    return;
                case DATE_LIST:
                    checkTypeOfListElements(realmList, Date.class);
                    this.osResults.setDateList(mapFieldNameToInternalName, realmList);
                    return;
                case FLOAT_LIST:
                    checkTypeOfListElements(realmList, Float.class);
                    this.osResults.setFloatList(mapFieldNameToInternalName, realmList);
                    return;
                case DOUBLE_LIST:
                    checkTypeOfListElements(realmList, Double.class);
                    this.osResults.setDoubleList(mapFieldNameToInternalName, realmList);
                    return;
                default:
                    throw new IllegalArgumentException(String.format("Field '%s' is not a list but a %s", new Object[]{mapFieldNameToInternalName, fieldType}));
            }
        } else {
            throw new IllegalArgumentException("Non-null 'list' required");
        }
    }

    private Class<?> getListType(RealmList realmList) {
        if (!realmList.isEmpty()) {
            return realmList.first().getClass();
        }
        return Long.class;
    }

    private <T> void checkTypeOfListElements(RealmList<T> realmList, Class<?> cls) {
        if (!realmList.isEmpty()) {
            Class cls2 = realmList.first().getClass();
            if (!cls.isAssignableFrom(cls2)) {
                throw new IllegalArgumentException(String.format("List contained the wrong type of elements. Elements of type '%s' was expected, but the actual type is '%s'", new Object[]{cls, cls2}));
            }
        }
    }

    public void addChangeListener(RealmChangeListener<RealmResults<E>> realmChangeListener) {
        checkForAddListener(realmChangeListener);
        this.osResults.addListener(this, realmChangeListener);
    }

    public void addChangeListener(OrderedRealmCollectionChangeListener<RealmResults<E>> orderedRealmCollectionChangeListener) {
        checkForAddListener(orderedRealmCollectionChangeListener);
        this.osResults.addListener(this, orderedRealmCollectionChangeListener);
    }

    private void checkForAddListener(@Nullable Object obj) {
        if (obj != null) {
            this.realm.checkIfValid();
            this.realm.sharedRealm.capabilities.checkCanDeliverNotification("Listeners cannot be used on current thread.");
            return;
        }
        throw new IllegalArgumentException("Listener should not be null");
    }

    private void checkForRemoveListener(@Nullable Object obj, boolean z) {
        if (z && obj == null) {
            throw new IllegalArgumentException("Listener should not be null");
        } else if (this.realm.isClosed()) {
            RealmLog.warn("Calling removeChangeListener on a closed Realm %s, make sure to close all listeners before closing the Realm.", this.realm.configuration.getPath());
        }
    }

    public void removeAllChangeListeners() {
        checkForRemoveListener(null, false);
        this.osResults.removeAllListeners();
    }

    public void removeChangeListener(RealmChangeListener<RealmResults<E>> realmChangeListener) {
        checkForRemoveListener(realmChangeListener, true);
        this.osResults.removeListener(this, realmChangeListener);
    }

    public void removeChangeListener(OrderedRealmCollectionChangeListener<RealmResults<E>> orderedRealmCollectionChangeListener) {
        checkForRemoveListener(orderedRealmCollectionChangeListener, true);
        this.osResults.removeListener(this, orderedRealmCollectionChangeListener);
    }

    public Flowable<RealmResults<E>> asFlowable() {
        if (this.realm instanceof Realm) {
            return this.realm.configuration.getRxFactory().from((Realm) this.realm, this);
        }
        if (this.realm instanceof DynamicRealm) {
            return this.realm.configuration.getRxFactory().from((DynamicRealm) this.realm, this);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.realm.getClass());
        sb.append(" does not support RxJava2.");
        throw new UnsupportedOperationException(sb.toString());
    }

    public Observable<CollectionChange<RealmResults<E>>> asChangesetObservable() {
        if (this.realm instanceof Realm) {
            return this.realm.configuration.getRxFactory().changesetsFrom((Realm) this.realm, this);
        }
        if (this.realm instanceof DynamicRealm) {
            return this.realm.configuration.getRxFactory().changesetsFrom((DynamicRealm) this.realm, this);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.realm.getClass());
        sb.append(" does not support RxJava2.");
        throw new UnsupportedOperationException(sb.toString());
    }

    public String asJSON() {
        return this.osResults.toJSON(-1);
    }

    private void checkNonEmptyFieldName(String str) {
        if (Util.isEmptyString(str)) {
            throw new IllegalArgumentException("Non-empty 'fieldname' required.");
        }
    }

    private void checkNotNull(@Nullable Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Non-null 'value' required. Use 'setNull(fieldName)' instead.");
        }
    }

    private void checkType(String str, RealmFieldType realmFieldType) {
        String className = this.osResults.getTable().getClassName();
        RealmFieldType fieldType = this.realm.getSchema().get(className).getFieldType(str);
        if (fieldType != realmFieldType) {
            throw new IllegalArgumentException(String.format("The field '%s.%s' is not of the expected type. Actual: %s, Expected: %s", new Object[]{className, str, fieldType, realmFieldType}));
        }
    }

    private String mapFieldNameToInternalName(String str) {
        if (!(this.realm instanceof Realm)) {
            return str;
        }
        String internalFieldName = this.realm.getSchema().getColumnInfo(this.osResults.getTable().getClassName()).getInternalFieldName(str);
        if (internalFieldName != null) {
            return internalFieldName;
        }
        throw new IllegalArgumentException(String.format("Field '%s' does not exists.", new Object[]{str}));
    }
}

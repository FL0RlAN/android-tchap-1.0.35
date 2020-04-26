package io.realm;

import io.realm.internal.OsList;
import io.realm.internal.OsResults;
import io.realm.internal.PendingRow;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.RealmProxyMediator;
import io.realm.internal.Row;
import io.realm.internal.SubscriptionAwareOsResults;
import io.realm.internal.Table;
import io.realm.internal.TableQuery;
import io.realm.internal.core.DescriptorOrdering;
import io.realm.internal.core.QueryDescriptor;
import io.realm.internal.fields.FieldDescriptor;
import io.realm.internal.fields.FieldDescriptor.SchemaProxy;
import io.realm.internal.sync.SubscriptionAction;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import javax.annotation.Nullable;

public class RealmQuery<E> {
    private static final String ASYNC_QUERY_WRONG_THREAD_MESSAGE = "Async query cannot be created on current thread.";
    private static final String EMPTY_VALUES = "Non-empty 'values' must be provided.";
    private static final String TYPE_MISMATCH = "Field '%s': type mismatch - %s expected.";
    private String className;
    private Class<E> clazz;
    private final boolean forValues;
    private final OsList osList;
    private final TableQuery query;
    private DescriptorOrdering queryDescriptors = new DescriptorOrdering();
    private final BaseRealm realm;
    private final RealmObjectSchema schema;
    private final Table table;

    /* renamed from: io.realm.RealmQuery$1 reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$io$realm$RealmFieldType = new int[RealmFieldType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            $SwitchMap$io$realm$RealmFieldType[RealmFieldType.INTEGER.ordinal()] = 1;
            $SwitchMap$io$realm$RealmFieldType[RealmFieldType.FLOAT.ordinal()] = 2;
            try {
                $SwitchMap$io$realm$RealmFieldType[RealmFieldType.DOUBLE.ordinal()] = 3;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    private static native String nativeSerializeQuery(long j, long j2);

    private static native long nativeSubscribe(long j, String str, long j2, long j3, long j4, boolean z);

    static <E extends RealmModel> RealmQuery<E> createQuery(Realm realm2, Class<E> cls) {
        return new RealmQuery<>(realm2, cls);
    }

    static <E extends RealmModel> RealmQuery<E> createDynamicQuery(DynamicRealm dynamicRealm, String str) {
        return new RealmQuery<>((BaseRealm) dynamicRealm, str);
    }

    static <E> RealmQuery<E> createQueryFromResult(RealmResults<E> realmResults) {
        if (realmResults.classSpec == null) {
            return new RealmQuery<>(realmResults, realmResults.className);
        }
        return new RealmQuery<>(realmResults, realmResults.classSpec);
    }

    static <E> RealmQuery<E> createQueryFromList(RealmList<E> realmList) {
        if (realmList.clazz == null) {
            return new RealmQuery<>(realmList.realm, realmList.getOsList(), realmList.className);
        }
        return new RealmQuery<>(realmList.realm, realmList.getOsList(), realmList.clazz);
    }

    private static boolean isClassForRealmModel(Class<?> cls) {
        return RealmModel.class.isAssignableFrom(cls);
    }

    private RealmQuery(Realm realm2, Class<E> cls) {
        this.realm = realm2;
        this.clazz = cls;
        this.forValues = !isClassForRealmModel(cls);
        if (this.forValues) {
            this.schema = null;
            this.table = null;
            this.osList = null;
            this.query = null;
            return;
        }
        this.schema = realm2.getSchema().getSchemaForClass(cls);
        this.table = this.schema.getTable();
        this.osList = null;
        this.query = this.table.where();
    }

    private RealmQuery(RealmResults<E> realmResults, Class<E> cls) {
        this.realm = realmResults.realm;
        this.clazz = cls;
        this.forValues = !isClassForRealmModel(cls);
        if (this.forValues) {
            this.schema = null;
            this.table = null;
            this.osList = null;
            this.query = null;
            return;
        }
        this.schema = this.realm.getSchema().getSchemaForClass(cls);
        this.table = realmResults.getTable();
        this.osList = null;
        this.query = realmResults.getOsResults().where();
    }

    private RealmQuery(BaseRealm baseRealm, OsList osList2, Class<E> cls) {
        this.realm = baseRealm;
        this.clazz = cls;
        this.forValues = !isClassForRealmModel(cls);
        if (this.forValues) {
            this.schema = null;
            this.table = null;
            this.osList = null;
            this.query = null;
            return;
        }
        this.schema = baseRealm.getSchema().getSchemaForClass(cls);
        this.table = this.schema.getTable();
        this.osList = osList2;
        this.query = osList2.getQuery();
    }

    private RealmQuery(BaseRealm baseRealm, String str) {
        this.realm = baseRealm;
        this.className = str;
        this.forValues = false;
        this.schema = baseRealm.getSchema().getSchemaForClass(str);
        this.table = this.schema.getTable();
        this.query = this.table.where();
        this.osList = null;
    }

    private RealmQuery(RealmResults<DynamicRealmObject> realmResults, String str) {
        this.realm = realmResults.realm;
        this.className = str;
        this.forValues = false;
        this.schema = this.realm.getSchema().getSchemaForClass(str);
        this.table = this.schema.getTable();
        this.query = realmResults.getOsResults().where();
        this.osList = null;
    }

    private RealmQuery(BaseRealm baseRealm, OsList osList2, String str) {
        this.realm = baseRealm;
        this.className = str;
        this.forValues = false;
        this.schema = baseRealm.getSchema().getSchemaForClass(str);
        this.table = this.schema.getTable();
        this.query = osList2.getQuery();
        this.osList = osList2;
    }

    public boolean isValid() {
        BaseRealm baseRealm = this.realm;
        boolean z = false;
        if (baseRealm != null && !baseRealm.isClosed()) {
            OsList osList2 = this.osList;
            if (osList2 != null) {
                return osList2.isValid();
            }
            Table table2 = this.table;
            if (table2 != null && table2.isValid()) {
                z = true;
            }
        }
        return z;
    }

    public RealmQuery<E> isNull(String str) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, new RealmFieldType[0]);
        this.query.isNull(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers());
        return this;
    }

    public RealmQuery<E> isNotNull(String str) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, new RealmFieldType[0]);
        this.query.isNotNull(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers());
        return this;
    }

    public RealmQuery<E> equalTo(String str, @Nullable String str2) {
        return equalTo(str, str2, Case.SENSITIVE);
    }

    public RealmQuery<E> equalTo(String str, @Nullable String str2, Case caseR) {
        this.realm.checkIfValid();
        return equalToWithoutThreadValidation(str, str2, caseR);
    }

    private RealmQuery<E> equalToWithoutThreadValidation(String str, @Nullable String str2, Case caseR) {
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.STRING);
        this.query.equalTo(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), str2, caseR);
        return this;
    }

    public RealmQuery<E> equalTo(String str, @Nullable Byte b) {
        this.realm.checkIfValid();
        return equalToWithoutThreadValidation(str, b);
    }

    private RealmQuery<E> equalToWithoutThreadValidation(String str, @Nullable Byte b) {
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.INTEGER);
        if (b == null) {
            this.query.isNull(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers());
        } else {
            this.query.equalTo(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), (long) b.byteValue());
        }
        return this;
    }

    public RealmQuery<E> equalTo(String str, @Nullable byte[] bArr) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.BINARY);
        if (bArr == null) {
            this.query.isNull(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers());
        } else {
            this.query.equalTo(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), bArr);
        }
        return this;
    }

    public RealmQuery<E> equalTo(String str, @Nullable Short sh) {
        this.realm.checkIfValid();
        return equalToWithoutThreadValidation(str, sh);
    }

    private RealmQuery<E> equalToWithoutThreadValidation(String str, @Nullable Short sh) {
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.INTEGER);
        if (sh == null) {
            this.query.isNull(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers());
        } else {
            this.query.equalTo(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), (long) sh.shortValue());
        }
        return this;
    }

    public RealmQuery<E> equalTo(String str, @Nullable Integer num) {
        this.realm.checkIfValid();
        return equalToWithoutThreadValidation(str, num);
    }

    private RealmQuery<E> equalToWithoutThreadValidation(String str, @Nullable Integer num) {
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.INTEGER);
        if (num == null) {
            this.query.isNull(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers());
        } else {
            this.query.equalTo(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), (long) num.intValue());
        }
        return this;
    }

    public RealmQuery<E> equalTo(String str, @Nullable Long l) {
        this.realm.checkIfValid();
        return equalToWithoutThreadValidation(str, l);
    }

    private RealmQuery<E> equalToWithoutThreadValidation(String str, @Nullable Long l) {
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.INTEGER);
        if (l == null) {
            this.query.isNull(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers());
        } else {
            this.query.equalTo(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), l.longValue());
        }
        return this;
    }

    public RealmQuery<E> equalTo(String str, @Nullable Double d) {
        this.realm.checkIfValid();
        return equalToWithoutThreadValidation(str, d);
    }

    private RealmQuery<E> equalToWithoutThreadValidation(String str, @Nullable Double d) {
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.DOUBLE);
        if (d == null) {
            this.query.isNull(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers());
        } else {
            this.query.equalTo(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), d.doubleValue());
        }
        return this;
    }

    public RealmQuery<E> equalTo(String str, @Nullable Float f) {
        this.realm.checkIfValid();
        return equalToWithoutThreadValidation(str, f);
    }

    private RealmQuery<E> equalToWithoutThreadValidation(String str, @Nullable Float f) {
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.FLOAT);
        if (f == null) {
            this.query.isNull(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers());
        } else {
            this.query.equalTo(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), f.floatValue());
        }
        return this;
    }

    public RealmQuery<E> equalTo(String str, @Nullable Boolean bool) {
        this.realm.checkIfValid();
        return equalToWithoutThreadValidation(str, bool);
    }

    private RealmQuery<E> equalToWithoutThreadValidation(String str, @Nullable Boolean bool) {
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.BOOLEAN);
        if (bool == null) {
            this.query.isNull(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers());
        } else {
            this.query.equalTo(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), bool.booleanValue());
        }
        return this;
    }

    public RealmQuery<E> equalTo(String str, @Nullable Date date) {
        this.realm.checkIfValid();
        return equalToWithoutThreadValidation(str, date);
    }

    private RealmQuery<E> equalToWithoutThreadValidation(String str, @Nullable Date date) {
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.DATE);
        this.query.equalTo(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), date);
        return this;
    }

    public RealmQuery<E> in(String str, @Nullable String[] strArr) {
        return in(str, strArr, Case.SENSITIVE);
    }

    public RealmQuery<E> in(String str, @Nullable String[] strArr, Case caseR) {
        this.realm.checkIfValid();
        if (strArr == null || strArr.length == 0) {
            alwaysFalse();
            return this;
        }
        beginGroupWithoutThreadValidation().equalToWithoutThreadValidation(str, strArr[0], caseR);
        for (int i = 1; i < strArr.length; i++) {
            orWithoutThreadValidation().equalToWithoutThreadValidation(str, strArr[i], caseR);
        }
        return endGroupWithoutThreadValidation();
    }

    public RealmQuery<E> in(String str, @Nullable Byte[] bArr) {
        this.realm.checkIfValid();
        if (bArr == null || bArr.length == 0) {
            alwaysFalse();
            return this;
        }
        beginGroupWithoutThreadValidation().equalToWithoutThreadValidation(str, bArr[0]);
        for (int i = 1; i < bArr.length; i++) {
            orWithoutThreadValidation().equalToWithoutThreadValidation(str, bArr[i]);
        }
        return endGroupWithoutThreadValidation();
    }

    public RealmQuery<E> in(String str, @Nullable Short[] shArr) {
        this.realm.checkIfValid();
        if (shArr == null || shArr.length == 0) {
            alwaysFalse();
            return this;
        }
        beginGroupWithoutThreadValidation().equalToWithoutThreadValidation(str, shArr[0]);
        for (int i = 1; i < shArr.length; i++) {
            orWithoutThreadValidation().equalToWithoutThreadValidation(str, shArr[i]);
        }
        return endGroupWithoutThreadValidation();
    }

    public RealmQuery<E> in(String str, @Nullable Integer[] numArr) {
        this.realm.checkIfValid();
        if (numArr == null || numArr.length == 0) {
            alwaysFalse();
            return this;
        }
        beginGroupWithoutThreadValidation().equalToWithoutThreadValidation(str, numArr[0]);
        for (int i = 1; i < numArr.length; i++) {
            orWithoutThreadValidation().equalToWithoutThreadValidation(str, numArr[i]);
        }
        return endGroupWithoutThreadValidation();
    }

    public RealmQuery<E> in(String str, @Nullable Long[] lArr) {
        this.realm.checkIfValid();
        if (lArr == null || lArr.length == 0) {
            alwaysFalse();
            return this;
        }
        beginGroupWithoutThreadValidation().equalToWithoutThreadValidation(str, lArr[0]);
        for (int i = 1; i < lArr.length; i++) {
            orWithoutThreadValidation().equalToWithoutThreadValidation(str, lArr[i]);
        }
        return endGroupWithoutThreadValidation();
    }

    public RealmQuery<E> in(String str, @Nullable Double[] dArr) {
        this.realm.checkIfValid();
        if (dArr == null || dArr.length == 0) {
            alwaysFalse();
            return this;
        }
        beginGroupWithoutThreadValidation().equalToWithoutThreadValidation(str, dArr[0]);
        for (int i = 1; i < dArr.length; i++) {
            orWithoutThreadValidation().equalToWithoutThreadValidation(str, dArr[i]);
        }
        return endGroupWithoutThreadValidation();
    }

    public RealmQuery<E> in(String str, @Nullable Float[] fArr) {
        this.realm.checkIfValid();
        if (fArr == null || fArr.length == 0) {
            alwaysFalse();
            return this;
        }
        beginGroupWithoutThreadValidation().equalToWithoutThreadValidation(str, fArr[0]);
        for (int i = 1; i < fArr.length; i++) {
            orWithoutThreadValidation().equalToWithoutThreadValidation(str, fArr[i]);
        }
        return endGroupWithoutThreadValidation();
    }

    public RealmQuery<E> in(String str, @Nullable Boolean[] boolArr) {
        this.realm.checkIfValid();
        if (boolArr == null || boolArr.length == 0) {
            alwaysFalse();
            return this;
        }
        beginGroupWithoutThreadValidation().equalToWithoutThreadValidation(str, boolArr[0]);
        for (int i = 1; i < boolArr.length; i++) {
            orWithoutThreadValidation().equalToWithoutThreadValidation(str, boolArr[i]);
        }
        return endGroupWithoutThreadValidation();
    }

    public RealmQuery<E> in(String str, @Nullable Date[] dateArr) {
        this.realm.checkIfValid();
        if (dateArr == null || dateArr.length == 0) {
            alwaysFalse();
            return this;
        }
        beginGroupWithoutThreadValidation().equalToWithoutThreadValidation(str, dateArr[0]);
        for (int i = 1; i < dateArr.length; i++) {
            orWithoutThreadValidation().equalToWithoutThreadValidation(str, dateArr[i]);
        }
        return endGroupWithoutThreadValidation();
    }

    public RealmQuery<E> notEqualTo(String str, @Nullable String str2) {
        return notEqualTo(str, str2, Case.SENSITIVE);
    }

    public RealmQuery<E> notEqualTo(String str, @Nullable String str2, Case caseR) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.STRING);
        if (columnIndices.length() <= 1 || caseR.getValue()) {
            this.query.notEqualTo(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), str2, caseR);
            return this;
        }
        throw new IllegalArgumentException("Link queries cannot be case insensitive - coming soon.");
    }

    public RealmQuery<E> notEqualTo(String str, @Nullable Byte b) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.INTEGER);
        if (b == null) {
            this.query.isNotNull(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers());
        } else {
            this.query.notEqualTo(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), (long) b.byteValue());
        }
        return this;
    }

    public RealmQuery<E> notEqualTo(String str, @Nullable byte[] bArr) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.BINARY);
        if (bArr == null) {
            this.query.isNotNull(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers());
        } else {
            this.query.notEqualTo(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), bArr);
        }
        return this;
    }

    public RealmQuery<E> notEqualTo(String str, @Nullable Short sh) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.INTEGER);
        if (sh == null) {
            this.query.isNotNull(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers());
        } else {
            this.query.notEqualTo(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), (long) sh.shortValue());
        }
        return this;
    }

    public RealmQuery<E> notEqualTo(String str, @Nullable Integer num) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.INTEGER);
        if (num == null) {
            this.query.isNotNull(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers());
        } else {
            this.query.notEqualTo(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), (long) num.intValue());
        }
        return this;
    }

    public RealmQuery<E> notEqualTo(String str, @Nullable Long l) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.INTEGER);
        if (l == null) {
            this.query.isNotNull(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers());
        } else {
            this.query.notEqualTo(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), l.longValue());
        }
        return this;
    }

    public RealmQuery<E> notEqualTo(String str, @Nullable Double d) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.DOUBLE);
        if (d == null) {
            this.query.isNotNull(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers());
        } else {
            this.query.notEqualTo(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), d.doubleValue());
        }
        return this;
    }

    public RealmQuery<E> notEqualTo(String str, @Nullable Float f) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.FLOAT);
        if (f == null) {
            this.query.isNotNull(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers());
        } else {
            this.query.notEqualTo(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), f.floatValue());
        }
        return this;
    }

    public RealmQuery<E> notEqualTo(String str, @Nullable Boolean bool) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.BOOLEAN);
        if (bool == null) {
            this.query.isNotNull(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers());
        } else {
            this.query.equalTo(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), !bool.booleanValue());
        }
        return this;
    }

    public RealmQuery<E> notEqualTo(String str, @Nullable Date date) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.DATE);
        if (date == null) {
            this.query.isNotNull(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers());
        } else {
            this.query.notEqualTo(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), date);
        }
        return this;
    }

    public RealmQuery<E> greaterThan(String str, int i) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.INTEGER);
        this.query.greaterThan(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), (long) i);
        return this;
    }

    public RealmQuery<E> greaterThan(String str, long j) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.INTEGER);
        this.query.greaterThan(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), j);
        return this;
    }

    public RealmQuery<E> greaterThan(String str, double d) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.DOUBLE);
        this.query.greaterThan(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), d);
        return this;
    }

    public RealmQuery<E> greaterThan(String str, float f) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.FLOAT);
        this.query.greaterThan(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), f);
        return this;
    }

    public RealmQuery<E> greaterThan(String str, Date date) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.DATE);
        this.query.greaterThan(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), date);
        return this;
    }

    public RealmQuery<E> greaterThanOrEqualTo(String str, int i) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.INTEGER);
        this.query.greaterThanOrEqual(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), (long) i);
        return this;
    }

    public RealmQuery<E> greaterThanOrEqualTo(String str, long j) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.INTEGER);
        this.query.greaterThanOrEqual(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), j);
        return this;
    }

    public RealmQuery<E> greaterThanOrEqualTo(String str, double d) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.DOUBLE);
        this.query.greaterThanOrEqual(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), d);
        return this;
    }

    public RealmQuery<E> greaterThanOrEqualTo(String str, float f) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.FLOAT);
        this.query.greaterThanOrEqual(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), f);
        return this;
    }

    public RealmQuery<E> greaterThanOrEqualTo(String str, Date date) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.DATE);
        this.query.greaterThanOrEqual(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), date);
        return this;
    }

    public RealmQuery<E> lessThan(String str, int i) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.INTEGER);
        this.query.lessThan(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), (long) i);
        return this;
    }

    public RealmQuery<E> lessThan(String str, long j) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.INTEGER);
        this.query.lessThan(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), j);
        return this;
    }

    public RealmQuery<E> lessThan(String str, double d) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.DOUBLE);
        this.query.lessThan(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), d);
        return this;
    }

    public RealmQuery<E> lessThan(String str, float f) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.FLOAT);
        this.query.lessThan(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), f);
        return this;
    }

    public RealmQuery<E> lessThan(String str, Date date) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.DATE);
        this.query.lessThan(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), date);
        return this;
    }

    public RealmQuery<E> lessThanOrEqualTo(String str, int i) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.INTEGER);
        this.query.lessThanOrEqual(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), (long) i);
        return this;
    }

    public RealmQuery<E> lessThanOrEqualTo(String str, long j) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.INTEGER);
        this.query.lessThanOrEqual(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), j);
        return this;
    }

    public RealmQuery<E> lessThanOrEqualTo(String str, double d) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.DOUBLE);
        this.query.lessThanOrEqual(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), d);
        return this;
    }

    public RealmQuery<E> lessThanOrEqualTo(String str, float f) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.FLOAT);
        this.query.lessThanOrEqual(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), f);
        return this;
    }

    public RealmQuery<E> lessThanOrEqualTo(String str, Date date) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.DATE);
        this.query.lessThanOrEqual(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), date);
        return this;
    }

    public RealmQuery<E> between(String str, int i, int i2) {
        this.realm.checkIfValid();
        this.query.between(this.schema.getColumnIndices(str, RealmFieldType.INTEGER).getColumnIndices(), (long) i, (long) i2);
        return this;
    }

    public RealmQuery<E> between(String str, long j, long j2) {
        this.realm.checkIfValid();
        this.query.between(this.schema.getColumnIndices(str, RealmFieldType.INTEGER).getColumnIndices(), j, j2);
        return this;
    }

    public RealmQuery<E> between(String str, double d, double d2) {
        this.realm.checkIfValid();
        this.query.between(this.schema.getColumnIndices(str, RealmFieldType.DOUBLE).getColumnIndices(), d, d2);
        return this;
    }

    public RealmQuery<E> between(String str, float f, float f2) {
        this.realm.checkIfValid();
        this.query.between(this.schema.getColumnIndices(str, RealmFieldType.FLOAT).getColumnIndices(), f, f2);
        return this;
    }

    public RealmQuery<E> between(String str, Date date, Date date2) {
        this.realm.checkIfValid();
        this.query.between(this.schema.getColumnIndices(str, RealmFieldType.DATE).getColumnIndices(), date, date2);
        return this;
    }

    public RealmQuery<E> contains(String str, String str2) {
        return contains(str, str2, Case.SENSITIVE);
    }

    public RealmQuery<E> contains(String str, String str2, Case caseR) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.STRING);
        this.query.contains(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), str2, caseR);
        return this;
    }

    public RealmQuery<E> beginsWith(String str, String str2) {
        return beginsWith(str, str2, Case.SENSITIVE);
    }

    public RealmQuery<E> beginsWith(String str, String str2, Case caseR) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.STRING);
        this.query.beginsWith(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), str2, caseR);
        return this;
    }

    public RealmQuery<E> endsWith(String str, String str2) {
        return endsWith(str, str2, Case.SENSITIVE);
    }

    public RealmQuery<E> endsWith(String str, String str2, Case caseR) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.STRING);
        this.query.endsWith(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), str2, caseR);
        return this;
    }

    public RealmQuery<E> like(String str, String str2) {
        return like(str, str2, Case.SENSITIVE);
    }

    public RealmQuery<E> like(String str, String str2, Case caseR) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.STRING);
        this.query.like(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers(), str2, caseR);
        return this;
    }

    public RealmQuery<E> beginGroup() {
        this.realm.checkIfValid();
        return beginGroupWithoutThreadValidation();
    }

    private RealmQuery<E> beginGroupWithoutThreadValidation() {
        this.query.group();
        return this;
    }

    public RealmQuery<E> endGroup() {
        this.realm.checkIfValid();
        return endGroupWithoutThreadValidation();
    }

    private RealmQuery<E> endGroupWithoutThreadValidation() {
        this.query.endGroup();
        return this;
    }

    public RealmQuery<E> or() {
        this.realm.checkIfValid();
        return orWithoutThreadValidation();
    }

    private RealmQuery<E> orWithoutThreadValidation() {
        this.query.or();
        return this;
    }

    public RealmQuery<E> and() {
        this.realm.checkIfValid();
        return this;
    }

    public RealmQuery<E> not() {
        this.realm.checkIfValid();
        this.query.not();
        return this;
    }

    public RealmQuery<E> isEmpty(String str) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.STRING, RealmFieldType.BINARY, RealmFieldType.LIST, RealmFieldType.LINKING_OBJECTS);
        this.query.isEmpty(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers());
        return this;
    }

    public RealmQuery<E> isNotEmpty(String str) {
        this.realm.checkIfValid();
        FieldDescriptor columnIndices = this.schema.getColumnIndices(str, RealmFieldType.STRING, RealmFieldType.BINARY, RealmFieldType.LIST, RealmFieldType.LINKING_OBJECTS);
        this.query.isNotEmpty(columnIndices.getColumnIndices(), columnIndices.getNativeTablePointers());
        return this;
    }

    public Number sum(String str) {
        this.realm.checkIfValid();
        long andCheckFieldIndex = this.schema.getAndCheckFieldIndex(str);
        int i = AnonymousClass1.$SwitchMap$io$realm$RealmFieldType[this.table.getColumnType(andCheckFieldIndex).ordinal()];
        if (i == 1) {
            return Long.valueOf(this.query.sumInt(andCheckFieldIndex));
        }
        if (i == 2) {
            return Double.valueOf(this.query.sumFloat(andCheckFieldIndex));
        }
        if (i == 3) {
            return Double.valueOf(this.query.sumDouble(andCheckFieldIndex));
        }
        throw new IllegalArgumentException(String.format(Locale.US, TYPE_MISMATCH, new Object[]{str, "int, float or double"}));
    }

    public double average(String str) {
        this.realm.checkIfValid();
        long andCheckFieldIndex = this.schema.getAndCheckFieldIndex(str);
        int i = AnonymousClass1.$SwitchMap$io$realm$RealmFieldType[this.table.getColumnType(andCheckFieldIndex).ordinal()];
        if (i == 1) {
            return this.query.averageInt(andCheckFieldIndex);
        }
        if (i == 2) {
            return this.query.averageFloat(andCheckFieldIndex);
        }
        if (i == 3) {
            return this.query.averageDouble(andCheckFieldIndex);
        }
        throw new IllegalArgumentException(String.format(Locale.US, TYPE_MISMATCH, new Object[]{str, "int, float or double"}));
    }

    @Nullable
    public Number min(String str) {
        this.realm.checkIfValid();
        long andCheckFieldIndex = this.schema.getAndCheckFieldIndex(str);
        int i = AnonymousClass1.$SwitchMap$io$realm$RealmFieldType[this.table.getColumnType(andCheckFieldIndex).ordinal()];
        if (i == 1) {
            return this.query.minimumInt(andCheckFieldIndex);
        }
        if (i == 2) {
            return this.query.minimumFloat(andCheckFieldIndex);
        }
        if (i == 3) {
            return this.query.minimumDouble(andCheckFieldIndex);
        }
        throw new IllegalArgumentException(String.format(Locale.US, TYPE_MISMATCH, new Object[]{str, "int, float or double"}));
    }

    @Nullable
    public Date minimumDate(String str) {
        this.realm.checkIfValid();
        return this.query.minimumDate(this.schema.getAndCheckFieldIndex(str));
    }

    @Nullable
    public Number max(String str) {
        this.realm.checkIfValid();
        long andCheckFieldIndex = this.schema.getAndCheckFieldIndex(str);
        int i = AnonymousClass1.$SwitchMap$io$realm$RealmFieldType[this.table.getColumnType(andCheckFieldIndex).ordinal()];
        if (i == 1) {
            return this.query.maximumInt(andCheckFieldIndex);
        }
        if (i == 2) {
            return this.query.maximumFloat(andCheckFieldIndex);
        }
        if (i == 3) {
            return this.query.maximumDouble(andCheckFieldIndex);
        }
        throw new IllegalArgumentException(String.format(Locale.US, TYPE_MISMATCH, new Object[]{str, "int, float or double"}));
    }

    @Nullable
    public Date maximumDate(String str) {
        this.realm.checkIfValid();
        return this.query.maximumDate(this.schema.getAndCheckFieldIndex(str));
    }

    public long count() {
        this.realm.checkIfValid();
        return lazyFindAll().size();
    }

    public RealmResults<E> findAll() {
        this.realm.checkIfValid();
        return createRealmResults(this.query, this.queryDescriptors, true, SubscriptionAction.NO_SUBSCRIPTION);
    }

    private OsResults lazyFindAll() {
        this.realm.checkIfValid();
        return createRealmResults(this.query, this.queryDescriptors, false, SubscriptionAction.NO_SUBSCRIPTION).osResults;
    }

    public RealmResults<E> findAllAsync() {
        SubscriptionAction subscriptionAction;
        this.realm.checkIfValid();
        this.realm.sharedRealm.capabilities.checkCanDeliverNotification(ASYNC_QUERY_WRONG_THREAD_MESSAGE);
        if (!this.realm.sharedRealm.isPartial() || this.osList != null) {
            subscriptionAction = SubscriptionAction.NO_SUBSCRIPTION;
        } else {
            subscriptionAction = SubscriptionAction.ANONYMOUS_SUBSCRIPTION;
        }
        return createRealmResults(this.query, this.queryDescriptors, false, subscriptionAction);
    }

    public RealmQuery<E> sort(String str) {
        this.realm.checkIfValid();
        return sort(str, Sort.ASCENDING);
    }

    public RealmQuery<E> sort(String str, Sort sort) {
        this.realm.checkIfValid();
        return sort(new String[]{str}, new Sort[]{sort});
    }

    public RealmQuery<E> sort(String str, Sort sort, String str2, Sort sort2) {
        this.realm.checkIfValid();
        return sort(new String[]{str, str2}, new Sort[]{sort, sort2});
    }

    public RealmQuery<E> sort(String[] strArr, Sort[] sortArr) {
        this.realm.checkIfValid();
        this.queryDescriptors.appendSort(QueryDescriptor.getInstanceForSort((SchemaProxy) getSchemaConnector(), this.query.getTable(), strArr, sortArr));
        return this;
    }

    public RealmQuery<E> distinct(String str) {
        return distinct(str, new String[0]);
    }

    public RealmQuery<E> distinct(String str, String... strArr) {
        QueryDescriptor queryDescriptor;
        this.realm.checkIfValid();
        if (strArr.length == 0) {
            queryDescriptor = QueryDescriptor.getInstanceForDistinct((SchemaProxy) getSchemaConnector(), this.table, str);
        } else {
            String[] strArr2 = new String[(strArr.length + 1)];
            strArr2[0] = str;
            System.arraycopy(strArr, 0, strArr2, 1, strArr.length);
            queryDescriptor = QueryDescriptor.getInstanceForDistinct((SchemaProxy) getSchemaConnector(), this.table, strArr2);
        }
        this.queryDescriptors.appendDistinct(queryDescriptor);
        return this;
    }

    public RealmQuery<E> limit(long j) {
        this.realm.checkIfValid();
        if (j >= 1) {
            this.queryDescriptors.setLimit(j);
            return this;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Only positive numbers above 0 is allowed. Yours was: ");
        sb.append(j);
        throw new IllegalArgumentException(sb.toString());
    }

    public RealmQuery<E> alwaysTrue() {
        this.realm.checkIfValid();
        this.query.alwaysTrue();
        return this;
    }

    public RealmQuery<E> alwaysFalse() {
        this.realm.checkIfValid();
        this.query.alwaysFalse();
        return this;
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

    public String getDescription() {
        return nativeSerializeQuery(this.query.getNativePtr(), this.queryDescriptors.getNativePtr());
    }

    public String getTypeQueried() {
        return this.table.getClassName();
    }

    private boolean isDynamicQuery() {
        return this.className != null;
    }

    @Nullable
    public E findFirst() {
        this.realm.checkIfValid();
        E e = null;
        if (this.forValues) {
            return null;
        }
        long sourceRowIndexForFirstObject = getSourceRowIndexForFirstObject();
        if (sourceRowIndexForFirstObject >= 0) {
            e = this.realm.get(this.clazz, this.className, sourceRowIndexForFirstObject);
        }
        return e;
    }

    public E findFirstAsync() {
        Row row;
        E e;
        this.realm.checkIfValid();
        if (!this.forValues) {
            this.realm.sharedRealm.capabilities.checkCanDeliverNotification(ASYNC_QUERY_WRONG_THREAD_MESSAGE);
            if (this.realm.isInTransaction()) {
                row = OsResults.createFromQuery(this.realm.sharedRealm, this.query).firstUncheckedRow();
            } else {
                row = new PendingRow(this.realm.sharedRealm, this.query, this.queryDescriptors, isDynamicQuery());
            }
            if (isDynamicQuery()) {
                e = new DynamicRealmObject(this.realm, row);
            } else {
                Class<E> cls = this.clazz;
                RealmProxyMediator schemaMediator = this.realm.getConfiguration().getSchemaMediator();
                BaseRealm baseRealm = this.realm;
                e = schemaMediator.newInstance(cls, baseRealm, row, baseRealm.getSchema().getColumnInfo(cls), false, Collections.emptyList());
            }
            if (row instanceof PendingRow) {
                ((PendingRow) row).setFrontEnd(((RealmObjectProxy) e).realmGet$proxyState());
            }
            return e;
        }
        throw new UnsupportedOperationException("findFirstAsync() available only when type parameter 'E' is implementing RealmModel.");
    }

    private RealmResults<E> createRealmResults(TableQuery tableQuery, DescriptorOrdering descriptorOrdering, boolean z, SubscriptionAction subscriptionAction) {
        OsResults osResults;
        RealmResults<E> realmResults;
        if (subscriptionAction.shouldCreateSubscriptions()) {
            osResults = SubscriptionAwareOsResults.createFromQuery(this.realm.sharedRealm, tableQuery, descriptorOrdering, subscriptionAction);
        } else {
            osResults = OsResults.createFromQuery(this.realm.sharedRealm, tableQuery, descriptorOrdering);
        }
        if (isDynamicQuery()) {
            realmResults = new RealmResults<>(this.realm, osResults, this.className);
        } else {
            realmResults = new RealmResults<>(this.realm, osResults, this.clazz);
        }
        if (z) {
            realmResults.load();
        }
        return realmResults;
    }

    private long getSourceRowIndexForFirstObject() {
        if (this.queryDescriptors.isEmpty()) {
            return this.query.find();
        }
        RealmObjectProxy realmObjectProxy = (RealmObjectProxy) findAll().first(null);
        if (realmObjectProxy != null) {
            return realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex();
        }
        return -1;
    }

    private SchemaConnector getSchemaConnector() {
        return new SchemaConnector(this.realm.getSchema());
    }
}

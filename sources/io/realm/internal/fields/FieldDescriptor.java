package io.realm.internal.fields;

import io.realm.RealmFieldType;
import io.realm.internal.ColumnInfo;
import io.realm.internal.Table;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

public abstract class FieldDescriptor {
    public static final Set<RealmFieldType> ALL_LINK_FIELD_TYPES;
    private static final Pattern FIELD_SEPARATOR = Pattern.compile("\\.");
    public static final Set<RealmFieldType> LIST_LINK_FIELD_TYPE;
    public static final Set<RealmFieldType> NO_LINK_FIELD_TYPE = Collections.emptySet();
    public static final Set<RealmFieldType> OBJECT_LINK_FIELD_TYPE;
    public static final Set<RealmFieldType> SIMPLE_LINK_FIELD_TYPES;
    private long[] columnIndices;
    private final List<String> fields;
    private String finalColumnName;
    private RealmFieldType finalColumnType;
    private long[] nativeTablePointers;
    private final Set<RealmFieldType> validFinalColumnTypes;
    private final Set<RealmFieldType> validInternalColumnTypes;

    public interface SchemaProxy {
        ColumnInfo getColumnInfo(String str);

        long getNativeTablePtr(String str);

        boolean hasCache();
    }

    /* access modifiers changed from: protected */
    public abstract void compileFieldDescription(List<String> list);

    static {
        HashSet hashSet = new HashSet(3);
        hashSet.add(RealmFieldType.OBJECT);
        hashSet.add(RealmFieldType.LIST);
        hashSet.add(RealmFieldType.LINKING_OBJECTS);
        ALL_LINK_FIELD_TYPES = Collections.unmodifiableSet(hashSet);
        HashSet hashSet2 = new HashSet(2);
        hashSet2.add(RealmFieldType.OBJECT);
        hashSet2.add(RealmFieldType.LIST);
        SIMPLE_LINK_FIELD_TYPES = Collections.unmodifiableSet(hashSet2);
        HashSet hashSet3 = new HashSet(1);
        hashSet3.add(RealmFieldType.LIST);
        LIST_LINK_FIELD_TYPE = Collections.unmodifiableSet(hashSet3);
        HashSet hashSet4 = new HashSet(1);
        hashSet4.add(RealmFieldType.OBJECT);
        OBJECT_LINK_FIELD_TYPE = Collections.unmodifiableSet(hashSet4);
    }

    public static FieldDescriptor createStandardFieldDescriptor(SchemaProxy schemaProxy, Table table, String str, RealmFieldType... realmFieldTypeArr) {
        return createFieldDescriptor(schemaProxy, table, str, null, new HashSet(Arrays.asList(realmFieldTypeArr)));
    }

    /* JADX WARNING: type inference failed for: r0v1, types: [io.realm.internal.fields.DynamicFieldDescriptor] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    public static FieldDescriptor createFieldDescriptor(SchemaProxy schemaProxy, Table table, String str, Set<RealmFieldType> set, Set<RealmFieldType> set2) {
        CachedFieldDescriptor cachedFieldDescriptor;
        if (schemaProxy == null || !schemaProxy.hasCache()) {
            if (set == null) {
                set = SIMPLE_LINK_FIELD_TYPES;
            }
            cachedFieldDescriptor = new DynamicFieldDescriptor(table, str, set, set2);
        } else {
            String className = table.getClassName();
            if (set == null) {
                set = ALL_LINK_FIELD_TYPES;
            }
            CachedFieldDescriptor cachedFieldDescriptor2 = new CachedFieldDescriptor(schemaProxy, className, str, set, set2);
            cachedFieldDescriptor = cachedFieldDescriptor2;
        }
        return cachedFieldDescriptor;
    }

    protected FieldDescriptor(String str, Set<RealmFieldType> set, Set<RealmFieldType> set2) {
        this.fields = parseFieldDescription(str);
        if (this.fields.size() > 0) {
            this.validInternalColumnTypes = set;
            this.validFinalColumnTypes = set2;
            return;
        }
        throw new IllegalArgumentException("Invalid query: Empty field descriptor");
    }

    public final int length() {
        return this.fields.size();
    }

    public final long[] getColumnIndices() {
        compileIfNecessary();
        long[] jArr = this.columnIndices;
        return Arrays.copyOf(jArr, jArr.length);
    }

    public final long[] getNativeTablePointers() {
        compileIfNecessary();
        long[] jArr = this.nativeTablePointers;
        return Arrays.copyOf(jArr, jArr.length);
    }

    public final String getFinalColumnName() {
        compileIfNecessary();
        return this.finalColumnName;
    }

    public final RealmFieldType getFinalColumnType() {
        compileIfNecessary();
        return this.finalColumnType;
    }

    /* access modifiers changed from: protected */
    public final void verifyInternalColumnType(String str, String str2, RealmFieldType realmFieldType) {
        verifyColumnType(str, str2, realmFieldType, this.validInternalColumnTypes);
    }

    /* access modifiers changed from: protected */
    public final void setCompilationResults(String str, String str2, RealmFieldType realmFieldType, long[] jArr, long[] jArr2) {
        Set<RealmFieldType> set = this.validFinalColumnTypes;
        if (set != null && set.size() > 0) {
            verifyColumnType(str, str2, realmFieldType, this.validFinalColumnTypes);
        }
        this.finalColumnName = str2;
        this.finalColumnType = realmFieldType;
        this.columnIndices = jArr;
        this.nativeTablePointers = jArr2;
    }

    private List<String> parseFieldDescription(String str) {
        if (str == null || str.equals("")) {
            throw new IllegalArgumentException("Invalid query: field name is empty");
        }
        int lastIndexOf = str.lastIndexOf(".");
        if (lastIndexOf == str.length() - 1) {
            throw new IllegalArgumentException("Invalid query: field name must not end with a period ('.')");
        } else if (lastIndexOf > -1) {
            return Arrays.asList(FIELD_SEPARATOR.split(str));
        } else {
            return Collections.singletonList(str);
        }
    }

    private void verifyColumnType(String str, String str2, RealmFieldType realmFieldType, Set<RealmFieldType> set) {
        if (!set.contains(realmFieldType)) {
            throw new IllegalArgumentException(String.format(Locale.US, "Invalid query: field '%s' in class '%s' is of invalid type '%s'.", new Object[]{str2, str, realmFieldType.toString()}));
        }
    }

    private void compileIfNecessary() {
        if (this.finalColumnType == null) {
            compileFieldDescription(this.fields);
        }
    }
}

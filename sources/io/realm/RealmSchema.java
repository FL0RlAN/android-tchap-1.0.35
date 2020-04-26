package io.realm;

import io.realm.internal.ColumnIndices;
import io.realm.internal.ColumnInfo;
import io.realm.internal.Table;
import io.realm.internal.Util;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

public abstract class RealmSchema {
    static final String EMPTY_STRING_MSG = "Null or empty class names are not allowed";
    private final Map<Class<? extends RealmModel>, RealmObjectSchema> classToSchema = new HashMap();
    private final Map<Class<? extends RealmModel>, Table> classToTable = new HashMap();
    private final ColumnIndices columnIndices;
    private final Map<String, RealmObjectSchema> dynamicClassToSchema = new HashMap();
    private final Map<String, Table> dynamicClassToTable = new HashMap();
    final BaseRealm realm;

    public abstract RealmObjectSchema create(String str);

    public abstract RealmObjectSchema createWithPrimaryKeyField(String str, String str2, Class<?> cls, FieldAttribute... fieldAttributeArr);

    @Nullable
    public abstract RealmObjectSchema get(String str);

    public abstract Set<RealmObjectSchema> getAll();

    public abstract void remove(String str);

    public abstract RealmObjectSchema rename(String str, String str2);

    RealmSchema(BaseRealm baseRealm, @Nullable ColumnIndices columnIndices2) {
        this.realm = baseRealm;
        this.columnIndices = columnIndices2;
    }

    public boolean contains(String str) {
        return this.realm.getSharedRealm().hasTable(Table.getTableNameForClass(str));
    }

    /* access modifiers changed from: 0000 */
    public void checkNotEmpty(String str, String str2) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException(str2);
        }
    }

    /* access modifiers changed from: 0000 */
    public void checkHasTable(String str, String str2) {
        if (!this.realm.getSharedRealm().hasTable(Table.getTableNameForClass(str))) {
            throw new IllegalArgumentException(str2);
        }
    }

    /* access modifiers changed from: 0000 */
    public Table getTable(String str) {
        String tableNameForClass = Table.getTableNameForClass(str);
        Table table = (Table) this.dynamicClassToTable.get(tableNameForClass);
        if (table != null) {
            return table;
        }
        Table table2 = this.realm.getSharedRealm().getTable(tableNameForClass);
        this.dynamicClassToTable.put(tableNameForClass, table2);
        return table2;
    }

    /* access modifiers changed from: 0000 */
    public Table getTable(Class<? extends RealmModel> cls) {
        Table table = (Table) this.classToTable.get(cls);
        if (table != null) {
            return table;
        }
        Class originalModelClass = Util.getOriginalModelClass(cls);
        if (isProxyClass(originalModelClass, cls)) {
            table = (Table) this.classToTable.get(originalModelClass);
        }
        if (table == null) {
            table = this.realm.getSharedRealm().getTable(Table.getTableNameForClass(this.realm.getConfiguration().getSchemaMediator().getSimpleClassName(originalModelClass)));
            this.classToTable.put(originalModelClass, table);
        }
        if (isProxyClass(originalModelClass, cls)) {
            this.classToTable.put(cls, table);
        }
        return table;
    }

    /* access modifiers changed from: 0000 */
    public RealmObjectSchema getSchemaForClass(Class<? extends RealmModel> cls) {
        RealmObjectSchema realmObjectSchema = (RealmObjectSchema) this.classToSchema.get(cls);
        if (realmObjectSchema != null) {
            return realmObjectSchema;
        }
        Class originalModelClass = Util.getOriginalModelClass(cls);
        if (isProxyClass(originalModelClass, cls)) {
            realmObjectSchema = (RealmObjectSchema) this.classToSchema.get(originalModelClass);
        }
        if (realmObjectSchema == null) {
            ImmutableRealmObjectSchema immutableRealmObjectSchema = new ImmutableRealmObjectSchema(this.realm, this, getTable(cls), getColumnInfo(originalModelClass));
            this.classToSchema.put(originalModelClass, immutableRealmObjectSchema);
            realmObjectSchema = immutableRealmObjectSchema;
        }
        if (isProxyClass(originalModelClass, cls)) {
            this.classToSchema.put(cls, realmObjectSchema);
        }
        return realmObjectSchema;
    }

    /* access modifiers changed from: 0000 */
    public RealmObjectSchema getSchemaForClass(String str) {
        String tableNameForClass = Table.getTableNameForClass(str);
        RealmObjectSchema realmObjectSchema = (RealmObjectSchema) this.dynamicClassToSchema.get(tableNameForClass);
        if (realmObjectSchema != null && realmObjectSchema.getTable().isValid() && realmObjectSchema.getClassName().equals(str)) {
            return realmObjectSchema;
        }
        if (this.realm.getSharedRealm().hasTable(tableNameForClass)) {
            BaseRealm baseRealm = this.realm;
            ImmutableRealmObjectSchema immutableRealmObjectSchema = new ImmutableRealmObjectSchema(baseRealm, this, baseRealm.getSharedRealm().getTable(tableNameForClass));
            this.dynamicClassToSchema.put(tableNameForClass, immutableRealmObjectSchema);
            return immutableRealmObjectSchema;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("The class ");
        sb.append(str);
        sb.append(" doesn't exist in this Realm.");
        throw new IllegalArgumentException(sb.toString());
    }

    private boolean isProxyClass(Class<? extends RealmModel> cls, Class<? extends RealmModel> cls2) {
        return cls.equals(cls2);
    }

    /* access modifiers changed from: 0000 */
    public final boolean haveColumnInfo() {
        return this.columnIndices != null;
    }

    /* access modifiers changed from: 0000 */
    public final ColumnInfo getColumnInfo(Class<? extends RealmModel> cls) {
        checkIndices();
        return this.columnIndices.getColumnInfo(cls);
    }

    /* access modifiers changed from: protected */
    public final ColumnInfo getColumnInfo(String str) {
        checkIndices();
        return this.columnIndices.getColumnInfo(str);
    }

    /* access modifiers changed from: 0000 */
    public final void putToClassNameToSchemaMap(String str, RealmObjectSchema realmObjectSchema) {
        this.dynamicClassToSchema.put(str, realmObjectSchema);
    }

    /* access modifiers changed from: 0000 */
    public final RealmObjectSchema removeFromClassNameToSchemaMap(String str) {
        return (RealmObjectSchema) this.dynamicClassToSchema.remove(str);
    }

    private void checkIndices() {
        if (!haveColumnInfo()) {
            throw new IllegalStateException("Attempt to use column index before set.");
        }
    }

    /* access modifiers changed from: 0000 */
    public void refresh() {
        ColumnIndices columnIndices2 = this.columnIndices;
        if (columnIndices2 != null) {
            columnIndices2.refresh();
        }
        this.dynamicClassToTable.clear();
        this.classToTable.clear();
        this.classToSchema.clear();
        this.dynamicClassToSchema.clear();
    }
}

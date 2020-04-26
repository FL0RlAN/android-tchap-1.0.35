package io.realm;

import io.realm.RealmObjectSchema.Function;
import io.realm.internal.ColumnInfo;
import io.realm.internal.Table;
import io.realm.internal.fields.FieldDescriptor;

class ImmutableRealmObjectSchema extends RealmObjectSchema {
    private static final String SCHEMA_IMMUTABLE_EXCEPTION_MSG = "This 'RealmObjectSchema' is immutable. Please use 'DynamicRealm.getSchema() to get a mutable instance.";

    ImmutableRealmObjectSchema(BaseRealm baseRealm, RealmSchema realmSchema, Table table, ColumnInfo columnInfo) {
        super(baseRealm, realmSchema, table, columnInfo);
    }

    ImmutableRealmObjectSchema(BaseRealm baseRealm, RealmSchema realmSchema, Table table) {
        super(baseRealm, realmSchema, table, new DynamicColumnIndices(table));
    }

    public RealmObjectSchema setClassName(String str) {
        throw new UnsupportedOperationException(SCHEMA_IMMUTABLE_EXCEPTION_MSG);
    }

    public RealmObjectSchema addField(String str, Class<?> cls, FieldAttribute... fieldAttributeArr) {
        throw new UnsupportedOperationException(SCHEMA_IMMUTABLE_EXCEPTION_MSG);
    }

    public RealmObjectSchema addRealmObjectField(String str, RealmObjectSchema realmObjectSchema) {
        throw new UnsupportedOperationException(SCHEMA_IMMUTABLE_EXCEPTION_MSG);
    }

    public RealmObjectSchema addRealmListField(String str, RealmObjectSchema realmObjectSchema) {
        throw new UnsupportedOperationException(SCHEMA_IMMUTABLE_EXCEPTION_MSG);
    }

    public RealmObjectSchema addRealmListField(String str, Class<?> cls) {
        throw new UnsupportedOperationException(SCHEMA_IMMUTABLE_EXCEPTION_MSG);
    }

    public RealmObjectSchema removeField(String str) {
        throw new UnsupportedOperationException(SCHEMA_IMMUTABLE_EXCEPTION_MSG);
    }

    public RealmObjectSchema renameField(String str, String str2) {
        throw new UnsupportedOperationException(SCHEMA_IMMUTABLE_EXCEPTION_MSG);
    }

    public RealmObjectSchema addIndex(String str) {
        throw new UnsupportedOperationException(SCHEMA_IMMUTABLE_EXCEPTION_MSG);
    }

    public RealmObjectSchema removeIndex(String str) {
        throw new UnsupportedOperationException(SCHEMA_IMMUTABLE_EXCEPTION_MSG);
    }

    public RealmObjectSchema addPrimaryKey(String str) {
        throw new UnsupportedOperationException(SCHEMA_IMMUTABLE_EXCEPTION_MSG);
    }

    public RealmObjectSchema removePrimaryKey() {
        throw new UnsupportedOperationException(SCHEMA_IMMUTABLE_EXCEPTION_MSG);
    }

    public RealmObjectSchema setRequired(String str, boolean z) {
        throw new UnsupportedOperationException(SCHEMA_IMMUTABLE_EXCEPTION_MSG);
    }

    public RealmObjectSchema setNullable(String str, boolean z) {
        throw new UnsupportedOperationException(SCHEMA_IMMUTABLE_EXCEPTION_MSG);
    }

    public RealmObjectSchema transform(Function function) {
        throw new UnsupportedOperationException(SCHEMA_IMMUTABLE_EXCEPTION_MSG);
    }

    /* access modifiers changed from: 0000 */
    public FieldDescriptor getColumnIndices(String str, RealmFieldType... realmFieldTypeArr) {
        return FieldDescriptor.createStandardFieldDescriptor(getSchemaConnector(), getTable(), str, realmFieldTypeArr);
    }
}

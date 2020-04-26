package io.realm;

import io.realm.internal.ColumnInfo;
import io.realm.internal.fields.FieldDescriptor.SchemaProxy;

class SchemaConnector implements SchemaProxy {
    private final RealmSchema schema;

    public SchemaConnector(RealmSchema realmSchema) {
        this.schema = realmSchema;
    }

    public boolean hasCache() {
        return this.schema.haveColumnInfo();
    }

    public ColumnInfo getColumnInfo(String str) {
        return this.schema.getColumnInfo(str);
    }

    public long getNativeTablePtr(String str) {
        return this.schema.getTable(str).getNativePtr();
    }
}

package io.realm;

import io.realm.internal.OsObjectStore;
import io.realm.internal.Table;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

class MutableRealmSchema extends RealmSchema {
    MutableRealmSchema(BaseRealm baseRealm) {
        super(baseRealm, null);
    }

    public RealmObjectSchema get(String str) {
        checkNotEmpty(str, "Null or empty class names are not allowed");
        String tableNameForClass = Table.getTableNameForClass(str);
        if (!this.realm.getSharedRealm().hasTable(tableNameForClass)) {
            return null;
        }
        return new MutableRealmObjectSchema(this.realm, this, this.realm.getSharedRealm().getTable(tableNameForClass));
    }

    public Set<RealmObjectSchema> getAll() {
        int size = (int) this.realm.getSharedRealm().size();
        LinkedHashSet linkedHashSet = new LinkedHashSet(size);
        for (int i = 0; i < size; i++) {
            RealmObjectSchema realmObjectSchema = get(Table.getClassNameForTable(this.realm.getSharedRealm().getTableName(i)));
            if (realmObjectSchema != null) {
                linkedHashSet.add(realmObjectSchema);
            }
        }
        return linkedHashSet;
    }

    public RealmObjectSchema create(String str) {
        checkNotEmpty(str, "Null or empty class names are not allowed");
        String tableNameForClass = Table.getTableNameForClass(str);
        if (str.length() <= Table.CLASS_NAME_MAX_LENGTH) {
            return new MutableRealmObjectSchema(this.realm, this, this.realm.getSharedRealm().createTable(tableNameForClass));
        }
        throw new IllegalArgumentException(String.format(Locale.US, "Class name is too long. Limit is %1$d characters: %2$s", new Object[]{Integer.valueOf(Table.CLASS_NAME_MAX_LENGTH), Integer.valueOf(str.length())}));
    }

    public RealmObjectSchema createWithPrimaryKeyField(String str, String str2, Class<?> cls, FieldAttribute... fieldAttributeArr) {
        checkNotEmpty(str, "Null or empty class names are not allowed");
        RealmObjectSchema.checkLegalName(str2);
        String checkAndGetTableNameFromClassName = checkAndGetTableNameFromClassName(str);
        FieldMetaData fieldMetaData = (FieldMetaData) RealmObjectSchema.getSupportedSimpleFields().get(cls);
        boolean z = true;
        if (fieldMetaData == null || !(fieldMetaData.fieldType == RealmFieldType.STRING || fieldMetaData.fieldType == RealmFieldType.INTEGER)) {
            throw new IllegalArgumentException(String.format("Realm doesn't support primary key field type '%s'.", new Object[]{cls}));
        }
        if (fieldMetaData.fieldType != RealmFieldType.STRING) {
            z = false;
        }
        boolean z2 = fieldMetaData.defaultNullable;
        if (MutableRealmObjectSchema.containsAttribute(fieldAttributeArr, FieldAttribute.REQUIRED)) {
            z2 = false;
        }
        return new MutableRealmObjectSchema(this.realm, this, this.realm.getSharedRealm().createTableWithPrimaryKey(checkAndGetTableNameFromClassName, str2, z, z2));
    }

    public void remove(String str) {
        this.realm.checkNotInSync();
        checkNotEmpty(str, "Null or empty class names are not allowed");
        String tableNameForClass = Table.getTableNameForClass(str);
        if (OsObjectStore.deleteTableForObject(this.realm.getSharedRealm(), str)) {
            removeFromClassNameToSchemaMap(tableNameForClass);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Cannot remove class because it is not in this Realm: ");
        sb.append(str);
        throw new IllegalArgumentException(sb.toString());
    }

    public RealmObjectSchema rename(String str, String str2) {
        this.realm.checkNotInSync();
        String str3 = "Class names cannot be empty or null";
        checkNotEmpty(str, str3);
        checkNotEmpty(str2, str3);
        String tableNameForClass = Table.getTableNameForClass(str);
        String tableNameForClass2 = Table.getTableNameForClass(str2);
        StringBuilder sb = new StringBuilder();
        sb.append("Cannot rename class because it doesn't exist in this Realm: ");
        sb.append(str);
        checkHasTable(str, sb.toString());
        if (!this.realm.getSharedRealm().hasTable(tableNameForClass2)) {
            String primaryKeyForObject = OsObjectStore.getPrimaryKeyForObject(this.realm.sharedRealm, str);
            if (primaryKeyForObject != null) {
                OsObjectStore.setPrimaryKeyForObject(this.realm.sharedRealm, str, null);
            }
            this.realm.getSharedRealm().renameTable(tableNameForClass, tableNameForClass2);
            Table table = this.realm.getSharedRealm().getTable(tableNameForClass2);
            if (primaryKeyForObject != null) {
                OsObjectStore.setPrimaryKeyForObject(this.realm.sharedRealm, str2, primaryKeyForObject);
            }
            RealmObjectSchema removeFromClassNameToSchemaMap = removeFromClassNameToSchemaMap(tableNameForClass);
            if (removeFromClassNameToSchemaMap == null || !removeFromClassNameToSchemaMap.getTable().isValid() || !removeFromClassNameToSchemaMap.getClassName().equals(str2)) {
                removeFromClassNameToSchemaMap = new MutableRealmObjectSchema(this.realm, this, table);
            }
            putToClassNameToSchemaMap(tableNameForClass2, removeFromClassNameToSchemaMap);
            return removeFromClassNameToSchemaMap;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(" cannot be renamed because the new class already exists: ");
        sb2.append(str2);
        throw new IllegalArgumentException(sb2.toString());
    }

    private String checkAndGetTableNameFromClassName(String str) {
        if (str.length() <= Table.CLASS_NAME_MAX_LENGTH) {
            return Table.getTableNameForClass(str);
        }
        throw new IllegalArgumentException(String.format(Locale.US, "Class name is too long. Limit is %1$d characters: %2$s", new Object[]{Integer.valueOf(Table.CLASS_NAME_MAX_LENGTH), Integer.valueOf(str.length())}));
    }
}

package io.realm.internal.fields;

import io.realm.RealmFieldType;
import io.realm.internal.ColumnInfo;
import io.realm.internal.ColumnInfo.ColumnDetails;
import io.realm.internal.fields.FieldDescriptor.SchemaProxy;
import java.util.List;
import java.util.Locale;
import java.util.Set;

class CachedFieldDescriptor extends FieldDescriptor {
    private final String className;
    private final SchemaProxy schema;

    CachedFieldDescriptor(SchemaProxy schemaProxy, String str, String str2, Set<RealmFieldType> set, Set<RealmFieldType> set2) {
        super(str2, set, set2);
        this.className = str;
        this.schema = schemaProxy;
    }

    /* access modifiers changed from: protected */
    public void compileFieldDescription(List<String> list) {
        long j;
        int size = list.size();
        long[] jArr = new long[size];
        long[] jArr2 = new long[size];
        String str = null;
        RealmFieldType realmFieldType = null;
        String str2 = this.className;
        int i = 0;
        while (i < size) {
            str = (String) list.get(i);
            if (str == null || str.length() <= 0) {
                throw new IllegalArgumentException("Invalid query: Field descriptor contains an empty field.  A field description may not begin with or contain adjacent periods ('.').");
            }
            ColumnInfo columnInfo = this.schema.getColumnInfo(str2);
            if (columnInfo != null) {
                ColumnDetails columnDetails = columnInfo.getColumnDetails(str);
                if (columnDetails != null) {
                    RealmFieldType realmFieldType2 = columnDetails.columnType;
                    if (i < size - 1) {
                        verifyInternalColumnType(str2, str, realmFieldType2);
                        str2 = columnDetails.linkedClassName;
                    }
                    jArr[i] = columnDetails.columnIndex;
                    if (realmFieldType2 != RealmFieldType.LINKING_OBJECTS) {
                        j = 0;
                    } else {
                        j = this.schema.getNativeTablePtr(columnDetails.linkedClassName);
                    }
                    jArr2[i] = j;
                    i++;
                    realmFieldType = realmFieldType2;
                } else {
                    throw new IllegalArgumentException(String.format(Locale.US, "Invalid query: field '%s' not found in class '%s'.", new Object[]{str, str2}));
                }
            } else {
                throw new IllegalArgumentException(String.format(Locale.US, "Invalid query: class '%s' not found in this schema.", new Object[]{str2}));
            }
        }
        setCompilationResults(str2, str, realmFieldType, jArr, jArr2);
    }
}

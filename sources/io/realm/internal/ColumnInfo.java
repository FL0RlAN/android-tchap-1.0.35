package io.realm.internal;

import io.realm.RealmFieldType;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;

public abstract class ColumnInfo {
    private final Map<String, ColumnDetails> indicesFromColumnNames;
    private final Map<String, ColumnDetails> indicesFromJavaFieldNames;
    private final Map<String, String> javaFieldNameToInternalNames;
    private final boolean mutable;

    public static final class ColumnDetails {
        public final long columnIndex;
        public final RealmFieldType columnType;
        public final String linkedClassName;

        private ColumnDetails(long j, RealmFieldType realmFieldType, @Nullable String str) {
            this.columnIndex = j;
            this.columnType = realmFieldType;
            this.linkedClassName = str;
        }

        ColumnDetails(Property property) {
            this(property.getColumnIndex(), property.getType(), property.getLinkedObjectName());
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("ColumnDetails[");
            sb.append(this.columnIndex);
            String str = ", ";
            sb.append(str);
            sb.append(this.columnType);
            sb.append(str);
            sb.append(this.linkedClassName);
            sb.append("]");
            return sb.toString();
        }
    }

    /* access modifiers changed from: protected */
    public abstract ColumnInfo copy(boolean z);

    /* access modifiers changed from: protected */
    public abstract void copy(ColumnInfo columnInfo, ColumnInfo columnInfo2);

    protected ColumnInfo(int i) {
        this(i, true);
    }

    protected ColumnInfo(@Nullable ColumnInfo columnInfo, boolean z) {
        this(columnInfo == null ? 0 : columnInfo.indicesFromJavaFieldNames.size(), z);
        if (columnInfo != null) {
            this.indicesFromJavaFieldNames.putAll(columnInfo.indicesFromJavaFieldNames);
        }
    }

    private ColumnInfo(int i, boolean z) {
        this.indicesFromJavaFieldNames = new HashMap(i);
        this.indicesFromColumnNames = new HashMap(i);
        this.javaFieldNameToInternalNames = new HashMap(i);
        this.mutable = z;
    }

    public final boolean isMutable() {
        return this.mutable;
    }

    public long getColumnIndex(String str) {
        ColumnDetails columnDetails = (ColumnDetails) this.indicesFromJavaFieldNames.get(str);
        if (columnDetails == null) {
            return -1;
        }
        return columnDetails.columnIndex;
    }

    @Nullable
    public ColumnDetails getColumnDetails(String str) {
        return (ColumnDetails) this.indicesFromJavaFieldNames.get(str);
    }

    @Nullable
    public String getInternalFieldName(String str) {
        return (String) this.javaFieldNameToInternalNames.get(str);
    }

    public void copyFrom(ColumnInfo columnInfo) {
        if (!this.mutable) {
            throw new UnsupportedOperationException("Attempt to modify an immutable ColumnInfo");
        } else if (columnInfo != null) {
            this.indicesFromJavaFieldNames.clear();
            this.indicesFromJavaFieldNames.putAll(columnInfo.indicesFromJavaFieldNames);
            this.indicesFromColumnNames.clear();
            this.indicesFromColumnNames.putAll(columnInfo.indicesFromColumnNames);
            this.javaFieldNameToInternalNames.clear();
            this.javaFieldNameToInternalNames.putAll(columnInfo.javaFieldNameToInternalNames);
            copy(columnInfo, this);
        } else {
            throw new NullPointerException("Attempt to copy null ColumnInfo");
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ColumnInfo[");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("mutable=");
        sb2.append(this.mutable);
        sb.append(sb2.toString());
        String str = ",";
        sb.append(str);
        String str2 = "->";
        boolean z = false;
        String str3 = "]";
        if (this.indicesFromJavaFieldNames != null) {
            sb.append("JavaFieldNames=[");
            boolean z2 = false;
            for (Entry entry : this.indicesFromJavaFieldNames.entrySet()) {
                if (z2) {
                    sb.append(str);
                }
                sb.append((String) entry.getKey());
                sb.append(str2);
                sb.append(entry.getValue());
                z2 = true;
            }
            sb.append(str3);
        }
        if (this.indicesFromColumnNames != null) {
            sb.append(", InternalFieldNames=[");
            for (Entry entry2 : this.indicesFromColumnNames.entrySet()) {
                if (z) {
                    sb.append(str);
                }
                sb.append((String) entry2.getKey());
                sb.append(str2);
                sb.append(entry2.getValue());
                z = true;
            }
            sb.append(str3);
        }
        sb.append(str3);
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public final long addColumnDetails(String str, String str2, OsObjectSchemaInfo osObjectSchemaInfo) {
        Property property = osObjectSchemaInfo.getProperty(str2);
        ColumnDetails columnDetails = new ColumnDetails(property);
        this.indicesFromJavaFieldNames.put(str, columnDetails);
        this.indicesFromColumnNames.put(str2, columnDetails);
        this.javaFieldNameToInternalNames.put(str, str2);
        return property.getColumnIndex();
    }

    /* access modifiers changed from: protected */
    public final void addBacklinkDetails(OsSchemaInfo osSchemaInfo, String str, String str2, String str3) {
        long columnIndex = osSchemaInfo.getObjectSchemaInfo(str2).getProperty(str3).getColumnIndex();
        Map<String, ColumnDetails> map = this.indicesFromJavaFieldNames;
        ColumnDetails columnDetails = new ColumnDetails(columnIndex, RealmFieldType.LINKING_OBJECTS, str2);
        map.put(str, columnDetails);
    }

    public Map<String, ColumnDetails> getIndicesMap() {
        return this.indicesFromJavaFieldNames;
    }
}

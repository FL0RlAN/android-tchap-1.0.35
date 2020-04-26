package io.realm.internal;

import io.realm.RealmModel;
import io.realm.exceptions.RealmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nonnull;

public final class ColumnIndices {
    private final Map<Class<? extends RealmModel>, ColumnInfo> classToColumnInfoMap = new HashMap();
    private final RealmProxyMediator mediator;
    private final OsSchemaInfo osSchemaInfo;
    private final Map<String, ColumnInfo> simpleClassNameToColumnInfoMap = new HashMap();

    public ColumnIndices(RealmProxyMediator realmProxyMediator, OsSchemaInfo osSchemaInfo2) {
        this.mediator = realmProxyMediator;
        this.osSchemaInfo = osSchemaInfo2;
    }

    @Nonnull
    public ColumnInfo getColumnInfo(Class<? extends RealmModel> cls) {
        ColumnInfo columnInfo = (ColumnInfo) this.classToColumnInfoMap.get(cls);
        if (columnInfo != null) {
            return columnInfo;
        }
        ColumnInfo createColumnInfo = this.mediator.createColumnInfo(cls, this.osSchemaInfo);
        this.classToColumnInfoMap.put(cls, createColumnInfo);
        return createColumnInfo;
    }

    @Nonnull
    public ColumnInfo getColumnInfo(String str) {
        ColumnInfo columnInfo = (ColumnInfo) this.simpleClassNameToColumnInfoMap.get(str);
        if (columnInfo == null) {
            Iterator it = this.mediator.getModelClasses().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Class cls = (Class) it.next();
                if (this.mediator.getSimpleClassName(cls).equals(str)) {
                    columnInfo = getColumnInfo(cls);
                    this.simpleClassNameToColumnInfoMap.put(str, columnInfo);
                    break;
                }
            }
        }
        if (columnInfo != null) {
            return columnInfo;
        }
        throw new RealmException(String.format(Locale.US, "'%s' doesn't exist in current schema.", new Object[]{str}));
    }

    public void refresh() {
        for (Entry entry : this.classToColumnInfoMap.entrySet()) {
            ((ColumnInfo) entry.getValue()).copyFrom(this.mediator.createColumnInfo((Class) entry.getKey(), this.osSchemaInfo));
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ColumnIndices[");
        boolean z = false;
        for (Entry entry : this.classToColumnInfoMap.entrySet()) {
            if (z) {
                sb.append(",");
            }
            sb.append(((Class) entry.getKey()).getSimpleName());
            sb.append("->");
            sb.append(entry.getValue());
            z = true;
        }
        sb.append("]");
        return sb.toString();
    }
}

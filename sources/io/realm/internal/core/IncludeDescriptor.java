package io.realm.internal.core;

import io.realm.RealmFieldType;
import io.realm.internal.NativeObject;
import io.realm.internal.Table;
import io.realm.internal.fields.FieldDescriptor;
import io.realm.internal.fields.FieldDescriptor.SchemaProxy;
import java.util.EnumSet;

public class IncludeDescriptor implements NativeObject {
    private static final long nativeFinalizerMethodPtr = nativeGetFinalizerMethodPtr();
    private final long nativePtr;

    private static native long nativeCreate(long j, long[] jArr, long[] jArr2);

    private static native long nativeGetFinalizerMethodPtr();

    public static IncludeDescriptor createInstance(SchemaProxy schemaProxy, Table table, String str) {
        FieldDescriptor createFieldDescriptor = FieldDescriptor.createFieldDescriptor(schemaProxy, table, str, EnumSet.of(RealmFieldType.OBJECT, RealmFieldType.LIST, RealmFieldType.LINKING_OBJECTS), EnumSet.of(RealmFieldType.LINKING_OBJECTS));
        return new IncludeDescriptor(table, createFieldDescriptor.getColumnIndices(), createFieldDescriptor.getNativeTablePointers());
    }

    private IncludeDescriptor(Table table, long[] jArr, long[] jArr2) {
        this.nativePtr = nativeCreate(table.getNativePtr(), jArr, jArr2);
    }

    public long getNativePtr() {
        return this.nativePtr;
    }

    public long getNativeFinalizerPtr() {
        return nativeFinalizerMethodPtr;
    }
}

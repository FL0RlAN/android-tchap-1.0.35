package io.realm.internal.core;

import io.realm.internal.NativeObject;

public class DescriptorOrdering implements NativeObject {
    private static final long nativeFinalizerMethodPtr = nativeGetFinalizerMethodPtr();
    private boolean distinctDefined = false;
    private boolean limitDefined = false;
    private final long nativePtr = nativeCreate();
    private boolean sortDefined = false;

    private static native void nativeAppendDistinct(long j, QueryDescriptor queryDescriptor);

    private static native void nativeAppendInclude(long j, long j2);

    private static native void nativeAppendLimit(long j, long j2);

    private static native void nativeAppendSort(long j, QueryDescriptor queryDescriptor);

    private static native long nativeCreate();

    private static native long nativeGetFinalizerMethodPtr();

    private static native boolean nativeIsEmpty(long j);

    public long getNativePtr() {
        return this.nativePtr;
    }

    public long getNativeFinalizerPtr() {
        return nativeFinalizerMethodPtr;
    }

    public void appendSort(QueryDescriptor queryDescriptor) {
        if (!this.sortDefined) {
            nativeAppendSort(this.nativePtr, queryDescriptor);
            this.sortDefined = true;
            return;
        }
        throw new IllegalStateException("A sorting order was already defined. It cannot be redefined");
    }

    public void appendDistinct(QueryDescriptor queryDescriptor) {
        if (!this.distinctDefined) {
            nativeAppendDistinct(this.nativePtr, queryDescriptor);
            this.distinctDefined = true;
            return;
        }
        throw new IllegalStateException("A distinct field was already defined. It cannot be redefined");
    }

    public void setLimit(long j) {
        if (!this.limitDefined) {
            nativeAppendLimit(this.nativePtr, j);
            this.limitDefined = true;
            return;
        }
        throw new IllegalStateException("A limit was already set. It cannot be redefined.");
    }

    public void appendIncludes(IncludeDescriptor includeDescriptor) {
        nativeAppendInclude(this.nativePtr, includeDescriptor.getNativePtr());
    }

    public boolean isEmpty() {
        return nativeIsEmpty(this.nativePtr);
    }
}

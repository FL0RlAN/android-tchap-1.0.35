package io.realm.internal;

import io.realm.RealmFieldType;
import java.util.Locale;

public class Property implements NativeObject {
    public static final boolean INDEXED = true;
    public static final boolean PRIMARY_KEY = true;
    public static final boolean REQUIRED = true;
    public static final int TYPE_ARRAY = 128;
    public static final int TYPE_BOOL = 1;
    public static final int TYPE_DATA = 3;
    public static final int TYPE_DATE = 4;
    public static final int TYPE_DOUBLE = 6;
    public static final int TYPE_FLOAT = 5;
    public static final int TYPE_INT = 0;
    public static final int TYPE_LINKING_OBJECTS = 8;
    public static final int TYPE_NULLABLE = 64;
    public static final int TYPE_OBJECT = 7;
    public static final int TYPE_REQUIRED = 0;
    public static final int TYPE_STRING = 2;
    private static final long nativeFinalizerPtr = nativeGetFinalizerPtr();
    private long nativePtr;

    static native long nativeCreateComputedLinkProperty(String str, String str2, String str3);

    static native long nativeCreatePersistedLinkProperty(String str, int i, String str2);

    static native long nativeCreatePersistedProperty(String str, int i, boolean z, boolean z2);

    private static native long nativeGetColumnIndex(long j);

    private static native long nativeGetFinalizerPtr();

    private static native String nativeGetLinkedObjectName(long j);

    private static native int nativeGetType(long j);

    Property(long j) {
        this.nativePtr = j;
        NativeContext.dummyContext.addReference(this);
    }

    static int convertFromRealmFieldType(RealmFieldType realmFieldType, boolean z) {
        int i = 1;
        int i2 = 0;
        switch (realmFieldType) {
            case OBJECT:
                return 71;
            case LIST:
                return 135;
            case LINKING_OBJECTS:
                return 136;
            case INTEGER:
                i = 0;
                break;
            case BOOLEAN:
                break;
            case STRING:
                i = 2;
                break;
            case BINARY:
                i = 3;
                break;
            case DATE:
                i = 4;
                break;
            case FLOAT:
                i = 5;
                break;
            case DOUBLE:
                i = 6;
                break;
            case INTEGER_LIST:
                i = 128;
                break;
            case BOOLEAN_LIST:
                i = 129;
                break;
            case STRING_LIST:
                i = 130;
                break;
            case BINARY_LIST:
                i = 131;
                break;
            case DATE_LIST:
                i = 132;
                break;
            case FLOAT_LIST:
                i = 133;
                break;
            case DOUBLE_LIST:
                i = 134;
                break;
            default:
                throw new IllegalArgumentException(String.format(Locale.US, "Unsupported filed type: '%s'.", new Object[]{realmFieldType.name()}));
        }
        if (!z) {
            i2 = 64;
        }
        return i | i2;
    }

    private static RealmFieldType convertToRealmFieldType(int i) {
        int i2 = i & -65;
        switch (i2) {
            case 0:
                return RealmFieldType.INTEGER;
            case 1:
                return RealmFieldType.BOOLEAN;
            case 2:
                return RealmFieldType.STRING;
            case 3:
                return RealmFieldType.BINARY;
            case 4:
                return RealmFieldType.DATE;
            case 5:
                return RealmFieldType.FLOAT;
            case 6:
                return RealmFieldType.DOUBLE;
            case 7:
                return RealmFieldType.OBJECT;
            default:
                switch (i2) {
                    case 128:
                        return RealmFieldType.INTEGER_LIST;
                    case 129:
                        return RealmFieldType.BOOLEAN_LIST;
                    case 130:
                        return RealmFieldType.STRING_LIST;
                    case 131:
                        return RealmFieldType.BINARY_LIST;
                    case 132:
                        return RealmFieldType.DATE_LIST;
                    case 133:
                        return RealmFieldType.FLOAT_LIST;
                    case 134:
                        return RealmFieldType.DOUBLE_LIST;
                    case 135:
                        return RealmFieldType.LIST;
                    case 136:
                        return RealmFieldType.LINKING_OBJECTS;
                    default:
                        throw new IllegalArgumentException(String.format(Locale.US, "Unsupported property type: '%d'", new Object[]{Integer.valueOf(i)}));
                }
        }
    }

    public RealmFieldType getType() {
        return convertToRealmFieldType(nativeGetType(this.nativePtr));
    }

    public String getLinkedObjectName() {
        return nativeGetLinkedObjectName(this.nativePtr);
    }

    public long getColumnIndex() {
        return nativeGetColumnIndex(this.nativePtr);
    }

    public long getNativePtr() {
        return this.nativePtr;
    }

    public long getNativeFinalizerPtr() {
        return nativeFinalizerPtr;
    }
}

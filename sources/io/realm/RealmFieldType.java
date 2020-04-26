package io.realm;

import java.nio.ByteBuffer;
import java.util.Date;

public enum RealmFieldType {
    INTEGER(0),
    BOOLEAN(1),
    STRING(2),
    BINARY(4),
    DATE(8),
    FLOAT(9),
    DOUBLE(10),
    OBJECT(12),
    LIST(13),
    LINKING_OBJECTS(14),
    INTEGER_LIST(128),
    BOOLEAN_LIST(129),
    STRING_LIST(130),
    BINARY_LIST(132),
    DATE_LIST(136),
    FLOAT_LIST(137),
    DOUBLE_LIST(138);
    
    private static final RealmFieldType[] basicTypes = null;
    private static final RealmFieldType[] listTypes = null;
    private final int nativeValue;

    static {
        int i;
        RealmFieldType[] values;
        basicTypes = new RealmFieldType[15];
        listTypes = new RealmFieldType[15];
        for (RealmFieldType realmFieldType : values()) {
            int i2 = realmFieldType.nativeValue;
            if (i2 < 128) {
                basicTypes[i2] = realmFieldType;
            } else {
                listTypes[i2 - 128] = realmFieldType;
            }
        }
    }

    private RealmFieldType(int i) {
        this.nativeValue = i;
    }

    public int getNativeValue() {
        return this.nativeValue;
    }

    public boolean isValid(Object obj) {
        int i = this.nativeValue;
        boolean z = true;
        if (i == 0) {
            if (!(obj instanceof Long) && !(obj instanceof Integer) && !(obj instanceof Short) && !(obj instanceof Byte)) {
                z = false;
            }
            return z;
        } else if (i == 1) {
            return obj instanceof Boolean;
        } else {
            if (i == 2) {
                return obj instanceof String;
            }
            if (i == 4) {
                if (!(obj instanceof byte[]) && !(obj instanceof ByteBuffer)) {
                    z = false;
                }
                return z;
            } else if (i == 132) {
                return false;
            } else {
                switch (i) {
                    case 8:
                        return obj instanceof Date;
                    case 9:
                        return obj instanceof Float;
                    case 10:
                        return obj instanceof Double;
                    default:
                        switch (i) {
                            case 12:
                            case 13:
                            case 14:
                                break;
                            default:
                                switch (i) {
                                    case 128:
                                    case 129:
                                    case 130:
                                        break;
                                    default:
                                        switch (i) {
                                            case 136:
                                            case 137:
                                            case 138:
                                                break;
                                            default:
                                                StringBuilder sb = new StringBuilder();
                                                sb.append("Unsupported Realm type:  ");
                                                sb.append(this);
                                                throw new RuntimeException(sb.toString());
                                        }
                                }
                        }
                        return false;
                }
            }
        }
    }

    public static RealmFieldType fromNativeValue(int i) {
        if (i >= 0) {
            RealmFieldType[] realmFieldTypeArr = basicTypes;
            if (i < realmFieldTypeArr.length) {
                RealmFieldType realmFieldType = realmFieldTypeArr[i];
                if (realmFieldType != null) {
                    return realmFieldType;
                }
            }
        }
        if (128 <= i) {
            int i2 = i - 128;
            RealmFieldType[] realmFieldTypeArr2 = listTypes;
            if (i2 < realmFieldTypeArr2.length) {
                RealmFieldType realmFieldType2 = realmFieldTypeArr2[i2];
                if (realmFieldType2 != null) {
                    return realmFieldType2;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid native Realm type: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }
}

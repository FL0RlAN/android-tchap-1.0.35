package org.matrix.androidsdk.crypto.data;

import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\t\u0010\t\u001a\u00020\u0003HÆ\u0003J\t\u0010\n\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\u000b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u000f\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007¨\u0006\u0012"}, d2 = {"Lorg/matrix/androidsdk/crypto/data/ImportRoomKeysResult;", "", "totalNumberOfKeys", "", "successfullyNumberOfImportedKeys", "(II)V", "getSuccessfullyNumberOfImportedKeys", "()I", "getTotalNumberOfKeys", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: ImportRoomKeysResult.kt */
public final class ImportRoomKeysResult {
    private final int successfullyNumberOfImportedKeys;
    private final int totalNumberOfKeys;

    public static /* synthetic */ ImportRoomKeysResult copy$default(ImportRoomKeysResult importRoomKeysResult, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = importRoomKeysResult.totalNumberOfKeys;
        }
        if ((i3 & 2) != 0) {
            i2 = importRoomKeysResult.successfullyNumberOfImportedKeys;
        }
        return importRoomKeysResult.copy(i, i2);
    }

    public final int component1() {
        return this.totalNumberOfKeys;
    }

    public final int component2() {
        return this.successfullyNumberOfImportedKeys;
    }

    public final ImportRoomKeysResult copy(int i, int i2) {
        return new ImportRoomKeysResult(i, i2);
    }

    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof ImportRoomKeysResult) {
                ImportRoomKeysResult importRoomKeysResult = (ImportRoomKeysResult) obj;
                if (this.totalNumberOfKeys == importRoomKeysResult.totalNumberOfKeys) {
                    if (this.successfullyNumberOfImportedKeys == importRoomKeysResult.successfullyNumberOfImportedKeys) {
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (this.totalNumberOfKeys * 31) + this.successfullyNumberOfImportedKeys;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ImportRoomKeysResult(totalNumberOfKeys=");
        sb.append(this.totalNumberOfKeys);
        sb.append(", successfullyNumberOfImportedKeys=");
        sb.append(this.successfullyNumberOfImportedKeys);
        sb.append(")");
        return sb.toString();
    }

    public ImportRoomKeysResult(int i, int i2) {
        this.totalNumberOfKeys = i;
        this.successfullyNumberOfImportedKeys = i2;
    }

    public final int getTotalNumberOfKeys() {
        return this.totalNumberOfKeys;
    }

    public final int getSuccessfullyNumberOfImportedKeys() {
        return this.successfullyNumberOfImportedKeys;
    }
}

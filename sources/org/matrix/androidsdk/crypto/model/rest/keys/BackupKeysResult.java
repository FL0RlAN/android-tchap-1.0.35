package org.matrix.androidsdk.crypto.model.rest.keys;

import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u001e\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u0010\n\u0002\u0010\t\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\n\u001a\u0004\u0018\u00010\u000bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000f¨\u0006\u0010"}, d2 = {"Lorg/matrix/androidsdk/crypto/model/rest/keys/BackupKeysResult;", "", "()V", "count", "", "getCount", "()Ljava/lang/Integer;", "setCount", "(Ljava/lang/Integer;)V", "Ljava/lang/Integer;", "hash", "", "getHash", "()Ljava/lang/String;", "setHash", "(Ljava/lang/String;)V", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: BackupKeysResult.kt */
public final class BackupKeysResult {
    private Integer count;
    private String hash;

    public final String getHash() {
        return this.hash;
    }

    public final void setHash(String str) {
        this.hash = str;
    }

    public final Integer getCount() {
        return this.count;
    }

    public final void setCount(Integer num) {
        this.count = num;
    }
}

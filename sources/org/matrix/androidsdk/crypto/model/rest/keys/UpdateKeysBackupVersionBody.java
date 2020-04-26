package org.matrix.androidsdk.crypto.model.rest.keys;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.crypto.model.keys.KeysAlgorithmAndData;
import org.matrix.androidsdk.rest.model.terms.TermsResponse;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, d2 = {"Lorg/matrix/androidsdk/crypto/model/rest/keys/UpdateKeysBackupVersionBody;", "Lorg/matrix/androidsdk/crypto/model/keys/KeysAlgorithmAndData;", "version", "", "(Ljava/lang/String;)V", "getVersion", "()Ljava/lang/String;", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: UpdateKeysBackupVersionBody.kt */
public final class UpdateKeysBackupVersionBody extends KeysAlgorithmAndData {
    private final String version;

    public UpdateKeysBackupVersionBody(String str) {
        Intrinsics.checkParameterIsNotNull(str, TermsResponse.VERSION);
        this.version = str;
    }

    public final String getVersion() {
        return this.version;
    }
}
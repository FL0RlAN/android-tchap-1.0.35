package org.matrix.androidsdk.crypto.interfaces;

import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005¨\u0006\u0006"}, d2 = {"Lorg/matrix/androidsdk/crypto/interfaces/CryptoStore;", "", "eventStreamToken", "", "getEventStreamToken", "()Ljava/lang/String;", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: CryptoStore.kt */
public interface CryptoStore {
    String getEventStreamToken();
}

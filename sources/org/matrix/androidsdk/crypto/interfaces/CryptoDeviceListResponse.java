package org.matrix.androidsdk.crypto.interfaces;

import java.util.List;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010!\n\u0002\u0010\u000e\n\u0002\b\u0005\bf\u0018\u00002\u00020\u0001R\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0018\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\u0006¨\u0006\t"}, d2 = {"Lorg/matrix/androidsdk/crypto/interfaces/CryptoDeviceListResponse;", "", "changed", "", "", "getChanged", "()Ljava/util/List;", "left", "getLeft", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: CryptoDeviceListResponse.kt */
public interface CryptoDeviceListResponse {
    List<String> getChanged();

    List<String> getLeft();
}

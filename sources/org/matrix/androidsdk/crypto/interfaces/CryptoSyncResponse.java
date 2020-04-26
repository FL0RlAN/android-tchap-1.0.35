package org.matrix.androidsdk.crypto.interfaces;

import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001R\u0014\u0010\u0002\u001a\u0004\u0018\u00010\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0014\u0010\u0006\u001a\u0004\u0018\u00010\u0007X¦\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\t¨\u0006\n"}, d2 = {"Lorg/matrix/androidsdk/crypto/interfaces/CryptoSyncResponse;", "", "deviceLists", "Lorg/matrix/androidsdk/crypto/interfaces/CryptoDeviceListResponse;", "getDeviceLists", "()Lorg/matrix/androidsdk/crypto/interfaces/CryptoDeviceListResponse;", "deviceOneTimeKeysCount", "Lorg/matrix/androidsdk/crypto/interfaces/CryptoDeviceOneTimeKeysCountSyncResponse;", "getDeviceOneTimeKeysCount", "()Lorg/matrix/androidsdk/crypto/interfaces/CryptoDeviceOneTimeKeysCountSyncResponse;", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: CryptoSyncResponse.kt */
public interface CryptoSyncResponse {
    CryptoDeviceListResponse getDeviceLists();

    CryptoDeviceOneTimeKeysCountSyncResponse getDeviceOneTimeKeysCount();
}

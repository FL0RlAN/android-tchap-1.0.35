package org.matrix.androidsdk.crypto.interfaces;

import kotlin.Metadata;
import org.matrix.androidsdk.crypto.MXCrypto;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\b\u0010\n\u001a\u00020\u0007H&J\b\u0010\u000b\u001a\u00020\fH&J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0007H&R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0007X¦\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\t¨\u0006\u0010"}, d2 = {"Lorg/matrix/androidsdk/crypto/interfaces/CryptoSession;", "", "dataHandler", "Lorg/matrix/androidsdk/crypto/interfaces/CryptoDataHandler;", "getDataHandler", "()Lorg/matrix/androidsdk/crypto/interfaces/CryptoDataHandler;", "myUserId", "", "getMyUserId", "()Ljava/lang/String;", "getDeviceId", "requireCrypto", "Lorg/matrix/androidsdk/crypto/MXCrypto;", "setDeviceId", "", "deviceId", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: CryptoSession.kt */
public interface CryptoSession {
    CryptoDataHandler getDataHandler();

    String getDeviceId();

    String getMyUserId();

    MXCrypto requireCrypto();

    void setDeviceId(String str);
}

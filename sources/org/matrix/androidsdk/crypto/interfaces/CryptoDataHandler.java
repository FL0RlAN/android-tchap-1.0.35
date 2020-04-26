package org.matrix.androidsdk.crypto.interfaces;

import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH&J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH&J\u0010\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u0010H&R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005¨\u0006\u0011"}, d2 = {"Lorg/matrix/androidsdk/crypto/interfaces/CryptoDataHandler;", "", "store", "Lorg/matrix/androidsdk/crypto/interfaces/CryptoStore;", "getStore", "()Lorg/matrix/androidsdk/crypto/interfaces/CryptoStore;", "getRoom", "Lorg/matrix/androidsdk/crypto/interfaces/CryptoRoom;", "roomId", "", "onEventDecrypted", "", "event", "Lorg/matrix/androidsdk/crypto/interfaces/CryptoEvent;", "setCryptoEventsListener", "eventListener", "Lorg/matrix/androidsdk/crypto/interfaces/CryptoEventListener;", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: CryptoDataHandler.kt */
public interface CryptoDataHandler {
    CryptoRoom getRoom(String str);

    CryptoStore getStore();

    void onEventDecrypted(CryptoEvent cryptoEvent);

    void setCryptoEventsListener(CryptoEventListener cryptoEventListener);
}

package org.matrix.androidsdk.crypto.interfaces;

import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&J\u0010\u0010\b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&¨\u0006\t"}, d2 = {"Lorg/matrix/androidsdk/crypto/interfaces/CryptoEventListener;", "", "onLiveEvent", "", "event", "Lorg/matrix/androidsdk/crypto/interfaces/CryptoEvent;", "roomState", "Lorg/matrix/androidsdk/crypto/interfaces/CryptoRoomState;", "onToDeviceEvent", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: CryptoEventListener.kt */
public interface CryptoEventListener {
    void onLiveEvent(CryptoEvent cryptoEvent, CryptoRoomState cryptoRoomState);

    void onToDeviceEvent(CryptoEvent cryptoEvent);
}

package org.matrix.androidsdk.crypto;

import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0007H&¨\u0006\b"}, d2 = {"Lorg/matrix/androidsdk/crypto/RoomKeysRequestListener;", "", "onRoomKeyRequest", "", "request", "Lorg/matrix/androidsdk/crypto/IncomingRoomKeyRequest;", "onRoomKeyRequestCancellation", "Lorg/matrix/androidsdk/crypto/IncomingRoomKeyRequestCancellation;", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: RoomKeysRequestListener.kt */
public interface RoomKeysRequestListener {
    void onRoomKeyRequest(IncomingRoomKeyRequest incomingRoomKeyRequest);

    void onRoomKeyRequestCancellation(IncomingRoomKeyRequestCancellation incomingRoomKeyRequestCancellation);
}

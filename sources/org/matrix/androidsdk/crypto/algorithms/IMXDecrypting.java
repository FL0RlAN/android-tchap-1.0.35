package org.matrix.androidsdk.crypto.algorithms;

import org.matrix.androidsdk.crypto.IncomingRoomKeyRequest;
import org.matrix.androidsdk.crypto.MXDecryptionException;
import org.matrix.androidsdk.crypto.MXEventDecryptionResult;
import org.matrix.androidsdk.crypto.interfaces.CryptoEvent;
import org.matrix.androidsdk.crypto.interfaces.CryptoSession;
import org.matrix.androidsdk.crypto.internal.MXCryptoImpl;

public interface IMXDecrypting {
    MXEventDecryptionResult decryptEvent(CryptoEvent cryptoEvent, String str) throws MXDecryptionException;

    boolean hasKeysForKeyRequest(IncomingRoomKeyRequest incomingRoomKeyRequest);

    void initWithMatrixSession(CryptoSession cryptoSession, MXCryptoImpl mXCryptoImpl);

    void onNewSession(String str, String str2);

    void onRoomKeyEvent(CryptoEvent cryptoEvent);

    void shareKeysWithDevice(IncomingRoomKeyRequest incomingRoomKeyRequest);
}

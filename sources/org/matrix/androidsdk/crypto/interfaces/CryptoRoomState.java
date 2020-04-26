package org.matrix.androidsdk.crypto.interfaces;

import java.util.List;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J\n\u0010\u0002\u001a\u0004\u0018\u00010\u0003H&J\u000e\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H&J\u0010\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0003H&J\b\u0010\t\u001a\u00020\nH&¨\u0006\u000b"}, d2 = {"Lorg/matrix/androidsdk/crypto/interfaces/CryptoRoomState;", "", "encryptionAlgorithm", "", "getLoadedMembersCrypto", "", "Lorg/matrix/androidsdk/crypto/interfaces/CryptoRoomMember;", "getMember", "userId", "isEncrypted", "", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: CryptoRoomState.kt */
public interface CryptoRoomState {
    String encryptionAlgorithm();

    List<CryptoRoomMember> getLoadedMembersCrypto();

    CryptoRoomMember getMember(String str);

    boolean isEncrypted();
}

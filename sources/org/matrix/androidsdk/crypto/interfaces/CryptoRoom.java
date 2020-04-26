package org.matrix.androidsdk.crypto.interfaces;

import java.util.List;
import kotlin.Metadata;
import org.matrix.androidsdk.core.callback.ApiCallback;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J\u001c\u0010\n\u001a\u00020\u000b2\u0012\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u000e0\rH&J\u001c\u0010\u0010\u001a\u00020\u000b2\u0012\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u000e0\rH&J\b\u0010\u0011\u001a\u00020\u0012H&R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0007X¦\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\t¨\u0006\u0013"}, d2 = {"Lorg/matrix/androidsdk/crypto/interfaces/CryptoRoom;", "", "roomId", "", "getRoomId", "()Ljava/lang/String;", "state", "Lorg/matrix/androidsdk/crypto/interfaces/CryptoRoomState;", "getState", "()Lorg/matrix/androidsdk/crypto/interfaces/CryptoRoomState;", "getActiveMembersAsyncCrypto", "", "callback", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "", "Lorg/matrix/androidsdk/crypto/interfaces/CryptoRoomMember;", "getJoinedMembersAsyncCrypto", "shouldEncryptForInvitedMembers", "", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: CryptoRoom.kt */
public interface CryptoRoom {
    void getActiveMembersAsyncCrypto(ApiCallback<List<CryptoRoomMember>> apiCallback);

    void getJoinedMembersAsyncCrypto(ApiCallback<List<CryptoRoomMember>> apiCallback);

    String getRoomId();

    CryptoRoomState getState();

    boolean shouldEncryptForInvitedMembers();
}

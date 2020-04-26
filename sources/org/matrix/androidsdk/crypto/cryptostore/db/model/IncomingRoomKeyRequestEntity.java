package org.matrix.androidsdk.crypto.cryptostore.db.model;

import io.realm.RealmObject;
import io.realm.internal.RealmObjectProxy;
import io.realm.org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface;
import kotlin.Metadata;
import kotlinx.coroutines.scheduling.WorkQueueKt;
import org.matrix.androidsdk.crypto.IncomingRoomKeyRequest;
import org.matrix.androidsdk.crypto.model.crypto.RoomKeyRequestBody;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0018\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0010\u0018\u00002\u00020\u0001BY\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\nJ\u0010\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eJ\u0006\u0010\u001f\u001a\u00020 R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\f\"\u0004\b\u0010\u0010\u000eR\u001c\u0010\u0007\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\f\"\u0004\b\u0012\u0010\u000eR\u001c\u0010\b\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\f\"\u0004\b\u0014\u0010\u000eR\u001c\u0010\t\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\f\"\u0004\b\u0016\u0010\u000eR\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\f\"\u0004\b\u0018\u0010\u000eR\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\f\"\u0004\b\u001a\u0010\u000e¨\u0006!"}, d2 = {"Lorg/matrix/androidsdk/crypto/cryptostore/db/model/IncomingRoomKeyRequestEntity;", "Lio/realm/RealmObject;", "requestId", "", "userId", "deviceId", "requestBodyAlgorithm", "requestBodyRoomId", "requestBodySenderKey", "requestBodySessionId", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getDeviceId", "()Ljava/lang/String;", "setDeviceId", "(Ljava/lang/String;)V", "getRequestBodyAlgorithm", "setRequestBodyAlgorithm", "getRequestBodyRoomId", "setRequestBodyRoomId", "getRequestBodySenderKey", "setRequestBodySenderKey", "getRequestBodySessionId", "setRequestBodySessionId", "getRequestId", "setRequestId", "getUserId", "setUserId", "putRequestBody", "", "requestBody", "Lorg/matrix/androidsdk/crypto/model/crypto/RoomKeyRequestBody;", "toIncomingRoomKeyRequest", "Lorg/matrix/androidsdk/crypto/IncomingRoomKeyRequest;", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: IncomingRoomKeyRequestEntity.kt */
public class IncomingRoomKeyRequestEntity extends RealmObject implements org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface {
    private String deviceId;
    private String requestBodyAlgorithm;
    private String requestBodyRoomId;
    private String requestBodySenderKey;
    private String requestBodySessionId;
    private String requestId;
    private String userId;

    public IncomingRoomKeyRequestEntity() {
        this(null, null, null, null, null, null, null, WorkQueueKt.MASK, null);
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
    }

    public String realmGet$deviceId() {
        return this.deviceId;
    }

    public String realmGet$requestBodyAlgorithm() {
        return this.requestBodyAlgorithm;
    }

    public String realmGet$requestBodyRoomId() {
        return this.requestBodyRoomId;
    }

    public String realmGet$requestBodySenderKey() {
        return this.requestBodySenderKey;
    }

    public String realmGet$requestBodySessionId() {
        return this.requestBodySessionId;
    }

    public String realmGet$requestId() {
        return this.requestId;
    }

    public String realmGet$userId() {
        return this.userId;
    }

    public void realmSet$deviceId(String str) {
        this.deviceId = str;
    }

    public void realmSet$requestBodyAlgorithm(String str) {
        this.requestBodyAlgorithm = str;
    }

    public void realmSet$requestBodyRoomId(String str) {
        this.requestBodyRoomId = str;
    }

    public void realmSet$requestBodySenderKey(String str) {
        this.requestBodySenderKey = str;
    }

    public void realmSet$requestBodySessionId(String str) {
        this.requestBodySessionId = str;
    }

    public void realmSet$requestId(String str) {
        this.requestId = str;
    }

    public void realmSet$userId(String str) {
        this.userId = str;
    }

    public /* synthetic */ IncomingRoomKeyRequestEntity(String str, String str2, String str3, String str4, String str5, String str6, String str7, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 1) != 0) {
            str = null;
        }
        if ((i & 2) != 0) {
            str2 = null;
        }
        String str8 = str2;
        if ((i & 4) != 0) {
            str3 = null;
        }
        String str9 = str3;
        if ((i & 8) != 0) {
            str4 = null;
        }
        String str10 = str4;
        if ((i & 16) != 0) {
            str5 = null;
        }
        String str11 = str5;
        if ((i & 32) != 0) {
            str6 = null;
        }
        String str12 = str6;
        if ((i & 64) != 0) {
            str7 = null;
        }
        this(str, str8, str9, str10, str11, str12, str7);
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
    }

    public final String getRequestId() {
        return realmGet$requestId();
    }

    public final void setRequestId(String str) {
        realmSet$requestId(str);
    }

    public final String getUserId() {
        return realmGet$userId();
    }

    public final void setUserId(String str) {
        realmSet$userId(str);
    }

    public final String getDeviceId() {
        return realmGet$deviceId();
    }

    public final void setDeviceId(String str) {
        realmSet$deviceId(str);
    }

    public final String getRequestBodyAlgorithm() {
        return realmGet$requestBodyAlgorithm();
    }

    public final void setRequestBodyAlgorithm(String str) {
        realmSet$requestBodyAlgorithm(str);
    }

    public final String getRequestBodyRoomId() {
        return realmGet$requestBodyRoomId();
    }

    public final void setRequestBodyRoomId(String str) {
        realmSet$requestBodyRoomId(str);
    }

    public final String getRequestBodySenderKey() {
        return realmGet$requestBodySenderKey();
    }

    public final void setRequestBodySenderKey(String str) {
        realmSet$requestBodySenderKey(str);
    }

    public final String getRequestBodySessionId() {
        return realmGet$requestBodySessionId();
    }

    public final void setRequestBodySessionId(String str) {
        realmSet$requestBodySessionId(str);
    }

    public IncomingRoomKeyRequestEntity(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
        realmSet$requestId(str);
        realmSet$userId(str2);
        realmSet$deviceId(str3);
        realmSet$requestBodyAlgorithm(str4);
        realmSet$requestBodyRoomId(str5);
        realmSet$requestBodySenderKey(str6);
        realmSet$requestBodySessionId(str7);
    }

    public final IncomingRoomKeyRequest toIncomingRoomKeyRequest() {
        IncomingRoomKeyRequest incomingRoomKeyRequest = new IncomingRoomKeyRequest();
        incomingRoomKeyRequest.mRequestId = realmGet$requestId();
        incomingRoomKeyRequest.mUserId = realmGet$userId();
        incomingRoomKeyRequest.mDeviceId = realmGet$deviceId();
        RoomKeyRequestBody roomKeyRequestBody = new RoomKeyRequestBody();
        roomKeyRequestBody.algorithm = realmGet$requestBodyAlgorithm();
        roomKeyRequestBody.roomId = realmGet$requestBodyRoomId();
        roomKeyRequestBody.senderKey = realmGet$requestBodySenderKey();
        roomKeyRequestBody.sessionId = realmGet$requestBodySessionId();
        incomingRoomKeyRequest.mRequestBody = roomKeyRequestBody;
        return incomingRoomKeyRequest;
    }

    public final void putRequestBody(RoomKeyRequestBody roomKeyRequestBody) {
        if (roomKeyRequestBody != null) {
            realmSet$requestBodyAlgorithm(roomKeyRequestBody.algorithm);
            realmSet$requestBodyRoomId(roomKeyRequestBody.roomId);
            realmSet$requestBodySenderKey(roomKeyRequestBody.senderKey);
            realmSet$requestBodySessionId(roomKeyRequestBody.sessionId);
        }
    }
}

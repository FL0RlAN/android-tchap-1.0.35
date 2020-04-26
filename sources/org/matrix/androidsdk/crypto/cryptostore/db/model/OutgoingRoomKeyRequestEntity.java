package org.matrix.androidsdk.crypto.cryptostore.db.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.internal.RealmObjectProxy;
import io.realm.org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import org.matrix.androidsdk.crypto.OutgoingRoomKeyRequest;
import org.matrix.androidsdk.crypto.OutgoingRoomKeyRequest.RequestState;
import org.matrix.androidsdk.crypto.cryptostore.db.HelperKt;
import org.matrix.androidsdk.crypto.model.crypto.RoomKeyRequestBody;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\u0016\n\u0002\u0010 \n\u0002\u0010%\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0010\u0018\u00002\u00020\u0001Bc\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b¢\u0006\u0002\u0010\fJ\u001c\u0010!\u001a\u0016\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030#\u0018\u00010\"H\u0002J\"\u0010$\u001a\u00020%2\u001a\u0010&\u001a\u0016\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030#\u0018\u00010\"J\u0010\u0010'\u001a\u00020%2\b\u0010(\u001a\u0004\u0018\u00010)J\u0006\u0010*\u001a\u00020+R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u000e\"\u0004\b\u0012\u0010\u0010R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u000e\"\u0004\b\u0014\u0010\u0010R\u001c\u0010\u0007\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u000e\"\u0004\b\u0016\u0010\u0010R\u001c\u0010\b\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u000e\"\u0004\b\u0018\u0010\u0010R\u001c\u0010\t\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u000e\"\u0004\b\u001a\u0010\u0010R \u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u000e\"\u0004\b\u001c\u0010\u0010R\u001a\u0010\n\u001a\u00020\u000bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 ¨\u0006,"}, d2 = {"Lorg/matrix/androidsdk/crypto/cryptostore/db/model/OutgoingRoomKeyRequestEntity;", "Lio/realm/RealmObject;", "requestId", "", "cancellationTxnId", "recipientsData", "requestBodyAlgorithm", "requestBodyRoomId", "requestBodySenderKey", "requestBodySessionId", "state", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V", "getCancellationTxnId", "()Ljava/lang/String;", "setCancellationTxnId", "(Ljava/lang/String;)V", "getRecipientsData", "setRecipientsData", "getRequestBodyAlgorithm", "setRequestBodyAlgorithm", "getRequestBodyRoomId", "setRequestBodyRoomId", "getRequestBodySenderKey", "setRequestBodySenderKey", "getRequestBodySessionId", "setRequestBodySessionId", "getRequestId", "setRequestId", "getState", "()I", "setState", "(I)V", "getRecipients", "", "", "putRecipients", "", "recipients", "putRequestBody", "requestBody", "Lorg/matrix/androidsdk/crypto/model/crypto/RoomKeyRequestBody;", "toOutgoingRoomKeyRequest", "Lorg/matrix/androidsdk/crypto/OutgoingRoomKeyRequest;", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: OutgoingRoomKeyRequestEntity.kt */
public class OutgoingRoomKeyRequestEntity extends RealmObject implements org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface {
    private String cancellationTxnId;
    private String recipientsData;
    private String requestBodyAlgorithm;
    private String requestBodyRoomId;
    private String requestBodySenderKey;
    private String requestBodySessionId;
    @PrimaryKey
    private String requestId;
    private int state;

    public OutgoingRoomKeyRequestEntity() {
        this(null, null, null, null, null, null, null, 0, 255, null);
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
    }

    public String realmGet$cancellationTxnId() {
        return this.cancellationTxnId;
    }

    public String realmGet$recipientsData() {
        return this.recipientsData;
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

    public int realmGet$state() {
        return this.state;
    }

    public void realmSet$cancellationTxnId(String str) {
        this.cancellationTxnId = str;
    }

    public void realmSet$recipientsData(String str) {
        this.recipientsData = str;
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

    public void realmSet$state(int i) {
        this.state = i;
    }

    public /* synthetic */ OutgoingRoomKeyRequestEntity(String str, String str2, String str3, String str4, String str5, String str6, String str7, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        int i3 = i2;
        String str8 = (i3 & 1) != 0 ? null : str;
        this(str8, (i3 & 2) != 0 ? null : str2, (i3 & 4) != 0 ? null : str3, (i3 & 8) != 0 ? null : str4, (i3 & 16) != 0 ? null : str5, (i3 & 32) != 0 ? null : str6, (i3 & 64) != 0 ? null : str7, (i3 & 128) != 0 ? 0 : i);
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

    public final String getCancellationTxnId() {
        return realmGet$cancellationTxnId();
    }

    public final void setCancellationTxnId(String str) {
        realmSet$cancellationTxnId(str);
    }

    public final String getRecipientsData() {
        return realmGet$recipientsData();
    }

    public final void setRecipientsData(String str) {
        realmSet$recipientsData(str);
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

    public final int getState() {
        return realmGet$state();
    }

    public final void setState(int i) {
        realmSet$state(i);
    }

    public OutgoingRoomKeyRequestEntity(String str, String str2, String str3, String str4, String str5, String str6, String str7, int i) {
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
        realmSet$requestId(str);
        realmSet$cancellationTxnId(str2);
        realmSet$recipientsData(str3);
        realmSet$requestBodyAlgorithm(str4);
        realmSet$requestBodyRoomId(str5);
        realmSet$requestBodySenderKey(str6);
        realmSet$requestBodySessionId(str7);
        realmSet$state(i);
    }

    public final OutgoingRoomKeyRequest toOutgoingRoomKeyRequest() {
        RoomKeyRequestBody roomKeyRequestBody = new RoomKeyRequestBody();
        roomKeyRequestBody.algorithm = realmGet$requestBodyAlgorithm();
        roomKeyRequestBody.roomId = realmGet$requestBodyRoomId();
        roomKeyRequestBody.senderKey = realmGet$requestBodySenderKey();
        roomKeyRequestBody.sessionId = realmGet$requestBodySessionId();
        OutgoingRoomKeyRequest outgoingRoomKeyRequest = new OutgoingRoomKeyRequest(roomKeyRequestBody, getRecipients(), realmGet$requestId(), RequestState.from(realmGet$state()));
        outgoingRoomKeyRequest.mCancellationTxnId = realmGet$cancellationTxnId();
        return outgoingRoomKeyRequest;
    }

    private final List<Map<String, String>> getRecipients() {
        return (List) HelperKt.deserializeFromRealm(realmGet$recipientsData());
    }

    public final void putRecipients(List<? extends Map<String, String>> list) {
        realmSet$recipientsData(HelperKt.serializeForRealm(list));
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

package org.matrix.androidsdk.crypto.interfaces;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Map;
import kotlin.Metadata;
import org.matrix.androidsdk.crypto.MXCryptoError;
import org.matrix.androidsdk.crypto.MXEventDecryptionResult;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedEventContent;
import org.matrix.androidsdk.crypto.model.crypto.ForwardedRoomKeyContent;
import org.matrix.androidsdk.crypto.model.crypto.OlmEventContent;
import org.matrix.androidsdk.crypto.model.crypto.RoomKeyContent;
import org.matrix.androidsdk.crypto.rest.model.crypto.RoomKeyShare;
import org.matrix.androidsdk.crypto.rest.model.crypto.RoomKeyShareRequest;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000v\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\r\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u0000 52\u00020\u0001:\u00015J\b\u0010\u001a\u001a\u00020\u001bH&J\b\u0010\u001c\u001a\u00020\fH&J\b\u0010\u001d\u001a\u00020\fH&J\b\u0010\u001e\u001a\u00020\fH&J\b\u0010\u001f\u001a\u00020\fH&J\b\u0010 \u001a\u00020!H&J\u0010\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%H&J\u0010\u0010&\u001a\u00020#2\u0006\u0010'\u001a\u00020(H&J\b\u0010)\u001a\u00020*H&J\b\u0010+\u001a\u00020,H&J\b\u0010-\u001a\u00020.H&J\b\u0010/\u001a\u000200H&J\b\u00101\u001a\u000202H&J\b\u00103\u001a\u000204H&R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0007X¦\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\tR\u001e\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\f0\u000bX¦\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u0012\u0010\u000f\u001a\u00020\fX¦\u0004¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0012\u0010\u0012\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0013\u0010\u0005R\u0012\u0010\u0014\u001a\u00020\u0015X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017R\u0012\u0010\u0018\u001a\u00020\fX¦\u0004¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u0011¨\u00066"}, d2 = {"Lorg/matrix/androidsdk/crypto/interfaces/CryptoEvent;", "", "content", "Lcom/google/gson/JsonElement;", "getContent", "()Lcom/google/gson/JsonElement;", "contentAsJsonObject", "Lcom/google/gson/JsonObject;", "getContentAsJsonObject", "()Lcom/google/gson/JsonObject;", "keysClaimed", "", "", "getKeysClaimed", "()Ljava/util/Map;", "senderKey", "getSenderKey", "()Ljava/lang/String;", "wireContent", "getWireContent", "wireEventContent", "Lorg/matrix/androidsdk/crypto/interfaces/CryptoEventContent;", "getWireEventContent", "()Lorg/matrix/androidsdk/crypto/interfaces/CryptoEventContent;", "wireType", "getWireType", "getAge", "", "getEventId", "getRoomId", "getSender", "getStateKey", "getType", "", "setClearData", "", "decryptionResult", "Lorg/matrix/androidsdk/crypto/MXEventDecryptionResult;", "setCryptoError", "cryptoError", "Lorg/matrix/androidsdk/crypto/MXCryptoError;", "toEncryptedEventContent", "Lorg/matrix/androidsdk/crypto/model/crypto/EncryptedEventContent;", "toForwardedRoomKeyContent", "Lorg/matrix/androidsdk/crypto/model/crypto/ForwardedRoomKeyContent;", "toOlmEventContent", "Lorg/matrix/androidsdk/crypto/model/crypto/OlmEventContent;", "toRoomKeyContent", "Lorg/matrix/androidsdk/crypto/model/crypto/RoomKeyContent;", "toRoomKeyShare", "Lorg/matrix/androidsdk/crypto/rest/model/crypto/RoomKeyShare;", "toRoomKeyShareRequest", "Lorg/matrix/androidsdk/crypto/rest/model/crypto/RoomKeyShareRequest;", "Companion", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: CryptoEvent.kt */
public interface CryptoEvent {
    public static final Companion Companion = Companion.$$INSTANCE;
    public static final String EVENT_TYPE_FORWARDED_ROOM_KEY = "m.forwarded_room_key";
    public static final String EVENT_TYPE_KEY_VERIFICATION_ACCEPT = "m.key.verification.accept";
    public static final String EVENT_TYPE_KEY_VERIFICATION_CANCEL = "m.key.verification.cancel";
    public static final String EVENT_TYPE_KEY_VERIFICATION_KEY = "m.key.verification.key";
    public static final String EVENT_TYPE_KEY_VERIFICATION_MAC = "m.key.verification.mac";
    public static final String EVENT_TYPE_KEY_VERIFICATION_START = "m.key.verification.start";
    public static final String EVENT_TYPE_MESSAGE_ENCRYPTED = "m.room.encrypted";
    public static final String EVENT_TYPE_MESSAGE_ENCRYPTION = "m.room.encryption";
    public static final String EVENT_TYPE_ROOM_KEY = "m.room_key";
    public static final String EVENT_TYPE_ROOM_KEY_REQUEST = "m.room_key_request";
    public static final String EVENT_TYPE_STATE_ROOM_MEMBER = "m.room.member";

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000b\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lorg/matrix/androidsdk/crypto/interfaces/CryptoEvent$Companion;", "", "()V", "EVENT_TYPE_FORWARDED_ROOM_KEY", "", "EVENT_TYPE_KEY_VERIFICATION_ACCEPT", "EVENT_TYPE_KEY_VERIFICATION_CANCEL", "EVENT_TYPE_KEY_VERIFICATION_KEY", "EVENT_TYPE_KEY_VERIFICATION_MAC", "EVENT_TYPE_KEY_VERIFICATION_START", "EVENT_TYPE_MESSAGE_ENCRYPTED", "EVENT_TYPE_MESSAGE_ENCRYPTION", "EVENT_TYPE_ROOM_KEY", "EVENT_TYPE_ROOM_KEY_REQUEST", "EVENT_TYPE_STATE_ROOM_MEMBER", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: CryptoEvent.kt */
    public static final class Companion {
        static final /* synthetic */ Companion $$INSTANCE = new Companion();
        public static final String EVENT_TYPE_FORWARDED_ROOM_KEY = "m.forwarded_room_key";
        public static final String EVENT_TYPE_KEY_VERIFICATION_ACCEPT = "m.key.verification.accept";
        public static final String EVENT_TYPE_KEY_VERIFICATION_CANCEL = "m.key.verification.cancel";
        public static final String EVENT_TYPE_KEY_VERIFICATION_KEY = "m.key.verification.key";
        public static final String EVENT_TYPE_KEY_VERIFICATION_MAC = "m.key.verification.mac";
        public static final String EVENT_TYPE_KEY_VERIFICATION_START = "m.key.verification.start";
        public static final String EVENT_TYPE_MESSAGE_ENCRYPTED = "m.room.encrypted";
        public static final String EVENT_TYPE_MESSAGE_ENCRYPTION = "m.room.encryption";
        public static final String EVENT_TYPE_ROOM_KEY = "m.room_key";
        public static final String EVENT_TYPE_ROOM_KEY_REQUEST = "m.room_key_request";
        public static final String EVENT_TYPE_STATE_ROOM_MEMBER = "m.room.member";

        private Companion() {
        }
    }

    long getAge();

    JsonElement getContent();

    JsonObject getContentAsJsonObject();

    String getEventId();

    Map<String, String> getKeysClaimed();

    String getRoomId();

    String getSender();

    String getSenderKey();

    String getStateKey();

    CharSequence getType();

    JsonElement getWireContent();

    CryptoEventContent getWireEventContent();

    String getWireType();

    void setClearData(MXEventDecryptionResult mXEventDecryptionResult);

    void setCryptoError(MXCryptoError mXCryptoError);

    EncryptedEventContent toEncryptedEventContent();

    ForwardedRoomKeyContent toForwardedRoomKeyContent();

    OlmEventContent toOlmEventContent();

    RoomKeyContent toRoomKeyContent();

    RoomKeyShare toRoomKeyShare();

    RoomKeyShareRequest toRoomKeyShareRequest();
}

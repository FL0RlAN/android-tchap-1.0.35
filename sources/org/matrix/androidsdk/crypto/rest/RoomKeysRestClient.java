package org.matrix.androidsdk.crypto.rest;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.crypto.api.RoomKeysApi;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntityFields;
import org.matrix.androidsdk.crypto.model.keys.CreateKeysBackupVersionBody;
import org.matrix.androidsdk.crypto.model.keys.KeyBackupData;
import org.matrix.androidsdk.crypto.model.keys.KeysBackupData;
import org.matrix.androidsdk.crypto.model.keys.KeysVersion;
import org.matrix.androidsdk.crypto.model.keys.KeysVersionResult;
import org.matrix.androidsdk.crypto.model.keys.RoomKeysBackupData;
import org.matrix.androidsdk.crypto.model.rest.keys.BackupKeysResult;
import org.matrix.androidsdk.crypto.model.rest.keys.UpdateKeysBackupVersionBody;
import org.matrix.androidsdk.rest.model.terms.TermsResponse;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J4\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fJ$\u0010\u0011\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\u00132\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fJ,\u0010\u0014\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\t2\u0006\u0010\u0015\u001a\u00020\u00162\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fJ\u001c\u0010\u0017\u001a\u00020\u00072\u0006\u0010\u0018\u001a\u00020\u00192\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u001a0\u000fJ\u001c\u0010\u001b\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\t2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u001c0\u000fJ\u001c\u0010\u001d\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\t2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fJ,\u0010\u001e\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\t2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fJ$\u0010\u001f\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\t2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fJ\u001c\u0010 \u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\t2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00130\u000fJ\u0014\u0010!\u001a\u00020\u00072\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\"0\u000fJ\u001c\u0010#\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\t2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\"0\u000fJ,\u0010$\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\t2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\r0\u000fJ$\u0010%\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\t2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00160\u000fJ$\u0010&\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\t2\u0006\u0010'\u001a\u00020(2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u001c0\u000f¨\u0006)"}, d2 = {"Lorg/matrix/androidsdk/crypto/rest/RoomKeysRestClient;", "Lorg/matrix/androidsdk/RestClient;", "Lorg/matrix/androidsdk/crypto/api/RoomKeysApi;", "homeServerConnectionConfig", "Lorg/matrix/androidsdk/HomeServerConnectionConfig;", "(Lorg/matrix/androidsdk/HomeServerConnectionConfig;)V", "backupKey", "", "roomId", "", "sessionId", "version", "keyBackupData", "Lorg/matrix/androidsdk/crypto/model/keys/KeyBackupData;", "callback", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "Lorg/matrix/androidsdk/crypto/model/rest/keys/BackupKeysResult;", "backupKeys", "keysBackupData", "Lorg/matrix/androidsdk/crypto/model/keys/KeysBackupData;", "backupRoomKeys", "roomKeysBackupData", "Lorg/matrix/androidsdk/crypto/model/keys/RoomKeysBackupData;", "createKeysBackupVersion", "createKeysBackupVersionBody", "Lorg/matrix/androidsdk/crypto/model/keys/CreateKeysBackupVersionBody;", "Lorg/matrix/androidsdk/crypto/model/keys/KeysVersion;", "deleteBackup", "Ljava/lang/Void;", "deleteKeys", "deleteRoomKey", "deleteRoomKeys", "getKeys", "getKeysBackupLastVersion", "Lorg/matrix/androidsdk/crypto/model/keys/KeysVersionResult;", "getKeysBackupVersion", "getRoomKey", "getRoomKeys", "updateKeysBackupVersion", "updateKeysBackupVersionBody", "Lorg/matrix/androidsdk/crypto/model/rest/keys/UpdateKeysBackupVersionBody;", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: RoomKeysRestClient.kt */
public final class RoomKeysRestClient extends RestClient<RoomKeysApi> {
    public RoomKeysRestClient(HomeServerConnectionConfig homeServerConnectionConfig) {
        Intrinsics.checkParameterIsNotNull(homeServerConnectionConfig, "homeServerConnectionConfig");
        super(homeServerConnectionConfig, RoomKeysApi.class, RestClient.URI_API_PREFIX_PATH_R0);
    }

    public final void getKeysBackupLastVersion(ApiCallback<KeysVersionResult> apiCallback) {
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        ((RoomKeysApi) this.mApi).getKeysBackupLastVersion().enqueue(new CryptoRestAdapterCallback("getKeysBackupLastVersion", apiCallback, null));
    }

    public final void getKeysBackupVersion(String str, ApiCallback<KeysVersionResult> apiCallback) {
        Intrinsics.checkParameterIsNotNull(str, TermsResponse.VERSION);
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        ((RoomKeysApi) this.mApi).getKeysBackupVersion(str).enqueue(new CryptoRestAdapterCallback("getKeysBackupVersion", apiCallback, null));
    }

    public final void createKeysBackupVersion(CreateKeysBackupVersionBody createKeysBackupVersionBody, ApiCallback<KeysVersion> apiCallback) {
        Intrinsics.checkParameterIsNotNull(createKeysBackupVersionBody, "createKeysBackupVersionBody");
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        ((RoomKeysApi) this.mApi).createKeysBackupVersion(createKeysBackupVersionBody).enqueue(new CryptoRestAdapterCallback("createKeysBackupVersion", apiCallback, null));
    }

    public final void updateKeysBackupVersion(String str, UpdateKeysBackupVersionBody updateKeysBackupVersionBody, ApiCallback<Void> apiCallback) {
        Intrinsics.checkParameterIsNotNull(str, TermsResponse.VERSION);
        Intrinsics.checkParameterIsNotNull(updateKeysBackupVersionBody, "updateKeysBackupVersionBody");
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        ((RoomKeysApi) this.mApi).updateKeysBackupVersion(str, updateKeysBackupVersionBody).enqueue(new CryptoRestAdapterCallback("updateKeysBackupVersion", apiCallback, null));
    }

    public final void backupKey(String str, String str2, String str3, KeyBackupData keyBackupData, ApiCallback<BackupKeysResult> apiCallback) {
        Intrinsics.checkParameterIsNotNull(str, CryptoRoomEntityFields.ROOM_ID);
        Intrinsics.checkParameterIsNotNull(str2, "sessionId");
        Intrinsics.checkParameterIsNotNull(str3, TermsResponse.VERSION);
        Intrinsics.checkParameterIsNotNull(keyBackupData, "keyBackupData");
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        ((RoomKeysApi) this.mApi).storeRoomSessionData(str, str2, str3, keyBackupData).enqueue(new CryptoRestAdapterCallback("backupKey", apiCallback, null));
    }

    public final void backupRoomKeys(String str, String str2, RoomKeysBackupData roomKeysBackupData, ApiCallback<BackupKeysResult> apiCallback) {
        Intrinsics.checkParameterIsNotNull(str, CryptoRoomEntityFields.ROOM_ID);
        Intrinsics.checkParameterIsNotNull(str2, TermsResponse.VERSION);
        Intrinsics.checkParameterIsNotNull(roomKeysBackupData, "roomKeysBackupData");
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        ((RoomKeysApi) this.mApi).storeRoomSessionsData(str, str2, roomKeysBackupData).enqueue(new CryptoRestAdapterCallback("backupRoomKeys", apiCallback, null));
    }

    public final void backupKeys(String str, KeysBackupData keysBackupData, ApiCallback<BackupKeysResult> apiCallback) {
        Intrinsics.checkParameterIsNotNull(str, TermsResponse.VERSION);
        Intrinsics.checkParameterIsNotNull(keysBackupData, "keysBackupData");
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        ((RoomKeysApi) this.mApi).storeSessionsData(str, keysBackupData).enqueue(new CryptoRestAdapterCallback("backupKeys", apiCallback, null));
    }

    public final void getRoomKey(String str, String str2, String str3, ApiCallback<KeyBackupData> apiCallback) {
        Intrinsics.checkParameterIsNotNull(str, CryptoRoomEntityFields.ROOM_ID);
        Intrinsics.checkParameterIsNotNull(str2, "sessionId");
        Intrinsics.checkParameterIsNotNull(str3, TermsResponse.VERSION);
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        ((RoomKeysApi) this.mApi).getRoomSessionData(str, str2, str3).enqueue(new CryptoRestAdapterCallback("getRoomKey", apiCallback, null));
    }

    public final void getRoomKeys(String str, String str2, ApiCallback<RoomKeysBackupData> apiCallback) {
        Intrinsics.checkParameterIsNotNull(str, CryptoRoomEntityFields.ROOM_ID);
        Intrinsics.checkParameterIsNotNull(str2, TermsResponse.VERSION);
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        ((RoomKeysApi) this.mApi).getRoomSessionsData(str, str2).enqueue(new CryptoRestAdapterCallback("getRoomKeys", apiCallback, null));
    }

    public final void getKeys(String str, ApiCallback<KeysBackupData> apiCallback) {
        Intrinsics.checkParameterIsNotNull(str, TermsResponse.VERSION);
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        ((RoomKeysApi) this.mApi).getSessionsData(str).enqueue(new CryptoRestAdapterCallback("getKeys", apiCallback, null));
    }

    public final void deleteRoomKey(String str, String str2, String str3, ApiCallback<BackupKeysResult> apiCallback) {
        Intrinsics.checkParameterIsNotNull(str, CryptoRoomEntityFields.ROOM_ID);
        Intrinsics.checkParameterIsNotNull(str2, "sessionId");
        Intrinsics.checkParameterIsNotNull(str3, TermsResponse.VERSION);
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        ((RoomKeysApi) this.mApi).deleteRoomSessionData(str, str2, str3).enqueue(new CryptoRestAdapterCallback("deleteRoomKey", apiCallback, null));
    }

    public final void deleteRoomKeys(String str, String str2, ApiCallback<BackupKeysResult> apiCallback) {
        Intrinsics.checkParameterIsNotNull(str, CryptoRoomEntityFields.ROOM_ID);
        Intrinsics.checkParameterIsNotNull(str2, TermsResponse.VERSION);
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        ((RoomKeysApi) this.mApi).deleteRoomSessionsData(str, str2).enqueue(new CryptoRestAdapterCallback("deleteRoomKeys", apiCallback, null));
    }

    public final void deleteKeys(String str, ApiCallback<BackupKeysResult> apiCallback) {
        Intrinsics.checkParameterIsNotNull(str, TermsResponse.VERSION);
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        ((RoomKeysApi) this.mApi).deleteSessionsData(str).enqueue(new CryptoRestAdapterCallback("deleteKeys", apiCallback, null));
    }

    public final void deleteBackup(String str, ApiCallback<Void> apiCallback) {
        Intrinsics.checkParameterIsNotNull(str, TermsResponse.VERSION);
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        ((RoomKeysApi) this.mApi).deleteBackup(str).enqueue(new CryptoRestAdapterCallback("deleteBackup", apiCallback, null));
    }
}

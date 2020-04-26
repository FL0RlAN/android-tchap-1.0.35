package org.matrix.androidsdk.crypto.keysbackup;

import java.util.HashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.ApiFailureCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.crypto.model.keys.KeyBackupData;
import org.matrix.androidsdk.crypto.model.keys.KeysBackupData;
import org.matrix.androidsdk.crypto.model.keys.RoomKeysBackupData;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0002H\u0016¨\u0006\u0006"}, d2 = {"org/matrix/androidsdk/crypto/keysbackup/KeysBackup$getKeys$1", "Lorg/matrix/androidsdk/core/callback/SimpleApiCallback;", "Lorg/matrix/androidsdk/crypto/model/keys/KeyBackupData;", "onSuccess", "", "info", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
public final class KeysBackup$getKeys$1 extends SimpleApiCallback<KeyBackupData> {
    final /* synthetic */ ApiCallback $callback;
    final /* synthetic */ String $roomId;
    final /* synthetic */ String $sessionId;

    KeysBackup$getKeys$1(String str, String str2, ApiCallback apiCallback, ApiFailureCallback apiFailureCallback) {
        this.$sessionId = str;
        this.$roomId = str2;
        this.$callback = apiCallback;
        super(apiFailureCallback);
    }

    public void onSuccess(KeyBackupData keyBackupData) {
        Intrinsics.checkParameterIsNotNull(keyBackupData, "info");
        KeysBackupData keysBackupData = new KeysBackupData();
        keysBackupData.setRoomIdToRoomKeysBackupData(new HashMap());
        RoomKeysBackupData roomKeysBackupData = new RoomKeysBackupData();
        roomKeysBackupData.setSessionIdToKeyBackupData(new HashMap());
        roomKeysBackupData.getSessionIdToKeyBackupData().put(this.$sessionId, keyBackupData);
        keysBackupData.getRoomIdToRoomKeysBackupData().put(this.$roomId, roomKeysBackupData);
        this.$callback.onSuccess(keysBackupData);
    }
}

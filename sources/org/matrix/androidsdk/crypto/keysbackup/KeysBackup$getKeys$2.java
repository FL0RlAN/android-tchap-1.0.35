package org.matrix.androidsdk.crypto.keysbackup;

import java.util.HashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.ApiFailureCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.crypto.model.keys.KeysBackupData;
import org.matrix.androidsdk.crypto.model.keys.RoomKeysBackupData;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0002H\u0016¨\u0006\u0006"}, d2 = {"org/matrix/androidsdk/crypto/keysbackup/KeysBackup$getKeys$2", "Lorg/matrix/androidsdk/core/callback/SimpleApiCallback;", "Lorg/matrix/androidsdk/crypto/model/keys/RoomKeysBackupData;", "onSuccess", "", "info", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
public final class KeysBackup$getKeys$2 extends SimpleApiCallback<RoomKeysBackupData> {
    final /* synthetic */ ApiCallback $callback;
    final /* synthetic */ String $roomId;

    KeysBackup$getKeys$2(String str, ApiCallback apiCallback, ApiFailureCallback apiFailureCallback) {
        this.$roomId = str;
        this.$callback = apiCallback;
        super(apiFailureCallback);
    }

    public void onSuccess(RoomKeysBackupData roomKeysBackupData) {
        Intrinsics.checkParameterIsNotNull(roomKeysBackupData, "info");
        KeysBackupData keysBackupData = new KeysBackupData();
        keysBackupData.setRoomIdToRoomKeysBackupData(new HashMap());
        keysBackupData.getRoomIdToRoomKeysBackupData().put(this.$roomId, roomKeysBackupData);
        this.$callback.onSuccess(keysBackupData);
    }
}

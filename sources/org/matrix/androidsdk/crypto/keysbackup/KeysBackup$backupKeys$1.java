package org.matrix.androidsdk.crypto.keysbackup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.data.MXOlmInboundGroupSession2;
import org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupState;
import org.matrix.androidsdk.crypto.model.keys.KeyBackupData;
import org.matrix.androidsdk.crypto.model.keys.KeysBackupData;
import org.matrix.androidsdk.crypto.model.keys.KeysVersionResult;
import org.matrix.androidsdk.crypto.model.keys.RoomKeysBackupData;
import org.matrix.androidsdk.crypto.model.rest.keys.BackupKeysResult;
import org.matrix.androidsdk.crypto.rest.RoomKeysRestClient;
import org.matrix.olm.OlmException;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
final class KeysBackup$backupKeys$1 implements Runnable {
    final /* synthetic */ List $sessions;
    final /* synthetic */ KeysBackup this$0;

    KeysBackup$backupKeys$1(KeysBackup keysBackup, List list) {
        this.this$0 = keysBackup;
        this.$sessions = list;
    }

    public final void run() {
        Log.d(KeysBackup.LOG_TAG, "backupKeys: 2 - Encrypting keys");
        KeysBackupData keysBackupData = new KeysBackupData();
        keysBackupData.setRoomIdToRoomKeysBackupData(new HashMap());
        for (MXOlmInboundGroupSession2 mXOlmInboundGroupSession2 : this.$sessions) {
            KeysBackup keysBackup = this.this$0;
            Intrinsics.checkExpressionValueIsNotNull(mXOlmInboundGroupSession2, "session");
            KeyBackupData encryptGroupSession = keysBackup.encryptGroupSession(mXOlmInboundGroupSession2);
            if (keysBackupData.getRoomIdToRoomKeysBackupData().get(mXOlmInboundGroupSession2.mRoomId) == null) {
                RoomKeysBackupData roomKeysBackupData = new RoomKeysBackupData();
                roomKeysBackupData.setSessionIdToKeyBackupData(new HashMap());
                Map roomIdToRoomKeysBackupData = keysBackupData.getRoomIdToRoomKeysBackupData();
                String str = mXOlmInboundGroupSession2.mRoomId;
                Intrinsics.checkExpressionValueIsNotNull(str, "session.mRoomId");
                roomIdToRoomKeysBackupData.put(str, roomKeysBackupData);
            }
            try {
                Object obj = keysBackupData.getRoomIdToRoomKeysBackupData().get(mXOlmInboundGroupSession2.mRoomId);
                if (obj == null) {
                    Intrinsics.throwNpe();
                }
                Map sessionIdToKeyBackupData = ((RoomKeysBackupData) obj).getSessionIdToKeyBackupData();
                String sessionIdentifier = mXOlmInboundGroupSession2.mSession.sessionIdentifier();
                Intrinsics.checkExpressionValueIsNotNull(sessionIdentifier, "session.mSession.sessionIdentifier()");
                sessionIdToKeyBackupData.put(sessionIdentifier, encryptGroupSession);
            } catch (OlmException e) {
                Log.e(KeysBackup.LOG_TAG, "OlmException", e);
            }
        }
        Log.d(KeysBackup.LOG_TAG, "backupKeys: 4 - Sending request");
        RoomKeysRestClient access$getMRoomKeysRestClient$p = this.this$0.mRoomKeysRestClient;
        KeysVersionResult mKeysBackupVersion = this.this$0.getMKeysBackupVersion();
        if (mKeysBackupVersion == null) {
            Intrinsics.throwNpe();
        }
        String version = mKeysBackupVersion.getVersion();
        if (version == null) {
            Intrinsics.throwNpe();
        }
        access$getMRoomKeysRestClient$p.backupKeys(version, keysBackupData, new ApiCallback<BackupKeysResult>(this) {
            final /* synthetic */ KeysBackup$backupKeys$1 this$0;

            {
                this.this$0 = r1;
            }

            public void onNetworkError(Exception exc) {
                Intrinsics.checkParameterIsNotNull(exc, "e");
                this.this$0.this$0.mCrypto.getUIHandler().post(new KeysBackup$backupKeys$1$1$onNetworkError$1(this, exc));
            }

            /* access modifiers changed from: private */
            public final void onError() {
                Log.e(KeysBackup.LOG_TAG, "backupKeys: backupKeys failed.");
                this.this$0.this$0.mKeysBackupStateManager.setState(KeysBackupState.ReadyToBackUp);
                this.this$0.this$0.maybeBackupKeys();
            }

            public void onMatrixError(MatrixError matrixError) {
                Intrinsics.checkParameterIsNotNull(matrixError, "e");
                this.this$0.this$0.mCrypto.getUIHandler().post(new KeysBackup$backupKeys$1$1$onMatrixError$1(this, matrixError));
            }

            public void onUnexpectedError(Exception exc) {
                Intrinsics.checkParameterIsNotNull(exc, "e");
                this.this$0.this$0.mCrypto.getUIHandler().post(new KeysBackup$backupKeys$1$1$onUnexpectedError$1(this, exc));
            }

            public void onSuccess(BackupKeysResult backupKeysResult) {
                Intrinsics.checkParameterIsNotNull(backupKeysResult, "info");
                this.this$0.this$0.mCrypto.getUIHandler().post(new KeysBackup$backupKeys$1$1$onSuccess$1(this, backupKeysResult));
            }
        });
    }
}

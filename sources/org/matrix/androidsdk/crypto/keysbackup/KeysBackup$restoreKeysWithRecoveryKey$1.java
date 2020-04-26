package org.matrix.androidsdk.crypto.keysbackup;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.listeners.StepProgressListener;
import org.matrix.androidsdk.core.listeners.StepProgressListener.Step.DownloadingKey;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.MegolmSessionData;
import org.matrix.androidsdk.crypto.model.keys.KeyBackupData;
import org.matrix.androidsdk.crypto.model.keys.KeysBackupData;
import org.matrix.androidsdk.crypto.model.keys.KeysVersionResult;
import org.matrix.androidsdk.crypto.model.keys.RoomKeysBackupData;
import org.matrix.olm.OlmPkDecryption;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
final class KeysBackup$restoreKeysWithRecoveryKey$1 implements Runnable {
    final /* synthetic */ ApiCallback $callback;
    final /* synthetic */ KeysVersionResult $keysVersionResult;
    final /* synthetic */ String $recoveryKey;
    final /* synthetic */ String $roomId;
    final /* synthetic */ String $sessionId;
    final /* synthetic */ StepProgressListener $stepProgressListener;
    final /* synthetic */ KeysBackup this$0;

    KeysBackup$restoreKeysWithRecoveryKey$1(KeysBackup keysBackup, String str, KeysVersionResult keysVersionResult, ApiCallback apiCallback, StepProgressListener stepProgressListener, String str2, String str3) {
        this.this$0 = keysBackup;
        this.$recoveryKey = str;
        this.$keysVersionResult = keysVersionResult;
        this.$callback = apiCallback;
        this.$stepProgressListener = stepProgressListener;
        this.$sessionId = str2;
        this.$roomId = str3;
    }

    public final void run() {
        if (!this.this$0.isValidRecoveryKeyForKeysBackupVersion(this.$recoveryKey, this.$keysVersionResult)) {
            Log.e(KeysBackup.LOG_TAG, "restoreKeysWithRecoveryKey: Invalid recovery key for this keys version");
            this.this$0.mCrypto.getUIHandler().post(new Runnable(this) {
                final /* synthetic */ KeysBackup$restoreKeysWithRecoveryKey$1 this$0;

                {
                    this.this$0 = r1;
                }

                public final void run() {
                    this.this$0.$callback.onUnexpectedError(new InvalidParameterException("Invalid recovery key"));
                }
            });
            return;
        }
        final OlmPkDecryption pkDecryptionFromRecoveryKey = this.this$0.pkDecryptionFromRecoveryKey(this.$recoveryKey);
        if (pkDecryptionFromRecoveryKey == null) {
            Log.e(KeysBackup.LOG_TAG, "restoreKeysWithRecoveryKey: Invalid recovery key. Error");
            this.this$0.mCrypto.getUIHandler().post(new Runnable(this) {
                final /* synthetic */ KeysBackup$restoreKeysWithRecoveryKey$1 this$0;

                {
                    this.this$0 = r1;
                }

                public final void run() {
                    this.this$0.$callback.onUnexpectedError(new InvalidParameterException("Invalid recovery key"));
                }
            });
            return;
        }
        if (this.$stepProgressListener != null) {
            this.this$0.mCrypto.getUIHandler().post(new Runnable(this) {
                final /* synthetic */ KeysBackup$restoreKeysWithRecoveryKey$1 this$0;

                {
                    this.this$0 = r1;
                }

                public final void run() {
                    this.this$0.$stepProgressListener.onStepProgress(DownloadingKey.INSTANCE);
                }
            });
        }
        KeysBackup keysBackup = this.this$0;
        String str = this.$sessionId;
        String str2 = this.$roomId;
        String version = this.$keysVersionResult.getVersion();
        if (version == null) {
            Intrinsics.throwNpe();
        }
        keysBackup.getKeys(str, str2, version, new ApiCallback<KeysBackupData>(this) {
            final /* synthetic */ KeysBackup$restoreKeysWithRecoveryKey$1 this$0;

            {
                this.this$0 = r1;
            }

            public void onUnexpectedError(Exception exc) {
                Intrinsics.checkParameterIsNotNull(exc, "e");
                this.this$0.this$0.mCrypto.getUIHandler().post(new KeysBackup$restoreKeysWithRecoveryKey$1$4$onUnexpectedError$1(this, exc));
            }

            public void onNetworkError(Exception exc) {
                Intrinsics.checkParameterIsNotNull(exc, "e");
                this.this$0.this$0.mCrypto.getUIHandler().post(new KeysBackup$restoreKeysWithRecoveryKey$1$4$onNetworkError$1(this, exc));
            }

            public void onMatrixError(MatrixError matrixError) {
                Intrinsics.checkParameterIsNotNull(matrixError, "e");
                this.this$0.this$0.mCrypto.getUIHandler().post(new KeysBackup$restoreKeysWithRecoveryKey$1$4$onMatrixError$1(this, matrixError));
            }

            public void onSuccess(KeysBackupData keysBackupData) {
                Intrinsics.checkParameterIsNotNull(keysBackupData, "keysBackupData");
                ArrayList arrayList = new ArrayList();
                int i = 0;
                for (String str : keysBackupData.getRoomIdToRoomKeysBackupData().keySet()) {
                    Object obj = keysBackupData.getRoomIdToRoomKeysBackupData().get(str);
                    if (obj == null) {
                        Intrinsics.throwNpe();
                    }
                    for (String str2 : ((RoomKeysBackupData) obj).getSessionIdToKeyBackupData().keySet()) {
                        i++;
                        Object obj2 = keysBackupData.getRoomIdToRoomKeysBackupData().get(str);
                        if (obj2 == null) {
                            Intrinsics.throwNpe();
                        }
                        Object obj3 = ((RoomKeysBackupData) obj2).getSessionIdToKeyBackupData().get(str2);
                        if (obj3 == null) {
                            Intrinsics.throwNpe();
                        }
                        MegolmSessionData decryptKeyBackupData = this.this$0.this$0.decryptKeyBackupData((KeyBackupData) obj3, str2, str, pkDecryptionFromRecoveryKey);
                        if (decryptKeyBackupData != null) {
                            arrayList.add(decryptKeyBackupData);
                        }
                    }
                }
                String access$getLOG_TAG$cp = KeysBackup.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("restoreKeysWithRecoveryKey: Decrypted ");
                sb.append(arrayList.size());
                sb.append(" keys out of ");
                sb.append(i);
                sb.append(" from the backup store on the homeserver");
                Log.d(access$getLOG_TAG$cp, sb.toString());
                String version = this.this$0.$keysVersionResult.getVersion();
                KeysVersionResult mKeysBackupVersion = this.this$0.this$0.getMKeysBackupVersion();
                KeysBackup$restoreKeysWithRecoveryKey$1$4$onSuccess$progressListener$1 keysBackup$restoreKeysWithRecoveryKey$1$4$onSuccess$progressListener$1 = null;
                boolean z = !Intrinsics.areEqual((Object) version, mKeysBackupVersion != null ? mKeysBackupVersion.getVersion() : null);
                if (z) {
                    String access$getLOG_TAG$cp2 = KeysBackup.LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("restoreKeysWithRecoveryKey: Those keys will be backed up to backup version: ");
                    KeysVersionResult mKeysBackupVersion2 = this.this$0.this$0.getMKeysBackupVersion();
                    sb2.append(mKeysBackupVersion2 != null ? mKeysBackupVersion2.getVersion() : null);
                    Log.d(access$getLOG_TAG$cp2, sb2.toString());
                }
                if (this.this$0.$stepProgressListener != null) {
                    keysBackup$restoreKeysWithRecoveryKey$1$4$onSuccess$progressListener$1 = new KeysBackup$restoreKeysWithRecoveryKey$1$4$onSuccess$progressListener$1(this);
                }
                this.this$0.this$0.mCrypto.importMegolmSessionsData(arrayList, z, keysBackup$restoreKeysWithRecoveryKey$1$4$onSuccess$progressListener$1, this.this$0.$callback);
            }
        });
    }
}

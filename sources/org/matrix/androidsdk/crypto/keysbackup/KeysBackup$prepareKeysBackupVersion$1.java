package org.matrix.androidsdk.crypto.keysbackup;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.JsonUtility;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.SuccessErrorCallback;
import org.matrix.androidsdk.core.listeners.ProgressListener;
import org.matrix.androidsdk.crypto.CryptoConstantsKt;
import org.matrix.androidsdk.crypto.util.RecoveryKeyKt;
import org.matrix.olm.OlmException;
import org.matrix.olm.OlmPkDecryption;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
final class KeysBackup$prepareKeysBackupVersion$1 implements Runnable {
    final /* synthetic */ SuccessErrorCallback $callback;
    final /* synthetic */ String $password;
    final /* synthetic */ ProgressListener $progressListener;
    final /* synthetic */ KeysBackup this$0;

    KeysBackup$prepareKeysBackupVersion$1(KeysBackup keysBackup, String str, ProgressListener progressListener, SuccessErrorCallback successErrorCallback) {
        this.this$0 = keysBackup;
        this.$password = str;
        this.$progressListener = progressListener;
        this.$callback = successErrorCallback;
    }

    public final void run() {
        KeysBackup$prepareKeysBackupVersion$1$backgroundProgressListener$1 keysBackup$prepareKeysBackupVersion$1$backgroundProgressListener$1;
        try {
            OlmPkDecryption olmPkDecryption = new OlmPkDecryption();
            MegolmBackupAuthData megolmBackupAuthData = new MegolmBackupAuthData(null, null, null, null, 15, null);
            if (this.$password != null) {
                if (this.$progressListener == null) {
                    keysBackup$prepareKeysBackupVersion$1$backgroundProgressListener$1 = null;
                } else {
                    keysBackup$prepareKeysBackupVersion$1$backgroundProgressListener$1 = new KeysBackup$prepareKeysBackupVersion$1$backgroundProgressListener$1(this);
                }
                GeneratePrivateKeyResult generatePrivateKeyWithPassword = KeysBackupPasswordKt.generatePrivateKeyWithPassword(this.$password, keysBackup$prepareKeysBackupVersion$1$backgroundProgressListener$1);
                String privateKey = olmPkDecryption.setPrivateKey(generatePrivateKeyWithPassword.getPrivateKey());
                Intrinsics.checkExpressionValueIsNotNull(privateKey, "olmPkDecryption.setPriva…vateKeyResult.privateKey)");
                megolmBackupAuthData.setPublicKey(privateKey);
                megolmBackupAuthData.setPrivateKeySalt(generatePrivateKeyWithPassword.getSalt());
                megolmBackupAuthData.setPrivateKeyIterations(Integer.valueOf(generatePrivateKeyWithPassword.getIterations()));
            } else {
                String generateKey = olmPkDecryption.generateKey();
                Intrinsics.checkExpressionValueIsNotNull(generateKey, "publicKey");
                megolmBackupAuthData.setPublicKey(generateKey);
            }
            megolmBackupAuthData.setSignatures(this.this$0.mCrypto.signObject(JsonUtility.getCanonicalizedJsonString(megolmBackupAuthData.signalableJSONDictionary())));
            final MegolmBackupCreationInfo megolmBackupCreationInfo = new MegolmBackupCreationInfo();
            megolmBackupCreationInfo.setAlgorithm(CryptoConstantsKt.MXCRYPTO_ALGORITHM_MEGOLM_BACKUP);
            megolmBackupCreationInfo.setAuthData(megolmBackupAuthData);
            byte[] privateKey2 = olmPkDecryption.privateKey();
            Intrinsics.checkExpressionValueIsNotNull(privateKey2, "olmPkDecryption.privateKey()");
            megolmBackupCreationInfo.setRecoveryKey(RecoveryKeyKt.computeRecoveryKey(privateKey2));
            this.this$0.mCrypto.getUIHandler().post(new Runnable(this) {
                final /* synthetic */ KeysBackup$prepareKeysBackupVersion$1 this$0;

                {
                    this.this$0 = r1;
                }

                public final void run() {
                    this.this$0.$callback.onSuccess(megolmBackupCreationInfo);
                }
            });
        } catch (OlmException e) {
            Log.e(KeysBackup.LOG_TAG, "OlmException: ", e);
            this.this$0.mCrypto.getUIHandler().post(new Runnable(this) {
                final /* synthetic */ KeysBackup$prepareKeysBackupVersion$1 this$0;

                {
                    this.this$0 = r1;
                }

                public final void run() {
                    this.this$0.$callback.onUnexpectedError(e);
                }
            });
        }
    }
}

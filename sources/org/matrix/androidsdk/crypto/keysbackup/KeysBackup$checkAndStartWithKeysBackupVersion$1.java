package org.matrix.androidsdk.crypto.keysbackup;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.SuccessCallback;
import org.matrix.androidsdk.crypto.cryptostore.IMXCryptoStore;
import org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupState;
import org.matrix.androidsdk.crypto.model.keys.KeysVersionResult;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "trustInfo", "Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupVersionTrust;", "kotlin.jvm.PlatformType", "onSuccess"}, k = 3, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
final class KeysBackup$checkAndStartWithKeysBackupVersion$1<T> implements SuccessCallback<KeysBackupVersionTrust> {
    final /* synthetic */ KeysVersionResult $keyBackupVersion;
    final /* synthetic */ KeysBackup this$0;

    KeysBackup$checkAndStartWithKeysBackupVersion$1(KeysBackup keysBackup, KeysVersionResult keysVersionResult) {
        this.this$0 = keysBackup;
        this.$keyBackupVersion = keysVersionResult;
    }

    public final void onSuccess(KeysBackupVersionTrust keysBackupVersionTrust) {
        IMXCryptoStore cryptoStore = this.this$0.mCrypto.getCryptoStore();
        Intrinsics.checkExpressionValueIsNotNull(cryptoStore, "mCrypto.cryptoStore");
        String keyBackupVersion = cryptoStore.getKeyBackupVersion();
        if (keysBackupVersionTrust.getUsable()) {
            String access$getLOG_TAG$cp = KeysBackup.LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("checkAndStartWithKeysBackupVersion: Found usable key backup. version: ");
            sb.append(this.$keyBackupVersion.getVersion());
            Log.d(access$getLOG_TAG$cp, sb.toString());
            if (keyBackupVersion != null && (!Intrinsics.areEqual((Object) keyBackupVersion, (Object) this.$keyBackupVersion.getVersion()))) {
                String access$getLOG_TAG$cp2 = KeysBackup.LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(" -> clean the previously used version ");
                sb2.append(keyBackupVersion);
                Log.d(access$getLOG_TAG$cp2, sb2.toString());
                this.this$0.resetKeysBackupData();
            }
            Log.d(KeysBackup.LOG_TAG, "   -> enabling key backups");
            this.this$0.enableKeysBackup(this.$keyBackupVersion);
            return;
        }
        String access$getLOG_TAG$cp3 = KeysBackup.LOG_TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("checkAndStartWithKeysBackupVersion: No usable key backup. version: ");
        sb3.append(this.$keyBackupVersion.getVersion());
        Log.d(access$getLOG_TAG$cp3, sb3.toString());
        if (keyBackupVersion != null) {
            Log.d(KeysBackup.LOG_TAG, "   -> disabling key backup");
            this.this$0.resetKeysBackupData();
        }
        this.this$0.mKeysBackupStateManager.setState(KeysBackupState.NotTrusted);
    }
}

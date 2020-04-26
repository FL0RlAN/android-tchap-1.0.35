package org.matrix.androidsdk.crypto.keysbackup;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupState;
import org.matrix.androidsdk.crypto.model.keys.KeysVersionResult;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
final class KeysBackup$deleteBackup$1 implements Runnable {
    final /* synthetic */ ApiCallback $callback;
    final /* synthetic */ String $version;
    final /* synthetic */ KeysBackup this$0;

    KeysBackup$deleteBackup$1(KeysBackup keysBackup, String str, ApiCallback apiCallback) {
        this.this$0 = keysBackup;
        this.$version = str;
        this.$callback = apiCallback;
    }

    public final void run() {
        if (this.this$0.getMKeysBackupVersion() != null) {
            String str = this.$version;
            KeysVersionResult mKeysBackupVersion = this.this$0.getMKeysBackupVersion();
            if (mKeysBackupVersion == null) {
                Intrinsics.throwNpe();
            }
            if (Intrinsics.areEqual((Object) str, (Object) mKeysBackupVersion.getVersion())) {
                this.this$0.resetKeysBackupData();
                this.this$0.mKeysBackupVersion = null;
                this.this$0.mKeysBackupStateManager.setState(KeysBackupState.Unknown);
            }
        }
        this.this$0.mRoomKeysRestClient.deleteBackup(this.$version, new ApiCallback<Void>(this) {
            final /* synthetic */ KeysBackup$deleteBackup$1 this$0;

            {
                this.this$0 = r1;
            }

            private final void eventuallyRestartBackup() {
                if (this.this$0.this$0.getState() == KeysBackupState.Unknown) {
                    this.this$0.this$0.checkAndStartKeysBackup();
                }
            }

            public void onSuccess(Void voidR) {
                eventuallyRestartBackup();
                this.this$0.this$0.mCrypto.getUIHandler().post(new KeysBackup$deleteBackup$1$1$onSuccess$1(this));
            }

            public void onUnexpectedError(Exception exc) {
                Intrinsics.checkParameterIsNotNull(exc, "e");
                eventuallyRestartBackup();
                this.this$0.this$0.mCrypto.getUIHandler().post(new KeysBackup$deleteBackup$1$1$onUnexpectedError$1(this, exc));
            }

            public void onNetworkError(Exception exc) {
                Intrinsics.checkParameterIsNotNull(exc, "e");
                eventuallyRestartBackup();
                this.this$0.this$0.mCrypto.getUIHandler().post(new KeysBackup$deleteBackup$1$1$onNetworkError$1(this, exc));
            }

            public void onMatrixError(MatrixError matrixError) {
                Intrinsics.checkParameterIsNotNull(matrixError, "e");
                eventuallyRestartBackup();
                this.this$0.this$0.mCrypto.getUIHandler().post(new KeysBackup$deleteBackup$1$1$onMatrixError$1(this, matrixError));
            }
        });
    }
}

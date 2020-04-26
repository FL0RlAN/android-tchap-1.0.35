package org.matrix.androidsdk.crypto.keysbackup;

import com.google.android.gms.common.internal.ServiceSpecificExtraArgs.CastExtraArgs;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.MXCrypto;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001:\u0002\u0019\u001aB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\rJ\u000e\u0010\u0018\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\rR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\b8F¢\u0006\u0006\u001a\u0004\b\u0007\u0010\tR\u0011\u0010\n\u001a\u00020\b8F¢\u0006\u0006\u001a\u0004\b\n\u0010\tR\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0004¢\u0006\u0002\n\u0000R$\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u000e\u001a\u00020\u000f@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014¨\u0006\u001b"}, d2 = {"Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupStateManager;", "", "crypto", "Lorg/matrix/androidsdk/crypto/MXCrypto;", "(Lorg/matrix/androidsdk/crypto/MXCrypto;)V", "getCrypto", "()Lorg/matrix/androidsdk/crypto/MXCrypto;", "isEnabled", "", "()Z", "isStucked", "mListeners", "Ljava/util/ArrayList;", "Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupStateManager$KeysBackupStateListener;", "newState", "Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupStateManager$KeysBackupState;", "state", "getState", "()Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupStateManager$KeysBackupState;", "setState", "(Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupStateManager$KeysBackupState;)V", "addListener", "", "listener", "removeListener", "KeysBackupState", "KeysBackupStateListener", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: KeysBackupStateManager.kt */
public final class KeysBackupStateManager {
    private final MXCrypto crypto;
    /* access modifiers changed from: private */
    public final ArrayList<KeysBackupStateListener> mListeners = new ArrayList<>();
    private KeysBackupState state = KeysBackupState.Unknown;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u000b\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000b¨\u0006\f"}, d2 = {"Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupStateManager$KeysBackupState;", "", "(Ljava/lang/String;I)V", "Unknown", "CheckingBackUpOnHomeserver", "WrongBackUpVersion", "Disabled", "NotTrusted", "Enabling", "ReadyToBackUp", "WillBackUp", "BackingUp", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: KeysBackupStateManager.kt */
    public enum KeysBackupState {
        Unknown,
        CheckingBackUpOnHomeserver,
        WrongBackUpVersion,
        Disabled,
        NotTrusted,
        Enabling,
        ReadyToBackUp,
        WillBackUp,
        BackingUp
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&¨\u0006\u0006"}, d2 = {"Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupStateManager$KeysBackupStateListener;", "", "onStateChange", "", "newState", "Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupStateManager$KeysBackupState;", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: KeysBackupStateManager.kt */
    public interface KeysBackupStateListener {
        void onStateChange(KeysBackupState keysBackupState);
    }

    public KeysBackupStateManager(MXCrypto mXCrypto) {
        Intrinsics.checkParameterIsNotNull(mXCrypto, "crypto");
        this.crypto = mXCrypto;
    }

    public final MXCrypto getCrypto() {
        return this.crypto;
    }

    public final KeysBackupState getState() {
        return this.state;
    }

    public final void setState(KeysBackupState keysBackupState) {
        Intrinsics.checkParameterIsNotNull(keysBackupState, "newState");
        StringBuilder sb = new StringBuilder();
        sb.append("setState: ");
        sb.append(this.state);
        sb.append(" -> ");
        sb.append(keysBackupState);
        Log.d("KeysBackup", sb.toString());
        this.state = keysBackupState;
        this.crypto.getUIHandler().post(new KeysBackupStateManager$state$1(this, keysBackupState));
    }

    public final boolean isEnabled() {
        return this.state == KeysBackupState.ReadyToBackUp || this.state == KeysBackupState.WillBackUp || this.state == KeysBackupState.BackingUp;
    }

    public final boolean isStucked() {
        return this.state == KeysBackupState.Unknown || this.state == KeysBackupState.Disabled || this.state == KeysBackupState.WrongBackUpVersion || this.state == KeysBackupState.NotTrusted;
    }

    public final void addListener(KeysBackupStateListener keysBackupStateListener) {
        Intrinsics.checkParameterIsNotNull(keysBackupStateListener, CastExtraArgs.LISTENER);
        synchronized (this.mListeners) {
            this.mListeners.add(keysBackupStateListener);
        }
    }

    public final void removeListener(KeysBackupStateListener keysBackupStateListener) {
        Intrinsics.checkParameterIsNotNull(keysBackupStateListener, CastExtraArgs.LISTENER);
        synchronized (this.mListeners) {
            this.mListeners.remove(keysBackupStateListener);
        }
    }
}

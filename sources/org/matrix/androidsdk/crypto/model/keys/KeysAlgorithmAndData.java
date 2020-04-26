package org.matrix.androidsdk.crypto.model.keys;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import kotlin.Metadata;
import org.matrix.androidsdk.core.JsonUtility;
import org.matrix.androidsdk.crypto.keysbackup.MegolmBackupAuthData;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0016\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u000e\u0010\u000f\u001a\n \u0011*\u0004\u0018\u00010\u00100\u0010R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR \u0010\t\u001a\u0004\u0018\u00010\n8\u0006@\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e¨\u0006\u0012"}, d2 = {"Lorg/matrix/androidsdk/crypto/model/keys/KeysAlgorithmAndData;", "", "()V", "algorithm", "", "getAlgorithm", "()Ljava/lang/String;", "setAlgorithm", "(Ljava/lang/String;)V", "authData", "Lcom/google/gson/JsonElement;", "getAuthData", "()Lcom/google/gson/JsonElement;", "setAuthData", "(Lcom/google/gson/JsonElement;)V", "getAuthDataAsMegolmBackupAuthData", "Lorg/matrix/androidsdk/crypto/keysbackup/MegolmBackupAuthData;", "kotlin.jvm.PlatformType", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: KeysAlgorithmAndData.kt */
public class KeysAlgorithmAndData {
    private String algorithm;
    @SerializedName("auth_data")
    private JsonElement authData;

    public final String getAlgorithm() {
        return this.algorithm;
    }

    public final void setAlgorithm(String str) {
        this.algorithm = str;
    }

    public final JsonElement getAuthData() {
        return this.authData;
    }

    public final void setAuthData(JsonElement jsonElement) {
        this.authData = jsonElement;
    }

    public final MegolmBackupAuthData getAuthDataAsMegolmBackupAuthData() {
        return (MegolmBackupAuthData) JsonUtility.getBasicGson().fromJson(this.authData, MegolmBackupAuthData.class);
    }
}

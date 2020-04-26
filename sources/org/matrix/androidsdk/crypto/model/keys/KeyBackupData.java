package org.matrix.androidsdk.crypto.model.keys;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001e\u0010\t\u001a\u00020\n8\u0006@\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001e\u0010\u000f\u001a\u00020\u00108\u0006@\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0011\"\u0004\b\u0012\u0010\u0013R \u0010\u0014\u001a\u0004\u0018\u00010\u00158\u0006@\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019¨\u0006\u001a"}, d2 = {"Lorg/matrix/androidsdk/crypto/model/keys/KeyBackupData;", "", "()V", "firstMessageIndex", "", "getFirstMessageIndex", "()J", "setFirstMessageIndex", "(J)V", "forwardedCount", "", "getForwardedCount", "()I", "setForwardedCount", "(I)V", "isVerified", "", "()Z", "setVerified", "(Z)V", "sessionData", "Lcom/google/gson/JsonElement;", "getSessionData", "()Lcom/google/gson/JsonElement;", "setSessionData", "(Lcom/google/gson/JsonElement;)V", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: KeyBackupData.kt */
public final class KeyBackupData {
    @SerializedName("first_message_index")
    private long firstMessageIndex;
    @SerializedName("forwarded_count")
    private int forwardedCount;
    @SerializedName("is_verified")
    private boolean isVerified;
    @SerializedName("session_data")
    private JsonElement sessionData;

    public final long getFirstMessageIndex() {
        return this.firstMessageIndex;
    }

    public final void setFirstMessageIndex(long j) {
        this.firstMessageIndex = j;
    }

    public final int getForwardedCount() {
        return this.forwardedCount;
    }

    public final void setForwardedCount(int i) {
        this.forwardedCount = i;
    }

    public final boolean isVerified() {
        return this.isVerified;
    }

    public final void setVerified(boolean z) {
        this.isVerified = z;
    }

    public final JsonElement getSessionData() {
        return this.sessionData;
    }

    public final void setSessionData(JsonElement jsonElement) {
        this.sessionData = jsonElement;
    }
}

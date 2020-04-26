package org.matrix.androidsdk.crypto.verification;

import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0010\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0017\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007j\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011j\u0002\b\u0012¨\u0006\u0013"}, d2 = {"Lorg/matrix/androidsdk/crypto/verification/CancelCode;", "", "value", "", "humanReadable", "(Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;)V", "getHumanReadable", "()Ljava/lang/String;", "getValue", "User", "Timeout", "UnknownTransaction", "UnknownMethod", "MismatchedCommitment", "MismatchedSas", "UnexpectedMessage", "InvalidMessage", "MismatchedKeys", "UserMismatchError", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: CancelCode.kt */
public enum CancelCode {
    User("m.user", "the user cancelled the verification"),
    Timeout("m.timeout", "the verification process timed out"),
    UnknownTransaction("m.unknown_transaction", "the device does not know about that transaction"),
    UnknownMethod("m.unknown_method", "the device can’t agree on a key agreement, hash, MAC, or SAS method"),
    MismatchedCommitment("m.mismatched_commitment", "the hash commitment did not match"),
    MismatchedSas("m.mismatched_sas", "the SAS did not match"),
    UnexpectedMessage("m.unexpected_message", "the device received an unexpected message"),
    InvalidMessage("m.invalid_message", "an invalid message was received"),
    MismatchedKeys("m.key_mismatch", "Key mismatch"),
    UserMismatchError("m.user_error", "User mismatch");
    
    private final String humanReadable;
    private final String value;

    private CancelCode(String str, String str2) {
        this.value = str;
        this.humanReadable = str2;
    }

    public final String getHumanReadable() {
        return this.humanReadable;
    }

    public final String getValue() {
        return this.value;
    }
}

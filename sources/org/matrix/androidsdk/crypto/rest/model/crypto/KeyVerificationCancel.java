package org.matrix.androidsdk.crypto.rest.model.crypto;

import com.google.gson.annotations.SerializedName;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.matrix.androidsdk.crypto.verification.CancelCode;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u0000 \t2\u00020\u0001:\u0001\tB\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0007\u001a\u00020\bR\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\n"}, d2 = {"Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationCancel;", "Lorg/matrix/androidsdk/crypto/rest/model/crypto/SendToDeviceObject;", "()V", "code", "", "reason", "transactionID", "isValid", "", "Companion", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: KeyVerificationCancel.kt */
public final class KeyVerificationCancel implements SendToDeviceObject {
    public static final Companion Companion = new Companion(null);
    public String code;
    public String reason;
    @SerializedName("transaction_id")
    public String transactionID;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b¨\u0006\t"}, d2 = {"Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationCancel$Companion;", "", "()V", "create", "Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationCancel;", "tid", "", "cancelCode", "Lorg/matrix/androidsdk/crypto/verification/CancelCode;", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: KeyVerificationCancel.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final KeyVerificationCancel create(String str, CancelCode cancelCode) {
            Intrinsics.checkParameterIsNotNull(str, "tid");
            Intrinsics.checkParameterIsNotNull(cancelCode, "cancelCode");
            KeyVerificationCancel keyVerificationCancel = new KeyVerificationCancel();
            keyVerificationCancel.transactionID = str;
            keyVerificationCancel.code = cancelCode.getValue();
            keyVerificationCancel.reason = cancelCode.getHumanReadable();
            return keyVerificationCancel;
        }
    }

    public final boolean isValid() {
        CharSequence charSequence = this.transactionID;
        if (!(charSequence == null || StringsKt.isBlank(charSequence))) {
            CharSequence charSequence2 = this.code;
            return !(charSequence2 == null || StringsKt.isBlank(charSequence2));
        }
    }
}

package org.matrix.androidsdk.features.terms;

import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\u0018\u00002\u00060\u0001j\u0002`\u0002B\u000f\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004¢\u0006\u0002\u0010\u0005R\u0013\u0010\u0003\u001a\u0004\u0018\u00010\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\b"}, d2 = {"Lorg/matrix/androidsdk/features/terms/TermsNotSignedException;", "Ljava/lang/Exception;", "Lkotlin/Exception;", "token", "", "(Ljava/lang/String;)V", "getToken", "()Ljava/lang/String;", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: TermsNotSignedException.kt */
public final class TermsNotSignedException extends Exception {
    private final String token;

    public TermsNotSignedException(String str) {
        this.token = str;
    }

    public final String getToken() {
        return this.token;
    }
}

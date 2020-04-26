package org.matrix.androidsdk.core;

import android.content.Context;
import kotlin.Metadata;
import org.matrix.androidsdk.ssl.UnrecognizedCertificateException;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\u0010\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&J\u0010\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\nH&¨\u0006\u000b"}, d2 = {"Lorg/matrix/androidsdk/core/DataHandlerInterface;", "", "getContext", "Landroid/content/Context;", "onConfigurationError", "", "errcode", "", "onSSLCertificateError", "unrecCertEx", "Lorg/matrix/androidsdk/ssl/UnrecognizedCertificateException;", "matrix-sdk-core_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: DataHandlerInterface.kt */
public interface DataHandlerInterface {
    Context getContext();

    void onConfigurationError(String str);

    void onSSLCertificateError(UnrecognizedCertificateException unrecognizedCertificateException);
}

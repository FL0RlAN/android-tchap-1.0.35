package org.matrix.androidsdk.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import javax.net.ssl.X509TrustManager;

public class PinnedTrustManager implements X509TrustManager {
    @Nullable
    private final X509TrustManager mDefaultTrustManager;
    private final List<Fingerprint> mFingerprints;

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    public PinnedTrustManager(List<Fingerprint> list, @Nullable X509TrustManager x509TrustManager) {
        this.mFingerprints = list;
        this.mDefaultTrustManager = x509TrustManager;
    }

    public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        try {
            if (this.mDefaultTrustManager != null) {
                this.mDefaultTrustManager.checkClientTrusted(x509CertificateArr, str);
                return;
            }
        } catch (CertificateException e) {
            List<Fingerprint> list = this.mFingerprints;
            if (list == null || list.size() == 0) {
                throw new UnrecognizedCertificateException(x509CertificateArr[0], Fingerprint.newSha256Fingerprint(x509CertificateArr[0]), e.getCause());
            }
        }
        checkTrusted("client", x509CertificateArr);
    }

    public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        try {
            if (this.mDefaultTrustManager != null) {
                this.mDefaultTrustManager.checkServerTrusted(x509CertificateArr, str);
                return;
            }
        } catch (CertificateException e) {
            List<Fingerprint> list = this.mFingerprints;
            if (list == null || list.isEmpty()) {
                throw new UnrecognizedCertificateException(x509CertificateArr[0], Fingerprint.newSha256Fingerprint(x509CertificateArr[0]), e.getCause());
            }
        }
        checkTrusted("server", x509CertificateArr);
    }

    private void checkTrusted(String str, X509Certificate[] x509CertificateArr) throws CertificateException {
        boolean z = false;
        X509Certificate x509Certificate = x509CertificateArr[0];
        List<Fingerprint> list = this.mFingerprints;
        if (list != null) {
            Iterator it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Fingerprint fingerprint = (Fingerprint) it.next();
                if (fingerprint != null && fingerprint.matchesCert(x509Certificate)) {
                    z = true;
                    break;
                }
            }
        }
        if (!z) {
            throw new UnrecognizedCertificateException(x509Certificate, Fingerprint.newSha256Fingerprint(x509Certificate), null);
        }
    }
}

package org.matrix.androidsdk.ssl;

import android.util.Pair;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import kotlin.UByte;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.ConnectionSpec.Builder;
import okhttp3.TlsVersion;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.core.Log;

public class CertUtil {
    private static final String LOG_TAG = CertUtil.class.getSimpleName();
    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static byte[] generateSha256Fingerprint(X509Certificate x509Certificate) throws CertificateException {
        return generateFingerprint(x509Certificate, "SHA-256");
    }

    public static byte[] generateSha1Fingerprint(X509Certificate x509Certificate) throws CertificateException {
        return generateFingerprint(x509Certificate, "SHA-1");
    }

    private static byte[] generateFingerprint(X509Certificate x509Certificate, String str) throws CertificateException {
        try {
            return MessageDigest.getInstance(str).digest(x509Certificate.getEncoded());
        } catch (Exception e) {
            throw new CertificateException(e);
        }
    }

    public static String fingerprintToHexString(byte[] bArr) {
        return fingerprintToHexString(bArr, ' ');
    }

    public static String fingerprintToHexString(byte[] bArr, char c) {
        char[] cArr = new char[(bArr.length * 3)];
        for (int i = 0; i < bArr.length; i++) {
            byte b = bArr[i] & UByte.MAX_VALUE;
            int i2 = i * 3;
            char[] cArr2 = hexArray;
            cArr[i2] = cArr2[b >>> 4];
            cArr[i2 + 1] = cArr2[b & 15];
            cArr[i2 + 2] = c;
        }
        return new String(cArr, 0, cArr.length - 1);
    }

    public static UnrecognizedCertificateException getCertificateException(Throwable th) {
        int i = 0;
        while (th != null && i < 10) {
            if (th instanceof UnrecognizedCertificateException) {
                return (UnrecognizedCertificateException) th;
            }
            th = th.getCause();
            i++;
        }
        return null;
    }

    public static Pair<SSLSocketFactory, X509TrustManager> newPinnedSSLSocketFactory(HomeServerConnectionConfig homeServerConnectionConfig) {
        X509TrustManager x509TrustManager;
        Object obj;
        TrustManagerFactory trustManagerFactory;
        if (!homeServerConnectionConfig.shouldPin()) {
            try {
                trustManagerFactory = TrustManagerFactory.getInstance("PKIX");
            } catch (NoSuchAlgorithmException e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## newPinnedSSLSocketFactory() : TrustManagerFactory.getInstance failed ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
                trustManagerFactory = null;
            }
            if (trustManagerFactory == null) {
                try {
                    trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                } catch (NoSuchAlgorithmException e2) {
                    String str2 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("## newPinnedSSLSocketFactory() : TrustManagerFactory.getInstance with default algorithm failed ");
                    sb2.append(e2.getMessage());
                    Log.e(str2, sb2.toString(), e2);
                }
            }
            if (trustManagerFactory != null) {
                try {
                    trustManagerFactory.init(null);
                    TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
                    int i = 0;
                    while (true) {
                        if (i >= trustManagers.length) {
                            break;
                        } else if (trustManagers[i] instanceof X509TrustManager) {
                            x509TrustManager = (X509TrustManager) trustManagers[i];
                            break;
                        } else {
                            i++;
                        }
                    }
                } catch (KeyStoreException e3) {
                    String str3 = LOG_TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("## newPinnedSSLSocketFactory() : ");
                    sb3.append(e3.getMessage());
                    Log.e(str3, sb3.toString(), e3);
                }
            }
        }
        x509TrustManager = null;
        PinnedTrustManager pinnedTrustManager = new PinnedTrustManager(homeServerConnectionConfig.getAllowedFingerprints(), x509TrustManager);
        TrustManager[] trustManagerArr = {pinnedTrustManager};
        try {
            if (!homeServerConnectionConfig.forceUsageOfTlsVersions() || homeServerConnectionConfig.getAcceptedTlsVersions() == null) {
                SSLContext instance = SSLContext.getInstance("TLS");
                instance.init(null, trustManagerArr, new SecureRandom());
                obj = instance.getSocketFactory();
            } else {
                obj = new TLSSocketFactory(trustManagerArr, homeServerConnectionConfig.getAcceptedTlsVersions());
            }
            return new Pair<>(obj, pinnedTrustManager);
        } catch (Exception e4) {
            throw new RuntimeException(e4);
        }
    }

    public static HostnameVerifier newHostnameVerifier(HomeServerConnectionConfig homeServerConnectionConfig) {
        final HostnameVerifier defaultHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
        final List allowedFingerprints = homeServerConnectionConfig.getAllowedFingerprints();
        return new HostnameVerifier() {
            public boolean verify(String str, SSLSession sSLSession) {
                Certificate[] peerCertificates;
                if (defaultHostnameVerifier.verify(str, sSLSession)) {
                    return true;
                }
                List list = allowedFingerprints;
                if (!(list == null || list.size() == 0)) {
                    try {
                        for (Certificate certificate : sSLSession.getPeerCertificates()) {
                            for (Fingerprint fingerprint : allowedFingerprints) {
                                if (fingerprint != null && (certificate instanceof X509Certificate) && fingerprint.matchesCert((X509Certificate) certificate)) {
                                    return true;
                                }
                            }
                        }
                    } catch (CertificateException | SSLPeerUnverifiedException unused) {
                    }
                }
                return false;
            }
        };
    }

    public static List<ConnectionSpec> newConnectionSpecs(HomeServerConnectionConfig homeServerConnectionConfig, String str) {
        Builder builder = new Builder(ConnectionSpec.MODERN_TLS);
        List acceptedTlsVersions = homeServerConnectionConfig.getAcceptedTlsVersions();
        if (acceptedTlsVersions != null) {
            builder.tlsVersions((TlsVersion[]) acceptedTlsVersions.toArray(new TlsVersion[0]));
        }
        List acceptedTlsCipherSuites = homeServerConnectionConfig.getAcceptedTlsCipherSuites();
        if (acceptedTlsCipherSuites != null) {
            builder.cipherSuites((CipherSuite[]) acceptedTlsCipherSuites.toArray(new CipherSuite[0]));
        }
        builder.supportsTlsExtensions(homeServerConnectionConfig.shouldAcceptTlsExtensions());
        ArrayList arrayList = new ArrayList();
        arrayList.add(builder.build());
        if (str.startsWith("http://")) {
            arrayList.add(ConnectionSpec.CLEARTEXT);
        }
        return arrayList;
    }
}

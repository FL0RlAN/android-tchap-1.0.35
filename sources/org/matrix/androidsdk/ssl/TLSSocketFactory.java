package org.matrix.androidsdk.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import okhttp3.TlsVersion;
import org.matrix.androidsdk.core.Log;

class TLSSocketFactory extends SSLSocketFactory {
    private static final String LOG_TAG = TLSSocketFactory.class.getSimpleName();
    private String[] enabledProtocols;
    private SSLSocketFactory internalSSLSocketFactory;

    TLSSocketFactory(TrustManager[] trustManagerArr, List<TlsVersion> list) throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext instance = SSLContext.getInstance("TLS");
        instance.init(null, trustManagerArr, new SecureRandom());
        this.internalSSLSocketFactory = instance.getSocketFactory();
        this.enabledProtocols = new String[list.size()];
        int i = 0;
        for (TlsVersion javaName : list) {
            this.enabledProtocols[i] = javaName.javaName();
            i++;
        }
    }

    public String[] getDefaultCipherSuites() {
        return this.internalSSLSocketFactory.getDefaultCipherSuites();
    }

    public String[] getSupportedCipherSuites() {
        return this.internalSSLSocketFactory.getSupportedCipherSuites();
    }

    public Socket createSocket() throws IOException {
        return enableTLSOnSocket(this.internalSSLSocketFactory.createSocket());
    }

    public Socket createSocket(Socket socket, String str, int i, boolean z) throws IOException {
        return enableTLSOnSocket(this.internalSSLSocketFactory.createSocket(socket, str, i, z));
    }

    public Socket createSocket(String str, int i) throws IOException, UnknownHostException {
        return enableTLSOnSocket(this.internalSSLSocketFactory.createSocket(str, i));
    }

    public Socket createSocket(String str, int i, InetAddress inetAddress, int i2) throws IOException, UnknownHostException {
        return enableTLSOnSocket(this.internalSSLSocketFactory.createSocket(str, i, inetAddress, i2));
    }

    public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
        return enableTLSOnSocket(this.internalSSLSocketFactory.createSocket(inetAddress, i));
    }

    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) throws IOException {
        return enableTLSOnSocket(this.internalSSLSocketFactory.createSocket(inetAddress, i, inetAddress2, i2));
    }

    private Socket enableTLSOnSocket(Socket socket) {
        String[] strArr;
        if (socket != null && (socket instanceof SSLSocket)) {
            SSLSocket sSLSocket = (SSLSocket) socket;
            List asList = Arrays.asList(sSLSocket.getSupportedProtocols());
            ArrayList arrayList = new ArrayList();
            for (String str : this.enabledProtocols) {
                if (asList.contains(str)) {
                    arrayList.add(str);
                }
            }
            if (!arrayList.isEmpty()) {
                try {
                    sSLSocket.setEnabledProtocols((String[]) arrayList.toArray(new String[arrayList.size()]));
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Exception: ", e);
                }
            }
        }
        return socket;
    }
}

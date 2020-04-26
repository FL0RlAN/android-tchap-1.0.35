package org.matrix.androidsdk;

import android.net.Uri;
import android.text.TextUtils;
import im.vector.repositories.ServerUrlsRepository;
import java.util.ArrayList;
import java.util.List;
import okhttp3.CipherSuite;
import okhttp3.TlsVersion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.rest.model.login.Credentials;
import org.matrix.androidsdk.ssl.Fingerprint;

public class HomeServerConnectionConfig {
    /* access modifiers changed from: private */
    public List<Fingerprint> mAllowedFingerprints;
    /* access modifiers changed from: private */
    public Uri mAntiVirusServerUri;
    /* access modifiers changed from: private */
    public Credentials mCredentials;
    /* access modifiers changed from: private */
    public boolean mForceUsageTlsVersions;
    /* access modifiers changed from: private */
    public Uri mHomeServerUri;
    /* access modifiers changed from: private */
    public Uri mIdentityServerUri;
    /* access modifiers changed from: private */
    public boolean mPin;
    /* access modifiers changed from: private */
    public boolean mShouldAcceptTlsExtensions;
    /* access modifiers changed from: private */
    public List<CipherSuite> mTlsCipherSuites;
    /* access modifiers changed from: private */
    public List<TlsVersion> mTlsVersions;

    public static class Builder {
        private HomeServerConnectionConfig mHomeServerConnectionConfig;

        public Builder() {
            this.mHomeServerConnectionConfig = new HomeServerConnectionConfig();
        }

        public Builder(HomeServerConnectionConfig homeServerConnectionConfig) {
            try {
                this.mHomeServerConnectionConfig = HomeServerConnectionConfig.fromJson(homeServerConnectionConfig.toJson());
            } catch (JSONException e) {
                throw new RuntimeException("Unable to create a HomeServerConnectionConfig", e);
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:5:0x001a, code lost:
            if ("https".equals(r6.getScheme()) != false) goto L_0x001c;
         */
        public Builder withHomeServerUri(Uri uri) {
            String str = "Invalid home server URI: ";
            if (uri != null) {
                if (!"http".equals(uri.getScheme())) {
                }
                if (uri.toString().endsWith("/")) {
                    try {
                        String uri2 = uri.toString();
                        this.mHomeServerConnectionConfig.mHomeServerUri = Uri.parse(uri2.substring(0, uri2.length() - 1));
                    } catch (Exception unused) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(str);
                        sb.append(uri);
                        throw new RuntimeException(sb.toString());
                    }
                } else {
                    this.mHomeServerConnectionConfig.mHomeServerUri = uri;
                }
                return this;
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(uri);
            throw new RuntimeException(sb2.toString());
        }

        public Builder withIdentityServerUri(Uri uri) {
            String str = "Invalid identity server URI: ";
            if (uri != null) {
                if (!"http".equals(uri.getScheme())) {
                    if (!"https".equals(uri.getScheme())) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(str);
                        sb.append(uri);
                        throw new RuntimeException(sb.toString());
                    }
                }
            }
            if (uri == null || !uri.toString().endsWith("/")) {
                this.mHomeServerConnectionConfig.mIdentityServerUri = uri;
            } else {
                try {
                    String uri2 = uri.toString();
                    this.mHomeServerConnectionConfig.mIdentityServerUri = Uri.parse(uri2.substring(0, uri2.length() - 1));
                } catch (Exception unused) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str);
                    sb2.append(uri);
                    throw new RuntimeException(sb2.toString());
                }
            }
            return this;
        }

        public Builder withCredentials(Credentials credentials) {
            this.mHomeServerConnectionConfig.mCredentials = credentials;
            return this;
        }

        public Builder addAllowedFingerPrint(Fingerprint fingerprint) {
            if (fingerprint != null) {
                this.mHomeServerConnectionConfig.mAllowedFingerprints.add(fingerprint);
            }
            return this;
        }

        public Builder withPin(boolean z) {
            this.mHomeServerConnectionConfig.mPin = z;
            return this;
        }

        public Builder withShouldAcceptTlsExtensions(boolean z) {
            this.mHomeServerConnectionConfig.mShouldAcceptTlsExtensions = z;
            return this;
        }

        public Builder addAcceptedTlsVersion(TlsVersion tlsVersion) {
            if (this.mHomeServerConnectionConfig.mTlsVersions == null) {
                this.mHomeServerConnectionConfig.mTlsVersions = new ArrayList();
            }
            this.mHomeServerConnectionConfig.mTlsVersions.add(tlsVersion);
            return this;
        }

        public Builder forceUsageOfTlsVersions(boolean z) {
            this.mHomeServerConnectionConfig.mForceUsageTlsVersions = z;
            return this;
        }

        public Builder addAcceptedTlsCipherSuite(CipherSuite cipherSuite) {
            if (this.mHomeServerConnectionConfig.mTlsCipherSuites == null) {
                this.mHomeServerConnectionConfig.mTlsCipherSuites = new ArrayList();
            }
            this.mHomeServerConnectionConfig.mTlsCipherSuites.add(cipherSuite);
            return this;
        }

        public Builder withAntiVirusServerUri(Uri uri) {
            if (uri != null) {
                if (!"http".equals(uri.getScheme())) {
                    if (!"https".equals(uri.getScheme())) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Invalid antivirus server URI: ");
                        sb.append(uri);
                        throw new RuntimeException(sb.toString());
                    }
                }
            }
            this.mHomeServerConnectionConfig.mAntiVirusServerUri = uri;
            return this;
        }

        public Builder withTlsLimitations(boolean z, boolean z2) {
            if (z) {
                withShouldAcceptTlsExtensions(false);
                addAcceptedTlsVersion(TlsVersion.TLS_1_2);
                addAcceptedTlsVersion(TlsVersion.TLS_1_3);
                forceUsageOfTlsVersions(z2);
                addAcceptedTlsCipherSuite(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256);
                addAcceptedTlsCipherSuite(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256);
                addAcceptedTlsCipherSuite(CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256);
                addAcceptedTlsCipherSuite(CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256);
                addAcceptedTlsCipherSuite(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384);
                addAcceptedTlsCipherSuite(CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384);
                addAcceptedTlsCipherSuite(CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256);
                addAcceptedTlsCipherSuite(CipherSuite.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256);
                if (z2) {
                    addAcceptedTlsCipherSuite(CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA);
                    addAcceptedTlsCipherSuite(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA);
                }
            }
            return this;
        }

        public HomeServerConnectionConfig build() {
            if (this.mHomeServerConnectionConfig.mHomeServerUri != null) {
                return this.mHomeServerConnectionConfig;
            }
            throw new RuntimeException("Home server URI not set");
        }
    }

    private HomeServerConnectionConfig() {
        this.mAllowedFingerprints = new ArrayList();
        this.mShouldAcceptTlsExtensions = true;
    }

    public void setHomeserverUri(Uri uri) {
        this.mHomeServerUri = uri;
    }

    public Uri getHomeserverUri() {
        return this.mHomeServerUri;
    }

    public Uri getIdentityServerUri() {
        Uri uri = this.mIdentityServerUri;
        if (uri != null) {
            return uri;
        }
        return this.mHomeServerUri;
    }

    public Uri getAntiVirusServerUri() {
        Uri uri = this.mAntiVirusServerUri;
        if (uri != null) {
            return uri;
        }
        return this.mHomeServerUri;
    }

    public List<Fingerprint> getAllowedFingerprints() {
        return this.mAllowedFingerprints;
    }

    public Credentials getCredentials() {
        return this.mCredentials;
    }

    public void setCredentials(Credentials credentials) {
        this.mCredentials = credentials;
        if (credentials.wellKnown != null) {
            String str = "setCredentials";
            String str2 = "/";
            if (credentials.wellKnown.homeServer != null) {
                String str3 = credentials.wellKnown.homeServer.baseURL;
                if (!TextUtils.isEmpty(str3)) {
                    if (str3.endsWith(str2)) {
                        str3 = str3.substring(0, str3.length() - 1);
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Overriding homeserver url to ");
                    sb.append(str3);
                    Log.d(str, sb.toString());
                    this.mHomeServerUri = Uri.parse(str3);
                }
            }
            if (credentials.wellKnown.identityServer != null) {
                String str4 = credentials.wellKnown.identityServer.baseURL;
                if (!TextUtils.isEmpty(str4)) {
                    if (str4.endsWith(str2)) {
                        str4 = str4.substring(0, str4.length() - 1);
                    }
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Overriding identity server url to ");
                    sb2.append(str4);
                    Log.d(str, sb2.toString());
                    this.mIdentityServerUri = Uri.parse(str4);
                }
            }
        }
    }

    public boolean shouldPin() {
        return this.mPin;
    }

    public List<TlsVersion> getAcceptedTlsVersions() {
        return this.mTlsVersions;
    }

    public List<CipherSuite> getAcceptedTlsCipherSuites() {
        return this.mTlsCipherSuites;
    }

    public boolean shouldAcceptTlsExtensions() {
        return this.mShouldAcceptTlsExtensions;
    }

    public boolean forceUsageOfTlsVersions() {
        return this.mForceUsageTlsVersions;
    }

    public String toString() {
        Object obj;
        StringBuilder sb = new StringBuilder();
        sb.append("HomeserverConnectionConfig{mHomeServerUri=");
        sb.append(this.mHomeServerUri);
        sb.append(", mIdentityServerUri=");
        sb.append(this.mIdentityServerUri);
        sb.append(", mAntiVirusServerUri=");
        sb.append(this.mAntiVirusServerUri);
        sb.append(", mAllowedFingerprints size=");
        sb.append(this.mAllowedFingerprints.size());
        sb.append(", mCredentials=");
        sb.append(this.mCredentials);
        sb.append(", mPin=");
        sb.append(this.mPin);
        sb.append(", mShouldAcceptTlsExtensions=");
        sb.append(this.mShouldAcceptTlsExtensions);
        sb.append(", mTlsVersions=");
        List<TlsVersion> list = this.mTlsVersions;
        Object obj2 = "";
        if (list == null) {
            obj = obj2;
        } else {
            obj = Integer.valueOf(list.size());
        }
        sb.append(obj);
        sb.append(", mTlsCipherSuites=");
        List<CipherSuite> list2 = this.mTlsCipherSuites;
        if (list2 != null) {
            obj2 = Integer.valueOf(list2.size());
        }
        sb.append(obj2);
        sb.append('}');
        return sb.toString();
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(ServerUrlsRepository.HOME_SERVER_URL_PREF, this.mHomeServerUri.toString());
        jSONObject.put(ServerUrlsRepository.IDENTITY_SERVER_URL_PREF, getIdentityServerUri().toString());
        Uri uri = this.mAntiVirusServerUri;
        if (uri != null) {
            jSONObject.put("antivirus_server_url", uri.toString());
        }
        jSONObject.put("pin", this.mPin);
        Credentials credentials = this.mCredentials;
        if (credentials != null) {
            jSONObject.put("credentials", credentials.toJson());
        }
        List<Fingerprint> list = this.mAllowedFingerprints;
        if (list != null) {
            ArrayList arrayList = new ArrayList(list.size());
            for (Fingerprint json : this.mAllowedFingerprints) {
                arrayList.add(json.toJson());
            }
            jSONObject.put("fingerprints", new JSONArray(arrayList));
        }
        jSONObject.put("tls_extensions", this.mShouldAcceptTlsExtensions);
        List<TlsVersion> list2 = this.mTlsVersions;
        if (list2 != null) {
            ArrayList arrayList2 = new ArrayList(list2.size());
            for (TlsVersion javaName : this.mTlsVersions) {
                arrayList2.add(javaName.javaName());
            }
            jSONObject.put("tls_versions", new JSONArray(arrayList2));
        }
        jSONObject.put("force_usage_of_tls_versions", this.mForceUsageTlsVersions);
        List<CipherSuite> list3 = this.mTlsCipherSuites;
        if (list3 != null) {
            ArrayList arrayList3 = new ArrayList(list3.size());
            for (CipherSuite javaName2 : this.mTlsCipherSuites) {
                arrayList3.add(javaName2.javaName());
            }
            jSONObject.put("tls_cipher_suites", new JSONArray(arrayList3));
        }
        return jSONObject;
    }

    public static HomeServerConnectionConfig fromJson(JSONObject jSONObject) throws JSONException {
        JSONObject optJSONObject = jSONObject.optJSONObject("credentials");
        Uri uri = null;
        Credentials fromJson = optJSONObject != null ? Credentials.fromJson(optJSONObject) : null;
        Builder withHomeServerUri = new Builder().withHomeServerUri(Uri.parse(jSONObject.getString(ServerUrlsRepository.HOME_SERVER_URL_PREF)));
        String str = ServerUrlsRepository.IDENTITY_SERVER_URL_PREF;
        if (jSONObject.has(str)) {
            uri = Uri.parse(jSONObject.getString(str));
        }
        Builder withCredentials = withHomeServerUri.withIdentityServerUri(uri).withCredentials(fromJson);
        Builder withPin = withCredentials.withPin(jSONObject.optBoolean("pin", false));
        JSONArray optJSONArray = jSONObject.optJSONArray("fingerprints");
        if (optJSONArray != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                withPin.addAllowedFingerPrint(Fingerprint.fromJson(optJSONArray.getJSONObject(i)));
            }
        }
        String str2 = "antivirus_server_url";
        if (jSONObject.has(str2)) {
            withPin.withAntiVirusServerUri(Uri.parse(jSONObject.getString(str2)));
        }
        withPin.withShouldAcceptTlsExtensions(jSONObject.optBoolean("tls_extensions", true));
        String str3 = "tls_versions";
        if (jSONObject.has(str3)) {
            JSONArray optJSONArray2 = jSONObject.optJSONArray(str3);
            if (optJSONArray2 != null) {
                for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {
                    withPin.addAcceptedTlsVersion(TlsVersion.forJavaName(optJSONArray2.getString(i2)));
                }
            }
        }
        withPin.forceUsageOfTlsVersions(jSONObject.optBoolean("force_usage_of_tls_versions", false));
        String str4 = "tls_cipher_suites";
        if (jSONObject.has(str4)) {
            JSONArray optJSONArray3 = jSONObject.optJSONArray(str4);
            if (optJSONArray3 != null) {
                for (int i3 = 0; i3 < optJSONArray3.length(); i3++) {
                    withPin.addAcceptedTlsCipherSuite(CipherSuite.forJavaName(optJSONArray3.getString(i3)));
                }
            }
        }
        return withPin.build();
    }
}

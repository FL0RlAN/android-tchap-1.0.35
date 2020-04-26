package org.matrix.androidsdk.ssl;

import android.util.Base64;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import org.json.JSONException;
import org.json.JSONObject;

public class Fingerprint {
    private final byte[] mBytes;
    private String mDisplayableHexRepr = null;
    private final HashType mHashType;

    /* renamed from: org.matrix.androidsdk.ssl.Fingerprint$1 reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$org$matrix$androidsdk$ssl$Fingerprint$HashType = new int[HashType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        static {
            $SwitchMap$org$matrix$androidsdk$ssl$Fingerprint$HashType[HashType.SHA256.ordinal()] = 1;
            $SwitchMap$org$matrix$androidsdk$ssl$Fingerprint$HashType[HashType.SHA1.ordinal()] = 2;
        }
    }

    public enum HashType {
        SHA1,
        SHA256
    }

    public Fingerprint(HashType hashType, byte[] bArr) {
        this.mHashType = hashType;
        this.mBytes = bArr;
    }

    public static Fingerprint newSha256Fingerprint(X509Certificate x509Certificate) throws CertificateException {
        return new Fingerprint(HashType.SHA256, CertUtil.generateSha256Fingerprint(x509Certificate));
    }

    public static Fingerprint newSha1Fingerprint(X509Certificate x509Certificate) throws CertificateException {
        return new Fingerprint(HashType.SHA1, CertUtil.generateSha1Fingerprint(x509Certificate));
    }

    public HashType getType() {
        return this.mHashType;
    }

    public byte[] getBytes() {
        return this.mBytes;
    }

    public String getBytesAsHexString() {
        if (this.mDisplayableHexRepr == null) {
            this.mDisplayableHexRepr = CertUtil.fingerprintToHexString(this.mBytes);
        }
        return this.mDisplayableHexRepr;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("bytes", Base64.encodeToString(getBytes(), 0));
        jSONObject.put("hash_type", this.mHashType.toString());
        return jSONObject;
    }

    public static Fingerprint fromJson(JSONObject jSONObject) throws JSONException {
        HashType hashType;
        String string = jSONObject.getString("hash_type");
        byte[] decode = Base64.decode(jSONObject.getString("bytes"), 0);
        if ("SHA256".equalsIgnoreCase(string)) {
            hashType = HashType.SHA256;
        } else if ("SHA1".equalsIgnoreCase(string)) {
            hashType = HashType.SHA1;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Unrecognized hash type: ");
            sb.append(string);
            throw new JSONException(sb.toString());
        }
        return new Fingerprint(hashType, decode);
    }

    public boolean matchesCert(X509Certificate x509Certificate) throws CertificateException {
        Fingerprint fingerprint;
        int i = AnonymousClass1.$SwitchMap$org$matrix$androidsdk$ssl$Fingerprint$HashType[this.mHashType.ordinal()];
        if (i == 1) {
            fingerprint = newSha256Fingerprint(x509Certificate);
        } else if (i != 2) {
            fingerprint = null;
        } else {
            fingerprint = newSha1Fingerprint(x509Certificate);
        }
        return equals(fingerprint);
    }

    public String toString() {
        return String.format("Fingerprint{type: '%s', fingeprint: '%s'}", new Object[]{this.mHashType.toString(), getBytesAsHexString()});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Fingerprint fingerprint = (Fingerprint) obj;
        if (!Arrays.equals(this.mBytes, fingerprint.mBytes)) {
            return false;
        }
        if (this.mHashType != fingerprint.mHashType) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        byte[] bArr = this.mBytes;
        int i = 0;
        int hashCode = (bArr != null ? Arrays.hashCode(bArr) : 0) * 31;
        HashType hashType = this.mHashType;
        if (hashType != null) {
            i = hashType.hashCode();
        }
        return hashCode + i;
    }
}

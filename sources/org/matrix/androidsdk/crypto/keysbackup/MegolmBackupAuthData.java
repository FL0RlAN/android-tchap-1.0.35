package org.matrix.androidsdk.crypto.keysbackup;

import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010$\n\u0002\b\u0017\n\u0002\u0010\u000b\n\u0002\b\u0005\b\b\u0018\u00002\u00020\u0001BK\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\"\b\u0002\u0010\u0007\u001a\u001c\u0012\u0004\u0012\u00020\u0003\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\b\u0018\u00010\b¢\u0006\u0002\u0010\tJ\t\u0010\u0019\u001a\u00020\u0003HÆ\u0003J\u000b\u0010\u001a\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u0006HÆ\u0003¢\u0006\u0002\u0010\u000bJ#\u0010\u001c\u001a\u001c\u0012\u0004\u0012\u00020\u0003\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\b\u0018\u00010\bHÆ\u0003JT\u0010\u001d\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062\"\b\u0002\u0010\u0007\u001a\u001c\u0012\u0004\u0012\u00020\u0003\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\b\u0018\u00010\bHÆ\u0001¢\u0006\u0002\u0010\u001eJ\u0013\u0010\u001f\u001a\u00020 2\b\u0010!\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\"\u001a\u00020\u0006HÖ\u0001J\u0012\u0010#\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\bJ\t\u0010$\u001a\u00020\u0003HÖ\u0001R\"\u0010\u0005\u001a\u0004\u0018\u00010\u00068\u0006@\u0006X\u000e¢\u0006\u0010\n\u0002\u0010\u000e\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR \u0010\u0004\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001e\u0010\u0002\u001a\u00020\u00038\u0006@\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0010\"\u0004\b\u0014\u0010\u0012R4\u0010\u0007\u001a\u001c\u0012\u0004\u0012\u00020\u0003\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\b\u0018\u00010\bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018¨\u0006%"}, d2 = {"Lorg/matrix/androidsdk/crypto/keysbackup/MegolmBackupAuthData;", "", "publicKey", "", "privateKeySalt", "privateKeyIterations", "", "signatures", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/util/Map;)V", "getPrivateKeyIterations", "()Ljava/lang/Integer;", "setPrivateKeyIterations", "(Ljava/lang/Integer;)V", "Ljava/lang/Integer;", "getPrivateKeySalt", "()Ljava/lang/String;", "setPrivateKeySalt", "(Ljava/lang/String;)V", "getPublicKey", "setPublicKey", "getSignatures", "()Ljava/util/Map;", "setSignatures", "(Ljava/util/Map;)V", "component1", "component2", "component3", "component4", "copy", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/util/Map;)Lorg/matrix/androidsdk/crypto/keysbackup/MegolmBackupAuthData;", "equals", "", "other", "hashCode", "signalableJSONDictionary", "toString", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: MegolmBackupAuthData.kt */
public final class MegolmBackupAuthData {
    @SerializedName("private_key_iterations")
    private Integer privateKeyIterations;
    @SerializedName("private_key_salt")
    private String privateKeySalt;
    @SerializedName("public_key")
    private String publicKey;
    private Map<String, ? extends Map<String, String>> signatures;

    public MegolmBackupAuthData() {
        this(null, null, null, null, 15, null);
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Map, code=java.util.Map<java.lang.String, ? extends java.util.Map<java.lang.String, java.lang.String>>, for r4v0, types: [java.util.Map] */
    public static /* synthetic */ MegolmBackupAuthData copy$default(MegolmBackupAuthData megolmBackupAuthData, String str, String str2, Integer num, Map<String, ? extends Map<String, String>> map, int i, Object obj) {
        if ((i & 1) != 0) {
            str = megolmBackupAuthData.publicKey;
        }
        if ((i & 2) != 0) {
            str2 = megolmBackupAuthData.privateKeySalt;
        }
        if ((i & 4) != 0) {
            num = megolmBackupAuthData.privateKeyIterations;
        }
        if ((i & 8) != 0) {
            map = megolmBackupAuthData.signatures;
        }
        return megolmBackupAuthData.copy(str, str2, num, map);
    }

    public final String component1() {
        return this.publicKey;
    }

    public final String component2() {
        return this.privateKeySalt;
    }

    public final Integer component3() {
        return this.privateKeyIterations;
    }

    public final Map<String, Map<String, String>> component4() {
        return this.signatures;
    }

    public final MegolmBackupAuthData copy(String str, String str2, Integer num, Map<String, ? extends Map<String, String>> map) {
        Intrinsics.checkParameterIsNotNull(str, "publicKey");
        return new MegolmBackupAuthData(str, str2, num, map);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x002e, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r2.signatures, (java.lang.Object) r3.signatures) != false) goto L_0x0033;
     */
    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof MegolmBackupAuthData) {
                MegolmBackupAuthData megolmBackupAuthData = (MegolmBackupAuthData) obj;
                if (Intrinsics.areEqual((Object) this.publicKey, (Object) megolmBackupAuthData.publicKey)) {
                    if (Intrinsics.areEqual((Object) this.privateKeySalt, (Object) megolmBackupAuthData.privateKeySalt)) {
                        if (Intrinsics.areEqual((Object) this.privateKeyIterations, (Object) megolmBackupAuthData.privateKeyIterations)) {
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        String str = this.publicKey;
        int i = 0;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        String str2 = this.privateKeySalt;
        int hashCode2 = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        Integer num = this.privateKeyIterations;
        int hashCode3 = (hashCode2 + (num != null ? num.hashCode() : 0)) * 31;
        Map<String, ? extends Map<String, String>> map = this.signatures;
        if (map != null) {
            i = map.hashCode();
        }
        return hashCode3 + i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MegolmBackupAuthData(publicKey=");
        sb.append(this.publicKey);
        sb.append(", privateKeySalt=");
        sb.append(this.privateKeySalt);
        sb.append(", privateKeyIterations=");
        sb.append(this.privateKeyIterations);
        sb.append(", signatures=");
        sb.append(this.signatures);
        sb.append(")");
        return sb.toString();
    }

    public MegolmBackupAuthData(String str, String str2, Integer num, Map<String, ? extends Map<String, String>> map) {
        Intrinsics.checkParameterIsNotNull(str, "publicKey");
        this.publicKey = str;
        this.privateKeySalt = str2;
        this.privateKeyIterations = num;
        this.signatures = map;
    }

    public final String getPublicKey() {
        return this.publicKey;
    }

    public final void setPublicKey(String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.publicKey = str;
    }

    public /* synthetic */ MegolmBackupAuthData(String str, String str2, Integer num, Map map, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 1) != 0) {
            str = "";
        }
        if ((i & 2) != 0) {
            str2 = null;
        }
        if ((i & 4) != 0) {
            num = null;
        }
        if ((i & 8) != 0) {
            map = null;
        }
        this(str, str2, num, map);
    }

    public final String getPrivateKeySalt() {
        return this.privateKeySalt;
    }

    public final void setPrivateKeySalt(String str) {
        this.privateKeySalt = str;
    }

    public final Integer getPrivateKeyIterations() {
        return this.privateKeyIterations;
    }

    public final void setPrivateKeyIterations(Integer num) {
        this.privateKeyIterations = num;
    }

    public final Map<String, Map<String, String>> getSignatures() {
        return this.signatures;
    }

    public final void setSignatures(Map<String, ? extends Map<String, String>> map) {
        this.signatures = map;
    }

    public final Map<String, Object> signalableJSONDictionary() {
        HashMap hashMap = new HashMap();
        hashMap.put("public_key", this.publicKey);
        String str = this.privateKeySalt;
        if (str != null) {
            hashMap.put("private_key_salt", str);
        }
        Integer num = this.privateKeyIterations;
        if (num != null) {
            hashMap.put("private_key_iterations", Integer.valueOf(num.intValue()));
        }
        return hashMap;
    }
}

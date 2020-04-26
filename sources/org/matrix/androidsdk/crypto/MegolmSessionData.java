package org.matrix.androidsdk.crypto;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010$\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\b\u0018\u00002\u00020\u0001Bw\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\u0016\b\u0002\u0010\b\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003\u0018\u00010\t\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0003\u0012\u0010\b\u0002\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\f¢\u0006\u0002\u0010\rJ\u000b\u0010\u000e\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u000f\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u0010\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u0011\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u0012\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u0017\u0010\u0013\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003\u0018\u00010\tHÆ\u0003J\u000b\u0010\u0014\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u0011\u0010\u0015\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\fHÆ\u0003J{\u0010\u0016\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00032\u0016\b\u0002\u0010\b\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003\u0018\u00010\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00032\u0010\b\u0002\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\fHÆ\u0001J\u0013\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001a\u001a\u00020\u001bHÖ\u0001J\t\u0010\u001c\u001a\u00020\u0003HÖ\u0001R\u0014\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\f8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R \u0010\b\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003\u0018\u00010\t8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Lorg/matrix/androidsdk/crypto/MegolmSessionData;", "", "algorithm", "", "sessionId", "senderKey", "roomId", "sessionKey", "senderClaimedKeys", "", "senderClaimedEd25519Key", "forwardingCurve25519KeyChain", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;Ljava/lang/String;Ljava/util/List;)V", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "", "other", "hashCode", "", "toString", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: MegolmSessionData.kt */
public final class MegolmSessionData {
    public String algorithm;
    @SerializedName("forwarding_curve25519_key_chain")
    public List<String> forwardingCurve25519KeyChain;
    @SerializedName("room_id")
    public String roomId;
    @SerializedName("sender_claimed_ed25519_key")
    public String senderClaimedEd25519Key;
    @SerializedName("sender_claimed_keys")
    public Map<String, String> senderClaimedKeys;
    @SerializedName("sender_key")
    public String senderKey;
    @SerializedName("session_id")
    public String sessionId;
    @SerializedName("session_key")
    public String sessionKey;

    public MegolmSessionData() {
        this(null, null, null, null, null, null, null, null, 255, null);
    }

    public static /* synthetic */ MegolmSessionData copy$default(MegolmSessionData megolmSessionData, String str, String str2, String str3, String str4, String str5, Map map, String str6, List list, int i, Object obj) {
        MegolmSessionData megolmSessionData2 = megolmSessionData;
        int i2 = i;
        return megolmSessionData.copy((i2 & 1) != 0 ? megolmSessionData2.algorithm : str, (i2 & 2) != 0 ? megolmSessionData2.sessionId : str2, (i2 & 4) != 0 ? megolmSessionData2.senderKey : str3, (i2 & 8) != 0 ? megolmSessionData2.roomId : str4, (i2 & 16) != 0 ? megolmSessionData2.sessionKey : str5, (i2 & 32) != 0 ? megolmSessionData2.senderClaimedKeys : map, (i2 & 64) != 0 ? megolmSessionData2.senderClaimedEd25519Key : str6, (i2 & 128) != 0 ? megolmSessionData2.forwardingCurve25519KeyChain : list);
    }

    public final String component1() {
        return this.algorithm;
    }

    public final String component2() {
        return this.sessionId;
    }

    public final String component3() {
        return this.senderKey;
    }

    public final String component4() {
        return this.roomId;
    }

    public final String component5() {
        return this.sessionKey;
    }

    public final Map<String, String> component6() {
        return this.senderClaimedKeys;
    }

    public final String component7() {
        return this.senderClaimedEd25519Key;
    }

    public final List<String> component8() {
        return this.forwardingCurve25519KeyChain;
    }

    public final MegolmSessionData copy(String str, String str2, String str3, String str4, String str5, Map<String, String> map, String str6, List<String> list) {
        MegolmSessionData megolmSessionData = new MegolmSessionData(str, str2, str3, str4, str5, map, str6, list);
        return megolmSessionData;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0056, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r2.forwardingCurve25519KeyChain, (java.lang.Object) r3.forwardingCurve25519KeyChain) != false) goto L_0x005b;
     */
    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof MegolmSessionData) {
                MegolmSessionData megolmSessionData = (MegolmSessionData) obj;
                if (Intrinsics.areEqual((Object) this.algorithm, (Object) megolmSessionData.algorithm)) {
                    if (Intrinsics.areEqual((Object) this.sessionId, (Object) megolmSessionData.sessionId)) {
                        if (Intrinsics.areEqual((Object) this.senderKey, (Object) megolmSessionData.senderKey)) {
                            if (Intrinsics.areEqual((Object) this.roomId, (Object) megolmSessionData.roomId)) {
                                if (Intrinsics.areEqual((Object) this.sessionKey, (Object) megolmSessionData.sessionKey)) {
                                    if (Intrinsics.areEqual((Object) this.senderClaimedKeys, (Object) megolmSessionData.senderClaimedKeys)) {
                                        if (Intrinsics.areEqual((Object) this.senderClaimedEd25519Key, (Object) megolmSessionData.senderClaimedEd25519Key)) {
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        String str = this.algorithm;
        int i = 0;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        String str2 = this.sessionId;
        int hashCode2 = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        String str3 = this.senderKey;
        int hashCode3 = (hashCode2 + (str3 != null ? str3.hashCode() : 0)) * 31;
        String str4 = this.roomId;
        int hashCode4 = (hashCode3 + (str4 != null ? str4.hashCode() : 0)) * 31;
        String str5 = this.sessionKey;
        int hashCode5 = (hashCode4 + (str5 != null ? str5.hashCode() : 0)) * 31;
        Map<String, String> map = this.senderClaimedKeys;
        int hashCode6 = (hashCode5 + (map != null ? map.hashCode() : 0)) * 31;
        String str6 = this.senderClaimedEd25519Key;
        int hashCode7 = (hashCode6 + (str6 != null ? str6.hashCode() : 0)) * 31;
        List<String> list = this.forwardingCurve25519KeyChain;
        if (list != null) {
            i = list.hashCode();
        }
        return hashCode7 + i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MegolmSessionData(algorithm=");
        sb.append(this.algorithm);
        sb.append(", sessionId=");
        sb.append(this.sessionId);
        sb.append(", senderKey=");
        sb.append(this.senderKey);
        sb.append(", roomId=");
        sb.append(this.roomId);
        sb.append(", sessionKey=");
        sb.append(this.sessionKey);
        sb.append(", senderClaimedKeys=");
        sb.append(this.senderClaimedKeys);
        sb.append(", senderClaimedEd25519Key=");
        sb.append(this.senderClaimedEd25519Key);
        sb.append(", forwardingCurve25519KeyChain=");
        sb.append(this.forwardingCurve25519KeyChain);
        sb.append(")");
        return sb.toString();
    }

    public MegolmSessionData(String str, String str2, String str3, String str4, String str5, Map<String, String> map, String str6, List<String> list) {
        this.algorithm = str;
        this.sessionId = str2;
        this.senderKey = str3;
        this.roomId = str4;
        this.sessionKey = str5;
        this.senderClaimedKeys = map;
        this.senderClaimedEd25519Key = str6;
        this.forwardingCurve25519KeyChain = list;
    }

    public /* synthetic */ MegolmSessionData(String str, String str2, String str3, String str4, String str5, Map map, String str6, List list, int i, DefaultConstructorMarker defaultConstructorMarker) {
        int i2 = i;
        String str7 = (i2 & 1) != 0 ? null : str;
        this(str7, (i2 & 2) != 0 ? null : str2, (i2 & 4) != 0 ? null : str3, (i2 & 8) != 0 ? null : str4, (i2 & 16) != 0 ? null : str5, (i2 & 32) != 0 ? null : map, (i2 & 64) != 0 ? null : str6, (i2 & 128) != 0 ? null : list);
    }
}

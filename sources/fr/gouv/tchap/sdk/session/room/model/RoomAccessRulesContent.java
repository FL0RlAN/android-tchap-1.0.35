package fr.gouv.tchap.sdk.session.room.model;

import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\b\u0018\u00002\u00020\u0001B\u0011\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0004J\u000b\u0010\u0005\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u0015\u0010\u0006\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003HÆ\u0001J\u0013\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\n\u001a\u00020\u000bHÖ\u0001J\t\u0010\f\u001a\u00020\u0003HÖ\u0001R\u0014\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lfr/gouv/tchap/sdk/session/room/model/RoomAccessRulesContent;", "", "rule", "", "(Ljava/lang/String;)V", "component1", "copy", "equals", "", "other", "hashCode", "", "toString", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: RoomAccessRules.kt */
public final class RoomAccessRulesContent {
    public String rule;

    public RoomAccessRulesContent() {
        this(null, 1, null);
    }

    public static /* synthetic */ RoomAccessRulesContent copy$default(RoomAccessRulesContent roomAccessRulesContent, String str, int i, Object obj) {
        if ((i & 1) != 0) {
            str = roomAccessRulesContent.rule;
        }
        return roomAccessRulesContent.copy(str);
    }

    public final String component1() {
        return this.rule;
    }

    public final RoomAccessRulesContent copy(String str) {
        return new RoomAccessRulesContent(str);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0010, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r1.rule, (java.lang.Object) ((fr.gouv.tchap.sdk.session.room.model.RoomAccessRulesContent) r2).rule) != false) goto L_0x0015;
     */
    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof RoomAccessRulesContent) {
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        String str = this.rule;
        if (str != null) {
            return str.hashCode();
        }
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RoomAccessRulesContent(rule=");
        sb.append(this.rule);
        sb.append(")");
        return sb.toString();
    }

    public RoomAccessRulesContent(String str) {
        this.rule = str;
    }

    public /* synthetic */ RoomAccessRulesContent(String str, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 1) != 0) {
            str = null;
        }
        this(str);
    }
}

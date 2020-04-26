package fr.gouv.tchap.sdk.session.room.model;

import com.google.gson.annotations.SerializedName;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u001d\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010\t\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\nJ\u0010\u0010\u000b\u001a\u0004\u0018\u00010\u0005HÆ\u0003¢\u0006\u0002\u0010\fJ&\u0010\r\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005HÆ\u0001¢\u0006\u0002\u0010\u000eJ\u0013\u0010\u000f\u001a\u00020\u00052\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001R\u0016\u0010\u0004\u001a\u0004\u0018\u00010\u00058\u0006@\u0006X\u000e¢\u0006\u0004\n\u0002\u0010\u0007R\u0016\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u000e¢\u0006\u0004\n\u0002\u0010\b¨\u0006\u0015"}, d2 = {"Lfr/gouv/tchap/sdk/session/room/model/RoomRetentionContent;", "", "maxLifetime", "", "expireOnClients", "", "(Ljava/lang/Long;Ljava/lang/Boolean;)V", "Ljava/lang/Boolean;", "Ljava/lang/Long;", "component1", "()Ljava/lang/Long;", "component2", "()Ljava/lang/Boolean;", "copy", "(Ljava/lang/Long;Ljava/lang/Boolean;)Lfr/gouv/tchap/sdk/session/room/model/RoomRetentionContent;", "equals", "other", "hashCode", "", "toString", "", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: RoomRetention.kt */
public final class RoomRetentionContent {
    @SerializedName("expire_on_clients")
    public Boolean expireOnClients;
    @SerializedName("max_lifetime")
    public Long maxLifetime;

    public RoomRetentionContent() {
        this(null, null, 3, null);
    }

    public static /* synthetic */ RoomRetentionContent copy$default(RoomRetentionContent roomRetentionContent, Long l, Boolean bool, int i, Object obj) {
        if ((i & 1) != 0) {
            l = roomRetentionContent.maxLifetime;
        }
        if ((i & 2) != 0) {
            bool = roomRetentionContent.expireOnClients;
        }
        return roomRetentionContent.copy(l, bool);
    }

    public final Long component1() {
        return this.maxLifetime;
    }

    public final Boolean component2() {
        return this.expireOnClients;
    }

    public final RoomRetentionContent copy(Long l, Boolean bool) {
        return new RoomRetentionContent(l, bool);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x001a, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r2.expireOnClients, (java.lang.Object) r3.expireOnClients) != false) goto L_0x001f;
     */
    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof RoomRetentionContent) {
                RoomRetentionContent roomRetentionContent = (RoomRetentionContent) obj;
                if (Intrinsics.areEqual((Object) this.maxLifetime, (Object) roomRetentionContent.maxLifetime)) {
                }
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        Long l = this.maxLifetime;
        int i = 0;
        int hashCode = (l != null ? l.hashCode() : 0) * 31;
        Boolean bool = this.expireOnClients;
        if (bool != null) {
            i = bool.hashCode();
        }
        return hashCode + i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RoomRetentionContent(maxLifetime=");
        sb.append(this.maxLifetime);
        sb.append(", expireOnClients=");
        sb.append(this.expireOnClients);
        sb.append(")");
        return sb.toString();
    }

    public RoomRetentionContent(Long l, Boolean bool) {
        this.maxLifetime = l;
        this.expireOnClients = bool;
    }

    public /* synthetic */ RoomRetentionContent(Long l, Boolean bool, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 1) != 0) {
            l = null;
        }
        if ((i & 2) != 0) {
            bool = null;
        }
        this(l, bool);
    }
}

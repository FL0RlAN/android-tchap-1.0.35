package org.matrix.androidsdk.crypto.interfaces;

import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\bf\u0018\u0000 \u00072\u00020\u0001:\u0001\u0007J\b\u0010\u0006\u001a\u00020\u0003H&R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005¨\u0006\b"}, d2 = {"Lorg/matrix/androidsdk/crypto/interfaces/CryptoRoomMember;", "", "userId", "", "getUserId", "()Ljava/lang/String;", "getMembership", "Companion", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: CryptoRoomMember.kt */
public interface CryptoRoomMember {
    public static final Companion Companion = Companion.$$INSTANCE;
    public static final String MEMBERSHIP_BAN = "ban";
    public static final String MEMBERSHIP_INVITE = "invite";
    public static final String MEMBERSHIP_JOIN = "join";
    public static final String MEMBERSHIP_LEAVE = "leave";

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\b"}, d2 = {"Lorg/matrix/androidsdk/crypto/interfaces/CryptoRoomMember$Companion;", "", "()V", "MEMBERSHIP_BAN", "", "MEMBERSHIP_INVITE", "MEMBERSHIP_JOIN", "MEMBERSHIP_LEAVE", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: CryptoRoomMember.kt */
    public static final class Companion {
        static final /* synthetic */ Companion $$INSTANCE = new Companion();
        public static final String MEMBERSHIP_BAN = "ban";
        public static final String MEMBERSHIP_INVITE = "invite";
        public static final String MEMBERSHIP_JOIN = "join";
        public static final String MEMBERSHIP_LEAVE = "leave";

        private Companion() {
        }
    }

    String getMembership();

    String getUserId();
}

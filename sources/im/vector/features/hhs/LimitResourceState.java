package im.vector.features.hhs;

import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntityFields;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0002\u0005\u0006B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u0001\u0002\u0007\b¨\u0006\t"}, d2 = {"Lim/vector/features/hhs/LimitResourceState;", "", "()V", "softErrorOrNull", "Lorg/matrix/androidsdk/core/model/MatrixError;", "Exceeded", "Normal", "Lim/vector/features/hhs/LimitResourceState$Normal;", "Lim/vector/features/hhs/LimitResourceState$Exceeded;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: ResourceLimitEventListener.kt */
public abstract class LimitResourceState {

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\t\u0010\r\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000f\u001a\u00020\u0006HÆ\u0003J'\u0010\u0010\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0006HÆ\u0001J\u0013\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014HÖ\u0003J\t\u0010\u0015\u001a\u00020\u0016HÖ\u0001J\t\u0010\u0017\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\t¨\u0006\u0018"}, d2 = {"Lim/vector/features/hhs/LimitResourceState$Exceeded;", "Lim/vector/features/hhs/LimitResourceState;", "roomId", "", "eventId", "matrixError", "Lorg/matrix/androidsdk/core/model/MatrixError;", "(Ljava/lang/String;Ljava/lang/String;Lorg/matrix/androidsdk/core/model/MatrixError;)V", "getEventId", "()Ljava/lang/String;", "getMatrixError", "()Lorg/matrix/androidsdk/core/model/MatrixError;", "getRoomId", "component1", "component2", "component3", "copy", "equals", "", "other", "", "hashCode", "", "toString", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: ResourceLimitEventListener.kt */
    public static final class Exceeded extends LimitResourceState {
        private final String eventId;
        private final MatrixError matrixError;
        private final String roomId;

        public static /* synthetic */ Exceeded copy$default(Exceeded exceeded, String str, String str2, MatrixError matrixError2, int i, Object obj) {
            if ((i & 1) != 0) {
                str = exceeded.roomId;
            }
            if ((i & 2) != 0) {
                str2 = exceeded.eventId;
            }
            if ((i & 4) != 0) {
                matrixError2 = exceeded.matrixError;
            }
            return exceeded.copy(str, str2, matrixError2);
        }

        public final String component1() {
            return this.roomId;
        }

        public final String component2() {
            return this.eventId;
        }

        public final MatrixError component3() {
            return this.matrixError;
        }

        public final Exceeded copy(String str, String str2, MatrixError matrixError2) {
            Intrinsics.checkParameterIsNotNull(str, CryptoRoomEntityFields.ROOM_ID);
            Intrinsics.checkParameterIsNotNull(str2, "eventId");
            Intrinsics.checkParameterIsNotNull(matrixError2, "matrixError");
            return new Exceeded(str, str2, matrixError2);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:8:0x0024, code lost:
            if (kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r2.matrixError, (java.lang.Object) r3.matrixError) != false) goto L_0x0029;
         */
        public boolean equals(Object obj) {
            if (this != obj) {
                if (obj instanceof Exceeded) {
                    Exceeded exceeded = (Exceeded) obj;
                    if (Intrinsics.areEqual((Object) this.roomId, (Object) exceeded.roomId)) {
                        if (Intrinsics.areEqual((Object) this.eventId, (Object) exceeded.eventId)) {
                        }
                    }
                }
                return false;
            }
            return true;
        }

        public int hashCode() {
            String str = this.roomId;
            int i = 0;
            int hashCode = (str != null ? str.hashCode() : 0) * 31;
            String str2 = this.eventId;
            int hashCode2 = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
            MatrixError matrixError2 = this.matrixError;
            if (matrixError2 != null) {
                i = matrixError2.hashCode();
            }
            return hashCode2 + i;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Exceeded(roomId=");
            sb.append(this.roomId);
            sb.append(", eventId=");
            sb.append(this.eventId);
            sb.append(", matrixError=");
            sb.append(this.matrixError);
            sb.append(")");
            return sb.toString();
        }

        public Exceeded(String str, String str2, MatrixError matrixError2) {
            Intrinsics.checkParameterIsNotNull(str, CryptoRoomEntityFields.ROOM_ID);
            Intrinsics.checkParameterIsNotNull(str2, "eventId");
            Intrinsics.checkParameterIsNotNull(matrixError2, "matrixError");
            super(null);
            this.roomId = str;
            this.eventId = str2;
            this.matrixError = matrixError2;
        }

        public final String getEventId() {
            return this.eventId;
        }

        public final MatrixError getMatrixError() {
            return this.matrixError;
        }

        public final String getRoomId() {
            return this.roomId;
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/features/hhs/LimitResourceState$Normal;", "Lim/vector/features/hhs/LimitResourceState;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: ResourceLimitEventListener.kt */
    public static final class Normal extends LimitResourceState {
        public static final Normal INSTANCE = new Normal();

        private Normal() {
            super(null);
        }
    }

    private LimitResourceState() {
    }

    public /* synthetic */ LimitResourceState(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    public final MatrixError softErrorOrNull() {
        if (this instanceof Normal) {
            return null;
        }
        if (this instanceof Exceeded) {
            return ((Exceeded) this).getMatrixError();
        }
        throw new NoWhenBranchMatchedException();
    }
}

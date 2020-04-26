package im.vector.analytics;

import im.vector.analytics.e2e.DecryptionFailureReason;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0006\u0014\u0015\u0016\u0017\u0018\u0019B/\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t¢\u0006\u0002\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0015\u0010\b\u001a\u0004\u0018\u00010\t¢\u0006\n\n\u0002\u0010\u0013\u001a\u0004\b\u0011\u0010\u0012\u0001\u0006\u001a\u001b\u001c\u001d\u001e\u001f¨\u0006 "}, d2 = {"Lim/vector/analytics/TrackingEvent;", "", "category", "Lim/vector/analytics/Category;", "action", "Lim/vector/analytics/Action;", "title", "", "value", "", "(Lim/vector/analytics/Category;Lim/vector/analytics/Action;Ljava/lang/String;Ljava/lang/Float;)V", "getAction", "()Lim/vector/analytics/Action;", "getCategory", "()Lim/vector/analytics/Category;", "getTitle", "()Ljava/lang/String;", "getValue", "()Ljava/lang/Float;", "Ljava/lang/Float;", "DecryptionFailure", "IncrementalSync", "InitialSync", "LaunchScreen", "Rooms", "StorePreload", "Lim/vector/analytics/TrackingEvent$InitialSync;", "Lim/vector/analytics/TrackingEvent$IncrementalSync;", "Lim/vector/analytics/TrackingEvent$StorePreload;", "Lim/vector/analytics/TrackingEvent$LaunchScreen;", "Lim/vector/analytics/TrackingEvent$Rooms;", "Lim/vector/analytics/TrackingEvent$DecryptionFailure;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: Events.kt */
public abstract class TrackingEvent {
    private final Action action;
    private final Category category;
    private final String title;
    private final Float value;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u0007\u001a\u00020\u0003HÂ\u0003J\t\u0010\b\u001a\u00020\u0005HÂ\u0003J\u001d\u0010\t\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rHÖ\u0003J\t\u0010\u000e\u001a\u00020\u0005HÖ\u0001J\t\u0010\u000f\u001a\u00020\u0010HÖ\u0001R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lim/vector/analytics/TrackingEvent$DecryptionFailure;", "Lim/vector/analytics/TrackingEvent;", "reason", "Lim/vector/analytics/e2e/DecryptionFailureReason;", "failureCount", "", "(Lim/vector/analytics/e2e/DecryptionFailureReason;I)V", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "toString", "", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: Events.kt */
    public static final class DecryptionFailure extends TrackingEvent {
        private final int failureCount;
        private final DecryptionFailureReason reason;

        private final DecryptionFailureReason component1() {
            return this.reason;
        }

        private final int component2() {
            return this.failureCount;
        }

        public static /* synthetic */ DecryptionFailure copy$default(DecryptionFailure decryptionFailure, DecryptionFailureReason decryptionFailureReason, int i, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                decryptionFailureReason = decryptionFailure.reason;
            }
            if ((i2 & 2) != 0) {
                i = decryptionFailure.failureCount;
            }
            return decryptionFailure.copy(decryptionFailureReason, i);
        }

        public final DecryptionFailure copy(DecryptionFailureReason decryptionFailureReason, int i) {
            Intrinsics.checkParameterIsNotNull(decryptionFailureReason, "reason");
            return new DecryptionFailure(decryptionFailureReason, i);
        }

        public boolean equals(Object obj) {
            if (this != obj) {
                if (obj instanceof DecryptionFailure) {
                    DecryptionFailure decryptionFailure = (DecryptionFailure) obj;
                    if (Intrinsics.areEqual((Object) this.reason, (Object) decryptionFailure.reason)) {
                        if (this.failureCount == decryptionFailure.failureCount) {
                            return true;
                        }
                    }
                }
                return false;
            }
            return true;
        }

        public int hashCode() {
            DecryptionFailureReason decryptionFailureReason = this.reason;
            return ((decryptionFailureReason != null ? decryptionFailureReason.hashCode() : 0) * 31) + this.failureCount;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("DecryptionFailure(reason=");
            sb.append(this.reason);
            sb.append(", failureCount=");
            sb.append(this.failureCount);
            sb.append(")");
            return sb.toString();
        }

        public DecryptionFailure(DecryptionFailureReason decryptionFailureReason, int i) {
            Intrinsics.checkParameterIsNotNull(decryptionFailureReason, "reason");
            super(Category.E2E, Action.DECRYPTION_FAILURE, decryptionFailureReason.getValue(), Float.valueOf((float) i), null);
            this.reason = decryptionFailureReason;
            this.failureCount = i;
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fHÖ\u0003J\t\u0010\r\u001a\u00020\u000eHÖ\u0001J\t\u0010\u000f\u001a\u00020\u0010HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0011"}, d2 = {"Lim/vector/analytics/TrackingEvent$IncrementalSync;", "Lim/vector/analytics/TrackingEvent;", "duration", "", "(J)V", "getDuration", "()J", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: Events.kt */
    public static final class IncrementalSync extends TrackingEvent {
        private final long duration;

        public static /* synthetic */ IncrementalSync copy$default(IncrementalSync incrementalSync, long j, int i, Object obj) {
            if ((i & 1) != 0) {
                j = incrementalSync.duration;
            }
            return incrementalSync.copy(j);
        }

        public final long component1() {
            return this.duration;
        }

        public final IncrementalSync copy(long j) {
            return new IncrementalSync(j);
        }

        public boolean equals(Object obj) {
            if (this != obj) {
                if (obj instanceof IncrementalSync) {
                    if (this.duration == ((IncrementalSync) obj).duration) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }

        public int hashCode() {
            long j = this.duration;
            return (int) (j ^ (j >>> 32));
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("IncrementalSync(duration=");
            sb.append(this.duration);
            sb.append(")");
            return sb.toString();
        }

        public IncrementalSync(long j) {
            super(Category.METRICS, Action.STARTUP, "incrementalSync", Float.valueOf((float) j), null);
            this.duration = j;
        }

        public final long getDuration() {
            return this.duration;
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fHÖ\u0003J\t\u0010\r\u001a\u00020\u000eHÖ\u0001J\t\u0010\u000f\u001a\u00020\u0010HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0011"}, d2 = {"Lim/vector/analytics/TrackingEvent$InitialSync;", "Lim/vector/analytics/TrackingEvent;", "duration", "", "(J)V", "getDuration", "()J", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: Events.kt */
    public static final class InitialSync extends TrackingEvent {
        private final long duration;

        public static /* synthetic */ InitialSync copy$default(InitialSync initialSync, long j, int i, Object obj) {
            if ((i & 1) != 0) {
                j = initialSync.duration;
            }
            return initialSync.copy(j);
        }

        public final long component1() {
            return this.duration;
        }

        public final InitialSync copy(long j) {
            return new InitialSync(j);
        }

        public boolean equals(Object obj) {
            if (this != obj) {
                if (obj instanceof InitialSync) {
                    if (this.duration == ((InitialSync) obj).duration) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }

        public int hashCode() {
            long j = this.duration;
            return (int) (j ^ (j >>> 32));
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("InitialSync(duration=");
            sb.append(this.duration);
            sb.append(")");
            return sb.toString();
        }

        public InitialSync(long j) {
            super(Category.METRICS, Action.STARTUP, "initialSync", Float.valueOf((float) j), null);
            this.duration = j;
        }

        public final long getDuration() {
            return this.duration;
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fHÖ\u0003J\t\u0010\r\u001a\u00020\u000eHÖ\u0001J\t\u0010\u000f\u001a\u00020\u0010HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0011"}, d2 = {"Lim/vector/analytics/TrackingEvent$LaunchScreen;", "Lim/vector/analytics/TrackingEvent;", "duration", "", "(J)V", "getDuration", "()J", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: Events.kt */
    public static final class LaunchScreen extends TrackingEvent {
        private final long duration;

        public static /* synthetic */ LaunchScreen copy$default(LaunchScreen launchScreen, long j, int i, Object obj) {
            if ((i & 1) != 0) {
                j = launchScreen.duration;
            }
            return launchScreen.copy(j);
        }

        public final long component1() {
            return this.duration;
        }

        public final LaunchScreen copy(long j) {
            return new LaunchScreen(j);
        }

        public boolean equals(Object obj) {
            if (this != obj) {
                if (obj instanceof LaunchScreen) {
                    if (this.duration == ((LaunchScreen) obj).duration) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }

        public int hashCode() {
            long j = this.duration;
            return (int) (j ^ (j >>> 32));
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("LaunchScreen(duration=");
            sb.append(this.duration);
            sb.append(")");
            return sb.toString();
        }

        public LaunchScreen(long j) {
            super(Category.METRICS, Action.STARTUP, "launchScreen", Float.valueOf((float) j), null);
            this.duration = j;
        }

        public final long getDuration() {
            return this.duration;
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fHÖ\u0003J\t\u0010\r\u001a\u00020\u0003HÖ\u0001J\t\u0010\u000e\u001a\u00020\u000fHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0010"}, d2 = {"Lim/vector/analytics/TrackingEvent$Rooms;", "Lim/vector/analytics/TrackingEvent;", "nbOfRooms", "", "(I)V", "getNbOfRooms", "()I", "component1", "copy", "equals", "", "other", "", "hashCode", "toString", "", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: Events.kt */
    public static final class Rooms extends TrackingEvent {
        private final int nbOfRooms;

        public static /* synthetic */ Rooms copy$default(Rooms rooms, int i, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                i = rooms.nbOfRooms;
            }
            return rooms.copy(i);
        }

        public final int component1() {
            return this.nbOfRooms;
        }

        public final Rooms copy(int i) {
            return new Rooms(i);
        }

        public boolean equals(Object obj) {
            if (this != obj) {
                if (obj instanceof Rooms) {
                    if (this.nbOfRooms == ((Rooms) obj).nbOfRooms) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }

        public int hashCode() {
            return this.nbOfRooms;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Rooms(nbOfRooms=");
            sb.append(this.nbOfRooms);
            sb.append(")");
            return sb.toString();
        }

        public Rooms(int i) {
            super(Category.METRICS, Action.STATS, "rooms", Float.valueOf((float) i), null);
            this.nbOfRooms = i;
        }

        public final int getNbOfRooms() {
            return this.nbOfRooms;
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fHÖ\u0003J\t\u0010\r\u001a\u00020\u000eHÖ\u0001J\t\u0010\u000f\u001a\u00020\u0010HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0011"}, d2 = {"Lim/vector/analytics/TrackingEvent$StorePreload;", "Lim/vector/analytics/TrackingEvent;", "duration", "", "(J)V", "getDuration", "()J", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: Events.kt */
    public static final class StorePreload extends TrackingEvent {
        private final long duration;

        public static /* synthetic */ StorePreload copy$default(StorePreload storePreload, long j, int i, Object obj) {
            if ((i & 1) != 0) {
                j = storePreload.duration;
            }
            return storePreload.copy(j);
        }

        public final long component1() {
            return this.duration;
        }

        public final StorePreload copy(long j) {
            return new StorePreload(j);
        }

        public boolean equals(Object obj) {
            if (this != obj) {
                if (obj instanceof StorePreload) {
                    if (this.duration == ((StorePreload) obj).duration) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }

        public int hashCode() {
            long j = this.duration;
            return (int) (j ^ (j >>> 32));
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("StorePreload(duration=");
            sb.append(this.duration);
            sb.append(")");
            return sb.toString();
        }

        public StorePreload(long j) {
            super(Category.METRICS, Action.STARTUP, "storePreload", Float.valueOf((float) j), null);
            this.duration = j;
        }

        public final long getDuration() {
            return this.duration;
        }
    }

    private TrackingEvent(Category category2, Action action2, String str, Float f) {
        this.category = category2;
        this.action = action2;
        this.title = str;
        this.value = f;
    }

    /* synthetic */ TrackingEvent(Category category2, Action action2, String str, Float f, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 4) != 0) {
            str = null;
        }
        if ((i & 8) != 0) {
            f = null;
        }
        this(category2, action2, str, f);
    }

    public /* synthetic */ TrackingEvent(Category category2, Action action2, String str, Float f, DefaultConstructorMarker defaultConstructorMarker) {
        this(category2, action2, str, f);
    }

    public final Action getAction() {
        return this.action;
    }

    public final Category getCategory() {
        return this.category;
    }

    public final String getTitle() {
        return this.title;
    }

    public final Float getValue() {
        return this.value;
    }
}

package fr.gouv.tchap.version;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0003\u0003\u0004\u0005B\u0007\b\u0002¢\u0006\u0002\u0010\u0002\u0001\u0003\u0006\u0007\b¨\u0006\t"}, d2 = {"Lfr/gouv/tchap/version/VersionCheckResult;", "", "()V", "Ok", "ShowUpgradeScreen", "Unknown", "Lfr/gouv/tchap/version/VersionCheckResult$Unknown;", "Lfr/gouv/tchap/version/VersionCheckResult$Ok;", "Lfr/gouv/tchap/version/VersionCheckResult$ShowUpgradeScreen;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VersionCheckResult.kt */
public abstract class VersionCheckResult {

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lfr/gouv/tchap/version/VersionCheckResult$Ok;", "Lfr/gouv/tchap/version/VersionCheckResult;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: VersionCheckResult.kt */
    public static final class Ok extends VersionCheckResult {
        public static final Ok INSTANCE = new Ok();

        private Ok() {
            super(null);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0010\n\u0002\u0010\u0000\n\u0002\b\u0003\b\b\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0007¢\u0006\u0002\u0010\tJ\t\u0010\u0011\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0013\u001a\u00020\u0007HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0007HÆ\u0003J1\u0010\u0015\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u0007HÆ\u0001J\u0013\u0010\u0016\u001a\u00020\u00072\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018HÖ\u0003J\t\u0010\u0019\u001a\u00020\u0005HÖ\u0001J\t\u0010\u001a\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\b\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010¨\u0006\u001b"}, d2 = {"Lfr/gouv/tchap/version/VersionCheckResult$ShowUpgradeScreen;", "Lfr/gouv/tchap/version/VersionCheckResult;", "message", "", "forVersionCode", "", "displayOnlyOnce", "", "canOpenApp", "(Ljava/lang/String;IZZ)V", "getCanOpenApp", "()Z", "getDisplayOnlyOnce", "getForVersionCode", "()I", "getMessage", "()Ljava/lang/String;", "component1", "component2", "component3", "component4", "copy", "equals", "other", "", "hashCode", "toString", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: VersionCheckResult.kt */
    public static final class ShowUpgradeScreen extends VersionCheckResult {
        private final boolean canOpenApp;
        private final boolean displayOnlyOnce;
        private final int forVersionCode;
        private final String message;

        public static /* synthetic */ ShowUpgradeScreen copy$default(ShowUpgradeScreen showUpgradeScreen, String str, int i, boolean z, boolean z2, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                str = showUpgradeScreen.message;
            }
            if ((i2 & 2) != 0) {
                i = showUpgradeScreen.forVersionCode;
            }
            if ((i2 & 4) != 0) {
                z = showUpgradeScreen.displayOnlyOnce;
            }
            if ((i2 & 8) != 0) {
                z2 = showUpgradeScreen.canOpenApp;
            }
            return showUpgradeScreen.copy(str, i, z, z2);
        }

        public final String component1() {
            return this.message;
        }

        public final int component2() {
            return this.forVersionCode;
        }

        public final boolean component3() {
            return this.displayOnlyOnce;
        }

        public final boolean component4() {
            return this.canOpenApp;
        }

        public final ShowUpgradeScreen copy(String str, int i, boolean z, boolean z2) {
            Intrinsics.checkParameterIsNotNull(str, "message");
            return new ShowUpgradeScreen(str, i, z, z2);
        }

        public boolean equals(Object obj) {
            if (this != obj) {
                if (obj instanceof ShowUpgradeScreen) {
                    ShowUpgradeScreen showUpgradeScreen = (ShowUpgradeScreen) obj;
                    if (Intrinsics.areEqual((Object) this.message, (Object) showUpgradeScreen.message)) {
                        if (this.forVersionCode == showUpgradeScreen.forVersionCode) {
                            if (this.displayOnlyOnce == showUpgradeScreen.displayOnlyOnce) {
                                if (this.canOpenApp == showUpgradeScreen.canOpenApp) {
                                    return true;
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
            String str = this.message;
            int hashCode = (((str != null ? str.hashCode() : 0) * 31) + this.forVersionCode) * 31;
            boolean z = this.displayOnlyOnce;
            if (z) {
                z = true;
            }
            int i = (hashCode + (z ? 1 : 0)) * 31;
            boolean z2 = this.canOpenApp;
            if (z2) {
                z2 = true;
            }
            return i + (z2 ? 1 : 0);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("ShowUpgradeScreen(message=");
            sb.append(this.message);
            sb.append(", forVersionCode=");
            sb.append(this.forVersionCode);
            sb.append(", displayOnlyOnce=");
            sb.append(this.displayOnlyOnce);
            sb.append(", canOpenApp=");
            sb.append(this.canOpenApp);
            sb.append(")");
            return sb.toString();
        }

        public final String getMessage() {
            return this.message;
        }

        public final int getForVersionCode() {
            return this.forVersionCode;
        }

        public final boolean getDisplayOnlyOnce() {
            return this.displayOnlyOnce;
        }

        public ShowUpgradeScreen(String str, int i, boolean z, boolean z2) {
            Intrinsics.checkParameterIsNotNull(str, "message");
            super(null);
            this.message = str;
            this.forVersionCode = i;
            this.displayOnlyOnce = z;
            this.canOpenApp = z2;
        }

        public final boolean getCanOpenApp() {
            return this.canOpenApp;
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lfr/gouv/tchap/version/VersionCheckResult$Unknown;", "Lfr/gouv/tchap/version/VersionCheckResult;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: VersionCheckResult.kt */
    public static final class Unknown extends VersionCheckResult {
        public static final Unknown INSTANCE = new Unknown();

        private Unknown() {
            super(null);
        }
    }

    private VersionCheckResult() {
    }

    public /* synthetic */ VersionCheckResult(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }
}

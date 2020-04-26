package org.matrix.androidsdk.login;

import android.net.Uri;
import java.net.MalformedURLException;
import java.net.URL;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.HomeServerConnectionConfig.Builder;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.rest.client.IdentityPingRestClient;
import org.matrix.androidsdk.rest.client.LoginRestClient;
import org.matrix.androidsdk.rest.client.WellKnownRestClient;
import org.matrix.androidsdk.rest.model.WellKnown;
import org.matrix.androidsdk.rest.model.WellKnownBaseConfig;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0002\u0013\u0014B\u0005¢\u0006\u0002\u0010\u0002J\u001c\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nJ\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\bH\u0002J\u001e\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u00112\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0002J\u001e\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u00112\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lorg/matrix/androidsdk/login/AutoDiscovery;", "", "()V", "wellKnownRestClient", "Lorg/matrix/androidsdk/rest/client/WellKnownRestClient;", "findClientConfig", "", "domain", "", "callback", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "Lorg/matrix/androidsdk/login/AutoDiscovery$DiscoveredClientConfig;", "isValidURL", "", "url", "validateHomeServerAndProceed", "wellKnown", "Lorg/matrix/androidsdk/rest/model/WellKnown;", "validateIdentityServerAndFinish", "Action", "DiscoveredClientConfig", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: AutoDiscovery.kt */
public final class AutoDiscovery {
    private final WellKnownRestClient wellKnownRestClient = new WellKnownRestClient();

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006¨\u0006\u0007"}, d2 = {"Lorg/matrix/androidsdk/login/AutoDiscovery$Action;", "", "(Ljava/lang/String;I)V", "PROMPT", "IGNORE", "FAIL_PROMPT", "FAIL_ERROR", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: AutoDiscovery.kt */
    public enum Action {
        PROMPT,
        IGNORE,
        FAIL_PROMPT,
        FAIL_ERROR
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0019\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u001f\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0015"}, d2 = {"Lorg/matrix/androidsdk/login/AutoDiscovery$DiscoveredClientConfig;", "", "action", "Lorg/matrix/androidsdk/login/AutoDiscovery$Action;", "wellKnown", "Lorg/matrix/androidsdk/rest/model/WellKnown;", "(Lorg/matrix/androidsdk/login/AutoDiscovery$Action;Lorg/matrix/androidsdk/rest/model/WellKnown;)V", "getAction", "()Lorg/matrix/androidsdk/login/AutoDiscovery$Action;", "getWellKnown", "()Lorg/matrix/androidsdk/rest/model/WellKnown;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: AutoDiscovery.kt */
    public static final class DiscoveredClientConfig {
        private final Action action;
        private final WellKnown wellKnown;

        public static /* synthetic */ DiscoveredClientConfig copy$default(DiscoveredClientConfig discoveredClientConfig, Action action2, WellKnown wellKnown2, int i, Object obj) {
            if ((i & 1) != 0) {
                action2 = discoveredClientConfig.action;
            }
            if ((i & 2) != 0) {
                wellKnown2 = discoveredClientConfig.wellKnown;
            }
            return discoveredClientConfig.copy(action2, wellKnown2);
        }

        public final Action component1() {
            return this.action;
        }

        public final WellKnown component2() {
            return this.wellKnown;
        }

        public final DiscoveredClientConfig copy(Action action2, WellKnown wellKnown2) {
            Intrinsics.checkParameterIsNotNull(action2, "action");
            return new DiscoveredClientConfig(action2, wellKnown2);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:6:0x001a, code lost:
            if (kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r2.wellKnown, (java.lang.Object) r3.wellKnown) != false) goto L_0x001f;
         */
        public boolean equals(Object obj) {
            if (this != obj) {
                if (obj instanceof DiscoveredClientConfig) {
                    DiscoveredClientConfig discoveredClientConfig = (DiscoveredClientConfig) obj;
                    if (Intrinsics.areEqual((Object) this.action, (Object) discoveredClientConfig.action)) {
                    }
                }
                return false;
            }
            return true;
        }

        public int hashCode() {
            Action action2 = this.action;
            int i = 0;
            int hashCode = (action2 != null ? action2.hashCode() : 0) * 31;
            WellKnown wellKnown2 = this.wellKnown;
            if (wellKnown2 != null) {
                i = wellKnown2.hashCode();
            }
            return hashCode + i;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("DiscoveredClientConfig(action=");
            sb.append(this.action);
            sb.append(", wellKnown=");
            sb.append(this.wellKnown);
            sb.append(")");
            return sb.toString();
        }

        public DiscoveredClientConfig(Action action2, WellKnown wellKnown2) {
            Intrinsics.checkParameterIsNotNull(action2, "action");
            this.action = action2;
            this.wellKnown = wellKnown2;
        }

        public final Action getAction() {
            return this.action;
        }

        public /* synthetic */ DiscoveredClientConfig(Action action2, WellKnown wellKnown2, int i, DefaultConstructorMarker defaultConstructorMarker) {
            if ((i & 2) != 0) {
                wellKnown2 = null;
            }
            this(action2, wellKnown2);
        }

        public final WellKnown getWellKnown() {
            return this.wellKnown;
        }
    }

    public final void findClientConfig(String str, ApiCallback<DiscoveredClientConfig> apiCallback) {
        Intrinsics.checkParameterIsNotNull(str, "domain");
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        this.wellKnownRestClient.getWellKnown(str, new AutoDiscovery$findClientConfig$1(this, apiCallback, apiCallback));
    }

    /* access modifiers changed from: private */
    public final void validateHomeServerAndProceed(WellKnown wellKnown, ApiCallback<DiscoveredClientConfig> apiCallback) {
        Builder builder = new Builder();
        WellKnownBaseConfig wellKnownBaseConfig = wellKnown.homeServer;
        if (wellKnownBaseConfig == null) {
            Intrinsics.throwNpe();
        }
        String str = wellKnownBaseConfig.baseURL;
        if (str == null) {
            Intrinsics.throwNpe();
        }
        new LoginRestClient(builder.withHomeServerUri(Uri.parse(str)).build()).getVersions(new AutoDiscovery$validateHomeServerAndProceed$1(this, wellKnown, apiCallback));
    }

    /* access modifiers changed from: private */
    public final void validateIdentityServerAndFinish(WellKnown wellKnown, ApiCallback<DiscoveredClientConfig> apiCallback) {
        Builder builder = new Builder();
        WellKnownBaseConfig wellKnownBaseConfig = wellKnown.homeServer;
        if (wellKnownBaseConfig == null) {
            Intrinsics.throwNpe();
        }
        String str = wellKnownBaseConfig.baseURL;
        if (str == null) {
            Intrinsics.throwNpe();
        }
        Builder withHomeServerUri = builder.withHomeServerUri(Uri.parse(str));
        WellKnownBaseConfig wellKnownBaseConfig2 = wellKnown.identityServer;
        if (wellKnownBaseConfig2 == null) {
            Intrinsics.throwNpe();
        }
        String str2 = wellKnownBaseConfig2.baseURL;
        if (str2 == null) {
            Intrinsics.throwNpe();
        }
        HomeServerConnectionConfig build = withHomeServerUri.withIdentityServerUri(Uri.parse(str2)).build();
        Intrinsics.checkExpressionValueIsNotNull(build, "hsConfig");
        new IdentityPingRestClient(build).ping(new AutoDiscovery$validateIdentityServerAndFinish$1(apiCallback, wellKnown));
    }

    /* access modifiers changed from: private */
    public final boolean isValidURL(String str) {
        try {
            new URL(str);
            return true;
        } catch (MalformedURLException unused) {
            return false;
        }
    }
}

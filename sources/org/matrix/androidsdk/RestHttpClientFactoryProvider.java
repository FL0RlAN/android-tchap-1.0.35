package org.matrix.androidsdk;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0004"}, d2 = {"Lorg/matrix/androidsdk/RestHttpClientFactoryProvider;", "", "()V", "Companion", "matrix-sdk-core_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: RestHttpClientFactoryProvider.kt */
public final class RestHttpClientFactoryProvider {
    public static final Companion Companion = new Companion(null);
    /* access modifiers changed from: private */
    public static RestClientHttpClientFactory defaultProvider = new RestClientHttpClientFactory(null, 1, null);

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b¨\u0006\t"}, d2 = {"Lorg/matrix/androidsdk/RestHttpClientFactoryProvider$Companion;", "", "()V", "defaultProvider", "Lorg/matrix/androidsdk/RestClientHttpClientFactory;", "getDefaultProvider", "()Lorg/matrix/androidsdk/RestClientHttpClientFactory;", "setDefaultProvider", "(Lorg/matrix/androidsdk/RestClientHttpClientFactory;)V", "matrix-sdk-core_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: RestHttpClientFactoryProvider.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final RestClientHttpClientFactory getDefaultProvider() {
            return RestHttpClientFactoryProvider.defaultProvider;
        }

        public final void setDefaultProvider(RestClientHttpClientFactory restClientHttpClientFactory) {
            Intrinsics.checkParameterIsNotNull(restClientHttpClientFactory, "<set-?>");
            RestHttpClientFactoryProvider.defaultProvider = restClientHttpClientFactory;
        }
    }
}

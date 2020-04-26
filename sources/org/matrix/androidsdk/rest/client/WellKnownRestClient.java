package org.matrix.androidsdk.rest.client;

import android.net.Uri;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.HomeServerConnectionConfig.Builder;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.rest.DefaultRetrofit2CallbackWrapper;
import org.matrix.androidsdk.rest.api.WellKnownAPI;
import org.matrix.androidsdk.rest.model.WellKnown;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0003J\u001c\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t¨\u0006\u000b"}, d2 = {"Lorg/matrix/androidsdk/rest/client/WellKnownRestClient;", "Lorg/matrix/androidsdk/RestClient;", "Lorg/matrix/androidsdk/rest/api/WellKnownAPI;", "()V", "getWellKnown", "", "domain", "", "callback", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "Lorg/matrix/androidsdk/rest/model/WellKnown;", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: WellKnownRestClient.kt */
public final class WellKnownRestClient extends RestClient<WellKnownAPI> {
    public WellKnownRestClient() {
        super(new Builder().withHomeServerUri(Uri.parse("https://foo.bar")).build(), WellKnownAPI.class, "");
    }

    public final void getWellKnown(String str, ApiCallback<WellKnown> apiCallback) {
        Intrinsics.checkParameterIsNotNull(str, "domain");
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        ((WellKnownAPI) this.mApi).getWellKnown(str).enqueue(new DefaultRetrofit2CallbackWrapper(apiCallback));
    }
}

package fr.gouv.tchap.util;

import android.net.Uri;
import android.os.Build.VERSION;
import fr.gouv.tchap.config.PinningConfigurationKt;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.HomeServerConnectionConfig.Builder;
import org.matrix.androidsdk.ssl.Fingerprint;
import org.matrix.androidsdk.ssl.Fingerprint.HashType;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0002\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u001a\u000e\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001\u001a\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0003H\u0007¨\u0006\t"}, d2 = {"createHomeServerConnectionConfig", "Lorg/matrix/androidsdk/HomeServerConnectionConfig;", "homeServerUrl", "", "identityServerUrl", "config", "getBytesFromString", "", "certificateFingerprint", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 2, mv = {1, 1, 13})
/* compiled from: HomeServerConnectionConfigFactory.kt */
public final class HomeServerConnectionConfigFactoryKt {
    public static final HomeServerConnectionConfig createHomeServerConnectionConfig(String str, String str2) {
        Intrinsics.checkParameterIsNotNull(str, "homeServerUrl");
        Builder withHomeServerUri = new Builder().withHomeServerUri(Uri.parse(str));
        if (str2 != null) {
            withHomeServerUri.withIdentityServerUri(Uri.parse(str2));
        }
        for (String bytesFromString : PinningConfigurationKt.getCERTIFICATE_FINGERPRINT_LIST()) {
            withHomeServerUri.addAllowedFingerPrint(new Fingerprint(HashType.SHA256, getBytesFromString(bytesFromString)));
        }
        HomeServerConnectionConfig build = withHomeServerUri.withTlsLimitations(true, VERSION.SDK_INT <= 19).withPin(true).build();
        Intrinsics.checkExpressionValueIsNotNull(build, "HomeServerConnectionConf…ING)\n            .build()");
        return build;
    }

    public static final HomeServerConnectionConfig createHomeServerConnectionConfig(HomeServerConnectionConfig homeServerConnectionConfig) {
        Intrinsics.checkParameterIsNotNull(homeServerConnectionConfig, "config");
        Builder withCredentials = new Builder().withHomeServerUri(homeServerConnectionConfig.getHomeserverUri()).withIdentityServerUri(homeServerConnectionConfig.getIdentityServerUri()).withAntiVirusServerUri(homeServerConnectionConfig.getAntiVirusServerUri()).withCredentials(homeServerConnectionConfig.getCredentials());
        for (String bytesFromString : PinningConfigurationKt.getCERTIFICATE_FINGERPRINT_LIST()) {
            withCredentials.addAllowedFingerPrint(new Fingerprint(HashType.SHA256, getBytesFromString(bytesFromString)));
        }
        HomeServerConnectionConfig build = withCredentials.withTlsLimitations(true, VERSION.SDK_INT <= 19).withPin(true).build();
        Intrinsics.checkExpressionValueIsNotNull(build, "HomeServerConnectionConf…ING)\n            .build()");
        return build;
    }

    public static final byte[] getBytesFromString(String str) {
        Intrinsics.checkParameterIsNotNull(str, "certificateFingerprint");
        List chunked = StringsKt.chunked(StringsKt.replace$default(str, ":", "", false, 4, (Object) null), 2);
        byte[] bArr = new byte[chunked.size()];
        Iterable<String> iterable = chunked;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        int i = 0;
        for (String parseLong : iterable) {
            int i2 = i + 1;
            bArr[i] = (byte) ((int) Long.parseLong(parseLong, 16));
            arrayList.add(Unit.INSTANCE);
            i = i2;
        }
        List list = (List) arrayList;
        return bArr;
    }
}

package fr.gouv.tchap.config;

import im.vector.BuildConfig;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\b"}, d2 = {"Lfr/gouv/tchap/config/TchapConfiguration;", "", "()V", "packageWhiteList", "", "", "getPackageWhiteList", "()Ljava/util/List;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: TchapConfiguration.kt */
public final class TchapConfiguration {
    public static final TchapConfiguration INSTANCE = new TchapConfiguration();
    private static final List<String> packageWhiteList = CollectionsKt.listOf(BuildConfig.APPLICATION_ID);

    private TchapConfiguration() {
    }

    public final List<String> getPackageWhiteList() {
        return packageWhiteList;
    }
}
